variable "project_name" {
  description = "Nome del progetto, usato per il naming delle risorse"
  default     = "u-vod"
}

variable "location" {
  description = "Location di default da assegnare alle risorse"
  default     = "italynorth"
}

variable "location_alt" {
  description = "Location di default da assegnare alle risorse"
  default     = "germanywestcentral"
}

variable "common_tags" {
  description = "Tag da applicare a tutte le risorse"
  default = {
    Environment = "Dev"
    Project     = "U-VoD"
    Owner       = "Unruh"
  }
}

variable "subscription_id" {
  type        = string
  description = "Azure Subscription ID"
}

variable "github_username" {
  description = "Il mio username GitHub"
  type        = string
  default     = "unruhchris"
}
