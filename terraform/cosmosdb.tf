resource "azurerm_cosmosdb_account" "db_account" {
    name = "${var.project_name}-cosmos-account"
    location = var.location
    resource_group_name = azurerm_resource_group.rg.name

    offer_type = "Standard"
    kind = "GlobalDocumentDB"
    free_tier_enabled = true

    geo_location {  
      location = var.location
      failover_priority = 0
    }
    backup {
      type = "Periodic"
      storage_redundancy = "Local"
      interval_in_minutes = 240 
      retention_in_hours  = 8   
    }
    consistency_policy {
      consistency_level = "Session"
    }
  
    tags = merge(var.common_tags, {
    Component = "db"
  })
}

resource "azurerm_cosmosdb_sql_database" "db" {
  name                = "uvod-db"
  resource_group_name = azurerm_resource_group.rg.name
  account_name        = azurerm_cosmosdb_account.db_account.name
  autoscale_settings {
    max_throughput = 1000
  }
}

resource "azurerm_cosmosdb_sql_container" "users" {
  name                = "users"
  resource_group_name = azurerm_resource_group.rg.name
  account_name        = azurerm_cosmosdb_account.db_account.name
  database_name       = azurerm_cosmosdb_sql_database.db.name
  
  partition_key_paths = ["/id"]
}