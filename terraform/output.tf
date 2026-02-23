output "cosmos_db_endpoint" {
  value = azurerm_cosmosdb_account.db_account.endpoint
  description = "L'URL per connettersi al database"
}

output "cosmos_db_primary_key" {
  value = azurerm_cosmosdb_account.db_account.primary_key
  sensitive = true 
  description = "La chiave segreta per l'applicazione"
}

output "cosmos_db_connection_string" {
  value = azurerm_cosmosdb_account.db_account.primary_sql_connection_string
  sensitive = true
}