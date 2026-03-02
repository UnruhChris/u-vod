output "cosmos_db_endpoint" {
  value       = azurerm_cosmosdb_account.db_account.endpoint
  description = "The URL to connect to the database"
}

output "cosmos_db_primary_key" {
  value       = azurerm_cosmosdb_account.db_account.primary_key
  sensitive   = true
  description = "The secret key for the application"
}

output "cosmos_db_connection_string" {
  value     = azurerm_cosmosdb_account.db_account.primary_sql_connection_string
  sensitive = true
}
