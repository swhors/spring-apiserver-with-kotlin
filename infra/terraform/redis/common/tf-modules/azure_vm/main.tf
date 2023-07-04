# Configure the Azure provider
terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 2.65"
    }
  }
  required_version = ">= 0.14.9"
}

data "azurerm_virtual_network" "vnet" {
  name                = "backend_user_info_vnet"
  resource_group_name = var.rg_name
}

data "azurerm_subnet" "subnet" {
  name                 = "redis"
  resource_group_name  = var.rg_name
  virtual_network_name = "backend_user_info_vnet"
}

resource "azurerm_public_ip" "pubip" {
  name                = "${var.prefix}-publicip"
  resource_group_name = var.rg_name
  location            = var.rg_location
  allocation_method   = "Static"
}

resource "azurerm_network_interface" "pubnic" {
  name                = "${var.prefix}-pub-nic"
  location            = var.rg_location
  resource_group_name = var.rg_name

  ip_configuration {
    name                          = "configuration"
    subnet_id                     = data.azurerm_subnet.subnet.id
    private_ip_address_allocation = "Dynamic"
    public_ip_address_id          = azurerm_public_ip.pubip.id
  }
}

resource "azurerm_network_security_group" "nsg" {
  name                = "${var.prefix}-nsg"
  location            = var.rg_location
  resource_group_name = var.rg_name
}

resource "azurerm_network_security_rule" "allow_ssh_ports" {
  access                      = "Allow"
  direction                   = "Inbound"
  name                        = "AllowSSH"
  priority                    = 100
  protocol                    = "tcp"
  source_port_range           = "*"
  source_address_prefix       = "*"
  destination_address_prefix  = "*"
  destination_port_ranges     = var.allowed_port_ranges
  network_security_group_name = azurerm_network_security_group.nsg.name
  resource_group_name         = var.rg_name
}

resource "azurerm_network_interface_security_group_association" "nsg_association" {
  network_interface_id      = azurerm_network_interface.pubnic.id
  network_security_group_id = azurerm_network_security_group.nsg.id
}

resource "azurerm_virtual_machine" "vm" {
  name                          = "${var.prefix}-vm"
  location                      = var.rg_location
  resource_group_name           = var.rg_name
  network_interface_ids         = [azurerm_network_interface.pubnic.id]
  vm_size                       = var.vm_type
  delete_os_disk_on_termination = true

  storage_image_reference {
    publisher = "Canonical"
    offer     = "0001-com-ubuntu-server-jammy"
    sku       = "22_04-lts-gen2"
    version   = "latest"
  }

  storage_os_disk {
    name              = "${var.prefix}-os-disk"
    caching           = "ReadWrite"
    create_option     = "FromImage"
    managed_disk_type = "${var.disk_type}"
  }

  os_profile {
    computer_name  = "${var.prefix}-os-profile"
    admin_username = var.username
  }

  os_profile_linux_config {
    disable_password_authentication = true
    ssh_keys {
      key_data = var.ssh_public_key
      path     = "/home/default-user-name/.ssh/authorized_keys"
    }
  }

  provisioner "remote-exec" {
    connection {
      type        = "ssh"
      host        = azurerm_public_ip.pubip.ip_address
      user        = var.username
      private_key = file(var.ssh_private_key_path)
    }

    inline = [
      "sudo apt -y update",
      "sudo apt -y upgrade",
      "sudo apt install -y wget curl software-properties-common apt-transport-https ca-certificates lsb-release",
      "sudo apt -y update",
      "sudo apt install -y redis-server",
      "sudo apt -y update",
      "sudo sed -i 's/# requirepass foobared/requirepass [INPUT YOUR PASSWORD]/g' /etc/redis/redis.conf",
      "sudo sed -i 's/bind 127.0.0.1 ::1/bind 0.0.0.0/g' /etc/redis/redis.conf",
      "sudo sed -i 's/appendonly no/appendonly yes/g' /etc/redis/redis.conf",
      "sudo sed -i 's/timeout 0/timeout 180/g' /etc/redis/redis.conf",
      "sudo sed -i 's/port 6379/port 16379/g' /etc/redis/redis.conf",
      "sudo systemctl enable --now redis-server",
      "sudo systemctl restart redis-server",
      "sudo ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime",
      "sudo echo Asia/Seoul 2>&1 | sudo tee /etc/timezone"
    ]
  }
}