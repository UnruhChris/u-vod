export interface UserProfile {
  id: string
  visibleUsername: string
  email: string | null
  registrationDate: string
  favorites: string[]
  watchHistory: string[]
}

export interface ClientPrincipal {
  identityProvider: string
  userId: string
  userDetails: string
  userRoles: string[]
}

export interface AuthPayload {
  clientPrincipal: ClientPrincipal | null
}
