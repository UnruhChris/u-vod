<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

// --- 1. Interfacce ---

interface RegisterForm {
  visibleUsername: string
  email: string
}

// --- 2. Stato Reattivo ---

const route = useRoute()
const router = useRouter()

const userId = ref<string>('')
const userDetails = ref<string>('')
const provider = ref<string>('')

const form = ref<RegisterForm>({
  visibleUsername: '',
  email: '',
})

const isSubmitting = ref<boolean>(false)
const errorMessage = ref<string>('')
const successMessage = ref<string>('')

// --- 3. Logica ---

onMounted(() => {
  // Recupera i dati passati dalla query string
  userId.value = (route.query.userId as string) ?? ''
  userDetails.value = (route.query.userDetails as string) ?? ''
  provider.value = (route.query.provider as string) ?? ''

  // Se manca userId, l'utente non è autenticato correttamente
  if (!userId.value) {
    router.push({ name: 'login' })
    return
  }

  // Pre-compila il form con i dati disponibili
  form.value.visibleUsername = userDetails.value
})

/**
 * Invia il form di registrazione al backend
 */
async function submitRegistration(): Promise<void> {
  // Validazione base
  if (!form.value.visibleUsername.trim()) {
    errorMessage.value = 'Il nome utente è obbligatorio.'
    return
  }

  isSubmitting.value = true
  errorMessage.value = ''
  successMessage.value = ''

  try {
    const response = await fetch('/api/user/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify({
        visibleUsername: form.value.visibleUsername.trim(),
        email: form.value.email.trim(),
      }),
    })

    if (response.ok) {
      successMessage.value = 'Registrazione completata!'
      setTimeout(() => {
        router.push({ name: 'login' })
      }, 1500)
    } else if (response.status === 401) {
      // NUOVO: Gestione sessione scaduta
      errorMessage.value = 'Sessione scaduta. Effettua nuovamente il login.'
      setTimeout(() => {
        window.location.href = '/.auth/login/github' // o altro provider
      }, 2000)
    } else if (response.status === 409) {
      errorMessage.value = 'Utente già registrato.'
    } else {
      const data = await response.json()
      errorMessage.value = data.message || 'Errore durante la registrazione'
    }
  } catch (error) {
    errorMessage.value = `Errore di rete: ${error instanceof Error ? error.message : String(error)}`
  } finally {
    isSubmitting.value = false
  }
}

/**
 * Torna al login (logout implicito)
 */
function goBack(): void {
  router.push({ name: 'login' })
}
</script>

<template>
  <div class="container">
    <h1>Registrazione U-VoD 🎬</h1>

    <div class="register-box">
      <p class="welcome-text">
        Benvenuto! È la prima volta che accedi con questo account.
        <br />
        Completa la registrazione per continuare.
      </p>

      <!-- Info account collegato -->
      <div class="account-info">
        <p><strong>Provider:</strong> {{ provider }}</p>
        <p><strong>Account:</strong> {{ userDetails }}</p>
        <p>
          <strong>ID:</strong> <code class="code-pill">{{ userId }}</code>
        </p>
      </div>

      <hr class="divider" />

      <!-- Form di registrazione -->
      <form @submit.prevent="submitRegistration" class="register-form">
        <div class="form-group">
          <label for="visibleUsername">Nome utente visibile *</label>
          <input
            id="visibleUsername"
            v-model="form.visibleUsername"
            type="text"
            class="input-field"
            placeholder="Come vuoi essere chiamato?"
            maxlength="50"
            required
          />
          <small class="hint">Questo nome sarà visibile sulla piattaforma.</small>
        </div>

        <div class="form-group">
          <label for="email">Email *</label>
          <input
            id="email"
            v-model="form.email"
            type="email"
            class="input-field"
            placeholder="email@esempio.com"
            required
          />
          <small class="hint">Per ricevere notifiche e aggiornamenti.</small>
        </div>

        <!-- Messaggi -->
        <div v-if="errorMessage" class="message error">⚠️ {{ errorMessage }}</div>
        <div v-if="successMessage" class="message success">✅ {{ successMessage }}</div>

        <!-- Bottoni -->
        <div class="button-group">
          <button type="submit" class="btn btn-primary" :disabled="isSubmitting">
            {{ isSubmitting ? 'Registrazione...' : 'Completa Registrazione' }}
          </button>
          <button type="button" class="btn btn-secondary" @click="goBack" :disabled="isSubmitting">
            Annulla
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
.container {
  max-width: 500px;
  margin: 2rem auto;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  padding: 1rem;
}
h1 {
  font-size: 1.5rem;
  color: #0078d4;
  margin-bottom: 1.5rem;
  text-align: center;
}
.register-box {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}
.welcome-text {
  color: #333;
  font-size: 1rem;
  line-height: 1.5;
  margin-bottom: 1rem;
}
.account-info {
  background: #f0f7ff;
  padding: 1rem;
  border-radius: 6px;
  border-left: 4px solid #0078d4;
}
.account-info p {
  margin: 0.3rem 0;
  font-size: 0.9rem;
}
.code-pill {
  background: #e0e0e0;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 0.85em;
  font-family: 'Consolas', monospace;
}
.divider {
  margin: 1.5rem 0;
  border: 0;
  border-top: 1px solid #eee;
}
.register-form {
  display: flex;
  flex-direction: column;
  gap: 1.2rem;
}
.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.4rem;
}
.form-group label {
  font-weight: 600;
  color: #333;
  font-size: 0.95rem;
}
.input-field {
  padding: 0.7rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 1rem;
  transition: border-color 0.2s;
}
.input-field:focus {
  outline: none;
  border-color: #0078d4;
  box-shadow: 0 0 0 2px rgba(0, 120, 212, 0.1);
}
.hint {
  color: #888;
  font-size: 0.8rem;
}
.message {
  padding: 0.8rem;
  border-radius: 4px;
  font-size: 0.9rem;
}
.message.error {
  background: #fff3f3;
  border: 1px solid #ffcdd2;
  color: #c62828;
}
.message.success {
  background: #e8f5e9;
  border: 1px solid #a5d6a7;
  color: #2e7d32;
}
.button-group {
  display: flex;
  gap: 0.8rem;
  margin-top: 0.5rem;
}
.btn {
  flex: 1;
  padding: 0.8rem 1.2rem;
  border-radius: 4px;
  font-weight: 600;
  cursor: pointer;
  border: none;
  font-size: 1rem;
  transition: opacity 0.2s;
}
.btn-primary {
  background-color: #0078d4;
  color: white;
}
.btn-secondary {
  background-color: #6c757d;
  color: white;
}
.btn:hover:not(:disabled) {
  opacity: 0.9;
}
.btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
