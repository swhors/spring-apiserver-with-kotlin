variable "ssh_private_key_path" {
  type        = string
  default     = "~/.ssh/default-user-name.pem"
  description = "VM연결을 위한 SSH Private Key 경로."
}