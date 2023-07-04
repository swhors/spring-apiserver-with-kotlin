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
  default     = "Standard_D2s_v3"
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

variable "allowed_port_ranges" {
  type = list(string)
  default = [22]
  description = "오픈할 port range 정보"
}

variable "allowed_http_port_ranges" {
  type = list(string)
  default = [80, 443]
  description = "오픈할 http port range 정보"
}


variable "vm_count" {
  type        = number
  default     = 1
  description = "vm의 수"
}

variable "hostname" {
  type        = string
  default     = "imageproxy"
  description = "vm's host name"
}

variable "ssh_private_key_path" {
  type        = string
  default     = "~/.ssh/default-user-name.pem"
  description = "VM연결을 위한 SSH Private Key 경로."
}

variable "nginx_config_src_certbot_path" {
  type        = string
  description = "nginx configuration file for certbot"
  default     = "tester-image.koreacentral.cloudapp.azure.com.conf_certbot"
}

variable "nginx_config_src_path" {
  type        = string
  description = "nginx configuration file path"
  default     = "tester-image.koreacentral.cloudapp.azure.com.conf"
}

variable "nginx_config_des_path" {
  type        = string
  description = "nginx configuration file path"
  default     = "/etc/nginx/conf.d/tester-image.koreacentral.cloudapp.azure.com.conf"
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
