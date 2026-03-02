import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { ClientPrincipal, AuthPayload, UserProfile } from '@/types/User'

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
  async function register(visibleUsername: string, email: string) {
    isLoading.value = true
    error.value = null
    try {
      const response = await fetch('/api/user/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ visibleUsername, email }),
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
        const data = await response.json()
        error.value = data.message || 'Error during registration'
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
    checkAuth,
    checkRegistration,
    fetchProfile,
    register,
    initialize,
    logout,
  }
})
