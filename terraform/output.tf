output "cosmos_db_endpoint" {
  value       = azurerm_cosmosdb_account.db_account.endpoint
  description = "The URL to connect to the database"
}

output "gateway_url" {
  value       = "https://${azurerm_container_app.gateway_service.ingress[0].fqdn}"
  description = "Public URL of the API Gateway"
}
