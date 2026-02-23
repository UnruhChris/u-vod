# Questo blocco crea una stringa casuale di 6 caratteri (es. "a9x2b1")
# per rendere univoci i nomi delle risorse globali.
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