<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

// --- 1. Definizione delle Interfacce ---

interface ClientPrincipal {
  identityProvider: string
  userId: string
  userDetails: string
  userRoles: string[]
  claims?: { typ: string; val: string }[]
}

interface AuthPayload {
  clientPrincipal: ClientPrincipal | null
}

interface UserProfile {
  id: string
  visibleUsername: string
  email: string | null
  registrationDate: string
  favorites: string[]
  watchHistory: string[]
}

// --- 2. Stato Reattivo ---

const router = useRouter()
const userInfo = ref<ClientPrincipal | null>(null)
const userProfile = ref<UserProfile | null>(null)
const isLoading = ref<boolean>(true)
const isLoadingProfile = ref<boolean>(false)
const profileError = ref<string>('')

// --- 3. Logica ---

async function getUserInfo(): Promise<void> {
  try {
    const response = await fetch('/.auth/me')
    const payload: AuthPayload = await response.json()
    userInfo.value = payload.clientPrincipal

    // Se l'utente è autenticato, verifica se esiste nel DB
    if (payload.clientPrincipal) {
      await checkUserExists()
    }
  } catch (error) {
    console.error('Errore nel recupero dati utente:', error)
  } finally {
    isLoading.value = false
  }
}

/**
 * Verifica se l'utente è registrato tramite l'endpoint dedicato.
 * Se registrato, carica il profilo completo.
 * Se non registrato, reindirizza alla pagina di registrazione.
 */
async function checkUserExists(): Promise<void> {
  isLoadingProfile.value = true
  profileError.value = ''

  try {
    // Step 1: Verifica registrazione tramite endpoint dedicato
    const checkResponse = await fetch('/api/user/is-registered', {
      method: 'GET',
      credentials: 'include',
    })

    if (checkResponse.status === 401) {
      profileError.value = 'Sessione scaduta. Effettua nuovamente il login.'
      return
    }

    if (!checkResponse.ok) {
      profileError.value = `Errore dal server: ${checkResponse.status} - ${checkResponse.statusText}`
      return
    }

    const { registered } = (await checkResponse.json()) as { registered: boolean }

    if (!registered) {
      // Utente non registrato, reindirizza alla registrazione
      router.push({
        name: 'register',
        query: {
          userId: userInfo.value?.userId,
          userDetails: userInfo.value?.userDetails,
          provider: userInfo.value?.identityProvider,
        },
      })
      return
    }

    // Step 2: Utente registrato, carica il profilo completo
    const profileResponse = await fetch('/api/user/profile', {
      method: 'GET',
      credentials: 'include',
    })

    if (profileResponse.ok) {
      userProfile.value = await profileResponse.json()
    } else {
      profileError.value = `Errore nel caricamento del profilo: ${profileResponse.status}`
    }
  } catch (error) {
    profileError.value = `Errore di rete: ${error instanceof Error ? error.message : String(error)}`
  } finally {
    isLoadingProfile.value = false
  }
}

/**
 * Ricarica il profilo utente
 */
async function refreshProfile(): Promise<void> {
  await checkUserExists()
}

// --- 4. Lifecycle Hooks ---
onMounted(() => {
  getUserInfo()
})
</script>

<template>
  <div class="container">
    <h1>U-VoD Dev Environment ☁️</h1>

    <div v-if="isLoading" class="loading">Caricamento stato autenticazione...</div>

    <div v-else>
      <!-- Utente NON autenticato -->
      <div v-if="!userInfo" class="auth-box">
        <p>Stato: 🔴 Non autenticato</p>
        <p class="description">Clicca qui sotto per simulare il login tramite SWA CLI.</p>
        <a href="/.auth/login/github" class="btn btn-login">Login con GitHub (Mock)</a>
      </div>

      <!-- Utente autenticato -->
      <div v-else class="auth-box">
        <p>Stato: 🟢 Autenticato</p>

        <!-- Info autenticazione SWA -->
        <div class="user-details">
          <p><strong>Provider:</strong> {{ userInfo.identityProvider }}</p>
          <p><strong>Username SWA:</strong> {{ userInfo.userDetails }}</p>
          <p>
            <strong>User ID:</strong> <code class="code-pill">{{ userInfo.userId }}</code>
          </p>
          <p><strong>Ruoli:</strong> {{ userInfo.userRoles.join(', ') }}</p>
        </div>

        <a href="/.auth/logout" class="link-logout">Logout</a>

        <hr class="divider" />

        <!-- Sezione Profilo da Cosmos DB -->
        <h3>Profilo Utente (Cosmos DB)</h3>

        <div v-if="isLoadingProfile" class="loading">Verifica profilo in corso...</div>

        <div v-else-if="profileError" class="error-box">
          <p>⚠️ {{ profileError }}</p>
          <button @click="refreshProfile" class="btn btn-secondary">Riprova</button>
        </div>

        <!-- Profilo esistente -->
        <div v-else-if="userProfile" class="profile-box">
          <div class="profile-details">
            <p><strong>Username visibile:</strong> {{ userProfile.visibleUsername }}</p>
            <p><strong>Email:</strong> {{ userProfile.email ?? 'Non disponibile' }}</p>
            <p>
              <strong>Registrato il:</strong>
              {{ new Date(userProfile.registrationDate).toLocaleDateString('it-IT') }}
            </p>
            <p><strong>Preferiti:</strong> {{ userProfile.favorites.length }} elementi</p>
            <p><strong>Cronologia:</strong> {{ userProfile.watchHistory.length }} elementi</p>
          </div>
        </div>

        <hr class="divider" />

        <!-- Debug: JSON completo -->
        <details class="debug-section">
          <summary>🔍 Debug: Dati grezzi</summary>
          <div class="response-box">
            <strong>Auth (SWA):</strong>
            <pre>{{ JSON.stringify(userInfo, null, 2) }}</pre>
          </div>
          <div class="response-box">
            <strong>Profilo (Cosmos DB):</strong>
            <pre>{{ JSON.stringify(userProfile, null, 2) }}</pre>
          </div>
        </details>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  max-width: 650px;
  margin: 2rem auto;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  padding: 1rem;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  background-color: #f9f9f9;
}
h1 {
  font-size: 1.5rem;
  color: #0078d4;
  margin-bottom: 1.5rem;
}
h3 {
  margin-top: 0;
  color: #333;
}
.auth-box {
  background: white;
  padding: 1.5rem;
  border-radius: 6px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}
.user-details p,
.profile-details p {
  margin: 0.5rem 0;
}
.code-pill {
  background: #eee;
  padding: 2px 5px;
  border-radius: 4px;
  font-size: 0.9em;
}
.description {
  color: #666;
  font-size: 0.9rem;
  margin-bottom: 1rem;
}
.btn {
  display: inline-block;
  padding: 0.6rem 1.2rem;
  border-radius: 4px;
  text-decoration: none;
  font-weight: 600;
  cursor: pointer;
  border: none;
  margin-right: 0.5rem;
}
.btn-login {
  background-color: #333;
  color: white;
}
.btn-primary {
  background-color: #0078d4;
  color: white;
}
.btn-secondary {
  background-color: #6c757d;
  color: white;
}
.btn:hover {
  opacity: 0.9;
}
.link-logout {
  color: #d93025;
  font-size: 0.9rem;
  display: inline-block;
  margin-top: 1rem;
}
.divider {
  margin: 1.5rem 0;
  border: 0;
  border-top: 1px solid #ddd;
}
.loading {
  color: #666;
  font-style: italic;
}
.error-box {
  background: #fff3f3;
  border: 1px solid #ffcdd2;
  padding: 1rem;
  border-radius: 4px;
  color: #c62828;
}
.profile-box {
  background: #f5f5f5;
  padding: 1rem;
  border-radius: 4px;
}
.debug-section {
  margin-top: 1rem;
}
.debug-section summary {
  cursor: pointer;
  color: #666;
  font-size: 0.9rem;
}
.response-box {
  margin-top: 0.5rem;
  padding: 1rem;
  background-color: #2d2d2d;
  color: #4ec9b0;
  border-radius: 4px;
  overflow-x: auto;
}
pre {
  margin: 0;
  font-family: 'Consolas', monospace;
  font-size: 0.85rem;
}
</style>
