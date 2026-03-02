variable "project_name" {
  description = "Project name, used for resource naming"
  default     = "u-vod"
}

variable "location" {
  description = "Default location to assign to resources"
  default     = "italynorth"
}

variable "location_alt" {
  description = "Alternative location to assign to resources"
  default     = "germanywestcentral"
}

variable "common_tags" {
  description = "Tags to apply to all resources"
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
  description = "My GitHub username"
  type        = string
  default     = "unruhchris"
}
