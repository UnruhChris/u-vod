resource "azurerm_container_app" "user_service" {
  name                         = "${var.project_name}-user"
  resource_group_name          = azurerm_resource_group.rg.name
  container_app_environment_id = azurerm_container_app_environment.aca_env.id
  revision_mode                = "Single"

  identity {
    type         = "UserAssigned"
    identity_ids = [azurerm_user_assigned_identity.backend_identity.id]
  }

  template {
    container {
      name   = "user-container"
      image  = "ghcr.io/${var.github_username}/uvod-user:latest"
      cpu    = 0.5
      memory = "1.0Gi"

      env {
        name  = "AZURE_COSMOS_URI"
        value = azurerm_cosmosdb_account.db_account.endpoint
      }

      env {
        name  = "AZURE_COSMOS_DB"
        value = azurerm_cosmosdb_sql_database.db.name
      }

      env {
        name  = "AZURE_CLIENT_ID"
        value = azurerm_user_assigned_identity.backend_identity.client_id
      }

      # Startup probe per assicurarsi che il container sia pronto prima di accettare traffico
      startup_probe {
        transport               = "TCP"
        port                    = 8080
        initial_delay           = 30
        interval_seconds        = 5
        failure_count_threshold = 10
      }

    }

    min_replicas = 0
  }


  # INGRESS INTERNO: Solo gli altri container nello stesso environment possono chiamarlo
  ingress {
    external_enabled = false
    target_port      = 8080
    transport        = "auto"
    traffic_weight {
      percentage      = 100
      latest_revision = true
    }
  }
}
