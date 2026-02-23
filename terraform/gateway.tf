resource "azurerm_container_app" "gateway_service" {
  name                         = "${var.project_name}-gateway"
  resource_group_name          = azurerm_resource_group.rg.name
  container_app_environment_id = azurerm_container_app_environment.aca_env.id
  revision_mode                = "Single"

  identity {
    type         = "UserAssigned"
    identity_ids = [azurerm_user_assigned_identity.backend_identity.id]
  }

  template {
    container {
      name   = "gateway-container"
      image  = "ghcr.io/${var.github_username}/uvod-gateway:latest"
      cpu    = 0.5
      memory = "1.0Gi"

      env {
        name  = "USER_SERVICE_URL"
        value = "https://${azurerm_container_app.user_service.ingress[0].fqdn}"
      }

      env {
        name  = "SERVER_FORWARD_HEADERS_STRATEGY"
        value = "framework"
      }
    }
  }

  ingress {
    external_enabled = true
    target_port      = 8080
    transport        = "auto"
    traffic_weight {
      percentage      = 100
      latest_revision = true
    }
  }
}
