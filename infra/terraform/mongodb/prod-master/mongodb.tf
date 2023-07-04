locals {
  vm_username = "default-user-name"
}

module "azure_vm" {
  source               = "../../common/tf-modules/azure_vm"
  rg_location          = data.azurerm_resource_group.service_rg.location
  rg_name              = data.azurerm_resource_group.service_rg.name
  prefix               = "db-vm01"
  vm_type              = "Standard_B4ms"
  username             = local.vm_username
  ssh_public_key       = data.azurerm_ssh_public_key.default-user-name_ssh_pub_key.public_key
  allowed_port_ranges  = [22]
  vnet_name            = "db-vnet"
  sub_net_name         = "db-sub-net"
  cmd_lines            = [
    "sudo apt -y update",
    "sudo mkdir -p /data/mongodb",
    "sudo apt install -y -f wget curl software-properties-common apt-transport-https ca-certificates lsb-release",
    "curl -fsSL https://www.mongodb.org/static/pgp/server-6.0.asc|sudo gpg --dearmor -o /etc/apt/trusted.gpg.d/mongodb-6.gpg",
    "echo \"deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu $(lsb_release -cs)/mongodb-org/6.0 multiverse\" | sudo tee /etc/apt/sources.list.d/mongodb-org-6.0.list",
    "sudo apt -y update",
    "sudo apt -y install mongodb-org",
    "sudo systemctl enable --now mongod",
    "sudo chown mongodb:mongodb -fR /data",
    "sudo systemctl stop mongod",
    "sudo sed -i 's/storage:/#storage:/g' /etc/mongod.conf",
    "sudo sed -i 's/  dbPath:/#  dbPath:/g' /etc/mongod.conf",
    "sudo sed -i 's/systemLog:/#systemLog:/g' /etc/mongod.conf",
    "sudo sed -i 's/destination/#destiantion/g' /etc/mongod.conf",
    "sudo sed -i 's/  destination:/#  destination:/g' /etc/mongod.conf",
    "sudo sed -i 's/  logAppend/#  logAppend/g' /etc/mongod.conf",
    "sudo sed -i 's/  path/#  path/g' /etc/mongod.conf",
    "sudo sed -i 's/net/#net/g' /etc/mongod.conf",
    "sudo sed -i 's/  port/#  port/g' /etc/mongod.conf",
    "sudo sed -i 's/  bindIp/#  bindIp/g' /etc/mongod.conf",
    "sudo sed -i 's/process/#process/g' /etc/mongod.conf",
    "sudo sed -i 's/  time/#  time/g' /etc/mongod.conf",
    "echo \"storage:\"| sudo tee -a /etc/mongod.conf",
    "echo \"  dbPath: /data/mongodb\"| sudo tee -a /etc/mongod.conf",
    "echo \"  engine: wiredTiger\"| sudo tee -a /etc/mongod.conf",
    "echo \"  directoryPerDB: true\"| sudo tee -a /etc/mongod.conf",
    "echo \"  wiredTiger:\"| sudo tee -a /etc/mongod.conf",
    "echo \"    engineConfig:\"| sudo tee -a /etc/mongod.conf",
    "echo \"      cacheSizeGB: 0.4\"| sudo tee -a /etc/mongod.conf",
    "echo \"      journalCompressor: snappy\"| sudo tee -a /etc/mongod.conf",
    "echo \"    collectionConfig:\"| sudo tee -a /etc/mongod.conf",
    "echo \"      blockCompressor: snappy\"| sudo tee -a /etc/mongod.conf",
    "echo \"    indexConfig:\"| sudo tee -a /etc/mongod.conf",
    "echo \"      prefixCompression: true\"| sudo tee -a /etc/mongod.conf",
    "echo \"systemLog:\"| sudo tee -a /etc/mongod.conf",
    "echo \"  destination: file\"| sudo tee -a /etc/mongod.conf",
    "echo \"  path: /var/log/mongodb/mongod.log\"| sudo tee -a /etc/mongod.conf",
    "echo \"  logAppend: true\"| sudo tee -a /etc/mongod.conf",
    "echo \"  logRotate: rename\"| sudo tee -a /etc/mongod.conf",
    "echo \"  timeStampFormat: iso8601-utc\"| sudo tee -a /etc/mongod.conf",
    "echo \"  quiet: true\"| sudo tee -a /etc/mongod.conf",
    "echo \"net:\"| sudo tee -a /etc/mongod.conf",
    "echo \"  bindIpAll: true\"| sudo tee -a /etc/mongod.conf",
    "echo \"  port: 37027\"| sudo tee -a /etc/mongod.conf",
    "echo \"  wireObjectCheck: false\"| sudo tee -a /etc/mongod.conf",
    "echo \"  unixDomainSocket:\"| sudo tee -a /etc/mongod.conf",
    "echo \"    enabled: true\"| sudo tee -a /etc/mongod.conf",
    "echo \"#setParameter:\"| sudo tee -a /etc/mongod.conf",
    "echo \"#  enableLocalhostAuthBypass: true\"| sudo tee -a /etc/mongod.conf",
    "echo \"processManagement:\"| sudo tee -a /etc/mongod.conf",
    "echo \"  timeZoneInfo: /usr/share/zoneinfo\"| sudo tee -a /etc/mongod.conf",
    "echo \"  fork: true\"| sudo tee -a /etc/mongod.conf",
    "echo \"  pidFilePath: /var/run/mongodb/mongod.pid\"| sudo tee -a /etc/mongod.conf",
    "echo \"replication:\"| sudo tee -a /etc/mongod.conf",
    "echo \"  replSetName: rs0\"| sudo tee -a /etc/mongod.conf",
    "echo \"#security:\"| sudo tee -a /etc/mongod.conf",
    "echo \"#  authorization: enabled\"| sudo tee -a /etc/mongod.conf",
    "echo \"#  keyFile: /etc/mongo/mongodb.crt\"| sudo tee -a /etc/mongod.conf",
    "sudo systemctl stop mongod",
    "sudo ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime",
    "sudo echo Asia/Seoul 2>&1 | sudo tee /etc/timezone"
  ]
}
