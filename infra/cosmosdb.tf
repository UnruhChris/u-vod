resource "azurerm_cosmosdb_account" "db_account" {
    name = "${var.project_name}-cosmos-account"
    location = var.location_alt
    resource_group_name = azurerm_resource_group.rg.name
    offer_type = "Standard"
    free_tier_enabled = true
    geo_location {  
      location = var.location_alt
      failover_priority = 0
    }
    backup {
      type = "Periodic"
      storage_redundancy = "Local"
    }
    consistency_policy {
      consistency_level = "Session"
    }
    tags = merge(var.common_tags, {
    Component = "db"
  })
}