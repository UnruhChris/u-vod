variable "project_name" {
    description = "Nome del progetto"
    default = "u-void"
}

variable "location" {
    description = "Location di default da assegnare alle risorse"
    default = "westeurope"
}

variable "location_alt" {
    description = "Location di default da assegnare alle risorse"
    default = "germanywestcentral"
}


variable "common_tags" {
    description = "Tag da applicare a tutte le risorse"
    default = {
        Environment = "Development"
        Project = "U-Void"
        Owner = "Unruh"
    }
}