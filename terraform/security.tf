# USER ASSIGNED IDENTITY 
resource "azurerm_user_assigned_identity" "backend_identity" {
  location            = var.location
  name                = "${var.project_name}-backend-identity"
  resource_group_name = azurerm_resource_group.rg.name
  tags = merge(var.common_tags, {
    Component = "security"
  })
}


resource "azurerm_cosmosdb_sql_role_assignment" "user_service_access" {
  resource_group_name = azurerm_resource_group.rg.name
  account_name        = azurerm_cosmosdb_account.db_account.name
  role_definition_id  = "${azurerm_cosmosdb_account.db_account.id}/sqlRoleDefinitions/00000000-0000-0000-0000-000000000002"
  principal_id        = azurerm_user_assigned_identity.backend_identity.principal_id
  scope               = azurerm_cosmosdb_account.db_account.id
}
