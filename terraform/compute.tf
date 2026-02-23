# LOG ANALYTICS WORKSPACE
resource "azurerm_log_analytics_workspace" "law" {
  name                = "${var.project_name}-logs"
  location            = var.location
  resource_group_name = azurerm_resource_group.rg.name
  sku                 = "PerGB2018"
  retention_in_days   = 30
  daily_quota_gb      = 0.15
  tags = merge(var.common_tags, {
    Component = "log"
  })
}

# CONTAINER APPS ENVIRONMENT
resource "azurerm_container_app_environment" "aca_env" {
  name                       = "${var.project_name}-env"
  location                   = var.location
  resource_group_name        = azurerm_resource_group.rg.name
  log_analytics_workspace_id = azurerm_log_analytics_workspace.law.id

  workload_profile {
    name                  = "Consumption"
    workload_profile_type = "Consumption"
  }

  tags = merge(var.common_tags, {
    Component = "backend"
  })
}


