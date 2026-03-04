export interface FavoriteItem {
  movieId: string
  slug: string
  title: string
  description: string
  releaseYear: number
  genre: string
  durationInMinutes: number
  thumbnailUrl: string
  cast: string[]
  tags: string[]
}

export interface UserProfile {
  id: string
  username: string
  email: string | null
  avatarUrl: string | null
  locale: string
  createdAt: string
  updatedAt: string
  favorites: FavoriteItem[]
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
