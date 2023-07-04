locals {
  vm_username = "default-user-name"
}

module "azure_vm" {
  source               = "../../common/tf-modules/azure_vm"
  rg_location          = data.azurerm_resource_group.service_rg.location
  rg_name              = data.azurerm_resource_group.service_rg.name
  prefix               = "redis-cache-vm01-dev"
  vm_type              = "Standard_B2s"
  username             = local.vm_username
  ssh_public_key       = data.azurerm_ssh_public_key.default-user-name_ssh_pub_key.public_key
  allowed_port_ranges  = [22]
  vnet_name            = "db-vnet"
  sub_net_name         = "redis-sub-net"
  cmd_lines            = [
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
