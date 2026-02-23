resource "azurerm_storage_account" "sa" {
  name                     = "uvod${random_string.suffix.result}st"
  resource_group_name      = azurerm_resource_group.rg.name
  location                 = var.location
  
  account_tier             = "Standard"
  account_replication_type = "LRS"

  allow_nested_items_to_be_public = false 

  tags = merge(var.common_tags, {
    Component = "db"
  })
}


resource "azurerm_storage_container" "videos" {
  name                  = "videos"
  storage_account_id    = azurerm_storage_account.sa.id
}