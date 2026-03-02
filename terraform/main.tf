# This block creates a random string of 6 characters (e.g. "a9x2b1")
# to make global resource names unique.
resource "random_string" "suffix" {
  length  = 6
  special = false 
  upper   = false 
  numeric = true  
}

resource "azurerm_resource_group" "rg" {
  name     = "${var.project_name}-rg"
  location = var.location
  tags = var.common_tags
}