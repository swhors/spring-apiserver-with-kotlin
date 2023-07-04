terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 2.65"
    }
  }

  required_version = ">= 0.14.9"
}

provider "azurerm" {
  features {}
  subscription_id = "[Azure Subscription ID]" # Microsoft Azure 스폰서쉽(Converted to EA)
}

terraform {
  backend "azurerm" {
    resource_group_name  = "sysinfo_rg"
    storage_account_name = "[Storage Account Name for Terraform]"
    container_name       = "[Storgae Container Name for Terraform]"
    key                  = "db_vm03.terraform.tfstate"
  }
}

data "azurerm_resource_group" "sysinfo_rg" {
  name = "sysinfo_rg"
}

data "azurerm_resource_group" "service_rg" {
  name = "db_infra_rg"
}

data "azurerm_ssh_public_key" "default-user-name_ssh_pub_key" {
  name                = "default-user-name"
  resource_group_name = data.azurerm_resource_group.sysinfo_rg.name
}
