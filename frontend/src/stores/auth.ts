import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { ClientPrincipal, AuthPayload, UserProfile, FavoriteItem } from '@/types/User'

export const useAuthStore = defineStore('auth', () => {
  // --- State ---
  const clientPrincipal = ref<ClientPrincipal | null>(null)
  const userProfile = ref<UserProfile | null>(null)
  const isLoading = ref(false)
  const isRegistered = ref(false)
  const error = ref<string | null>(null)

  // --- Getters ---
  const isAuthenticated = computed(() => clientPrincipal.value !== null)
  const userId = computed(() => clientPrincipal.value?.userId ?? '')
  const userName = computed(() => clientPrincipal.value?.userDetails ?? '')
  const provider = computed(() => clientPrincipal.value?.identityProvider ?? '')
  const avatarUrl = computed(() => userProfile.value?.avatarUrl ?? null)
  const favorites = computed(() => userProfile.value?.favorites ?? [])

  /**
   * Checks if a film is in the user's favorites
   */
  function isFavorite(movieId: string): boolean {
    return favorites.value.some((f) => f.movieId === movieId)
  }

  // --- Actions ---

  /**
   * Checks SWA authentication status by calling /.auth/me
   */
  async function checkAuth() {
    isLoading.value = true
    error.value = null
    try {
      const response = await fetch('/.auth/me')
      const payload: AuthPayload = await response.json()
      clientPrincipal.value = payload.clientPrincipal
    } catch (e) {
      error.value = e instanceof Error ? e.message : String(e)
      clientPrincipal.value = null
    } finally {
      isLoading.value = false
    }
  }

  /**
   * Checks if the authenticated user is already registered in the backend
   */
  async function checkRegistration() {
    if (!clientPrincipal.value) return
    error.value = null
    try {
      const response = await fetch('/api/user/is-registered', {
        credentials: 'include',
      })
      if (response.status === 401) {
        error.value = 'Session expired. Please log in again.'
        return
      }
      if (!response.ok) throw new Error(`Error: ${response.status}`)
      const data = (await response.json()) as { registered: boolean }
      isRegistered.value = data.registered
    } catch (e) {
      error.value = e instanceof Error ? e.message : String(e)
    }
  }

  /**
   * Loads the full user profile
   */
  async function fetchProfile() {
    if (!clientPrincipal.value) return
    isLoading.value = true
    error.value = null
    try {
      const response = await fetch('/api/user/profile', {
        credentials: 'include',
      })
      if (!response.ok) throw new Error(`Error: ${response.status}`)
      userProfile.value = await response.json()
    } catch (e) {
      error.value = e instanceof Error ? e.message : String(e)
    } finally {
      isLoading.value = false
    }
  }

  /**
   * Registers a new user
   */
  async function register(data: {
    username: string
    email: string
    avatarUrl?: string
    locale?: string
  }) {
    isLoading.value = true
    error.value = null
    try {
      const response = await fetch('/api/user/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(data),
      })

      if (response.status === 401) {
        error.value = 'Session expired. Please log in again.'
        return false
      }
      if (response.status === 409) {
        error.value = 'User already registered.'
        return false
      }
      if (!response.ok) {
        const res = await response.json()
        error.value = res.message || 'Error during registration'
        return false
      }

      userProfile.value = await response.json()
      isRegistered.value = true
      return true
    } catch (e) {
      error.value = e instanceof Error ? e.message : String(e)
      return false
    } finally {
      isLoading.value = false
    }
  }

  /**
   * Updates the user's profile (username, avatarUrl, locale)
   */
  async function updateProfile(data: { username: string; avatarUrl?: string; locale?: string }) {
    isLoading.value = true
    error.value = null
    try {
      const response = await fetch('/api/user/profile', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(data),
      })

      if (!response.ok) {
        const res = await response.json()
        error.value = res.message || 'Error updating profile'
        return false
      }

      userProfile.value = await response.json()
      return true
    } catch (e) {
      error.value = e instanceof Error ? e.message : String(e)
      return false
    } finally {
      isLoading.value = false
    }
  }

  /**
   * Adds a film to favorites
   */
  async function addFavorite(item: FavoriteItem) {
    error.value = null
    try {
      const response = await fetch('/api/user/favorites', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(item),
      })

      if (!response.ok) {
        const res = await response.json()
        error.value = res.message || 'Error adding favorite'
        return false
      }

      userProfile.value = await response.json()
      return true
    } catch (e) {
      error.value = e instanceof Error ? e.message : String(e)
      return false
    }
  }

  /**
   * Removes a film from favorites
   */
  async function removeFavorite(movieId: string) {
    error.value = null
    try {
      const response = await fetch(`/api/user/favorites/${movieId}`, {
        method: 'DELETE',
        credentials: 'include',
      })

      if (!response.ok) {
        const res = await response.json()
        error.value = res.message || 'Error removing favorite'
        return false
      }

      userProfile.value = await response.json()
      return true
    } catch (e) {
      error.value = e instanceof Error ? e.message : String(e)
      return false
    }
  }

  /**
   * Initializes the full state: auth → registration → profile
   */
  async function initialize() {
    await checkAuth()
    if (isAuthenticated.value) {
      await checkRegistration()
      if (isRegistered.value) {
        await fetchProfile()
      }
    }
  }

  function logout() {
    window.location.href = '/.auth/logout'
  }

  return {
    clientPrincipal,
    userProfile,
    isLoading,
    isRegistered,
    error,
    isAuthenticated,
    userId,
    userName,
    provider,
    avatarUrl,
    favorites,
    isFavorite,
    checkAuth,
    checkRegistration,
    fetchProfile,
    register,
    updateProfile,
    addFavorite,
    removeFavorite,
    initialize,
    logout,
  }
})
