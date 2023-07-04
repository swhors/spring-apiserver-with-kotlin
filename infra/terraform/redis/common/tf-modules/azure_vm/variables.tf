variable "prefix" {
  description = "The Prefix used for all resources in this example"
}

variable "rg_name" {
  type        = string
  description = "ResourceGroup 이름"
}

variable "rg_location" {
  type        = string
  description = "지역 이름"
}

variable "vm_type" {
  type        = string
  description = "지역 이름"
  default     = "Standard_D4s_v3"
}

variable "ssh_public_key" {
  type        = string
  description = "SSH Public Key"
}

variable "username" {
  type        = string
  default     = "default-user-name"
  description = "VM User 이름"
}

variable "disk_type" {
  type        = string
  default     = "StandardSSD_LRS"
  description = "Disk Type"
}

variable "data_disk_size" {
  type        = number
  default     = 64
  description = "Disk Disk Size"
}

variable "allowed_port_ranges" {
  type = list(string)
  default = [22]
  description = "오픈할 port range 정보"
}

variable "vm_count" {
  type        = number
  default     = 2
  description = "vm의 수"
}

variable "ssh_private_key_path" {
  type        = string
  default     = "~/.ssh/default-user-name.pem"
  description = "VM연결을 위한 SSH Private Key 경로."
}

variable "redis_password" {
  type    = string
  default = "[input password for real]"
}

variable "vnet_name" {
  type        = string
  description = "Virtual Network Name"
  default     = "vnet_name"
}

variable "sub_net_name" {
  type        = string
  description = "Sub-Net Name"
  default     = "service_name"
}
