locals {
  vm_username = "default-user-name"
}

module "azure_vm" {
  source               = "../common/tf-modules/azure_vm"
  rg_location          = data.azurerm_resource_group.service_rg.location
  rg_name              = data.azurerm_resource_group.service_rg.name
  prefix               = "redis-cache-vm01"
  vm_type              = "Standard_B4ms"
  username             = local.vm_username
  ssh_public_key       = data.azurerm_ssh_public_key.default-user-name_ssh_pub_key.public_key
  allowed_port_ranges  = [22]
  vnet_name            = "db-vnet"
  sub_net_name         = "redis-sub-net"
}
