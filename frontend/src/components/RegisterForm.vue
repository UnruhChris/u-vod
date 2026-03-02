<script setup lang="ts">
import { ref } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const auth = useAuthStore()
const router = useRouter()

const visibleUsername = ref('')
const email = ref('')
const submitting = ref(false)

async function onSubmit() {
  if (!visibleUsername.value.trim() || !email.value.trim()) return
  submitting.value = true
  const ok = await auth.register(visibleUsername.value.trim(), email.value.trim())
  submitting.value = false
  if (ok) {
    router.replace('/home')
  }
}
</script>

<template>
  <form class="register-form" @submit.prevent="onSubmit">
    <h2 class="register-form__title">Completa il tuo profilo</h2>
    <p class="register-form__desc">
      Scegli un nome visibile e inserisci la tua email per iniziare.
    </p>

    <label class="register-form__label">
      Nome utente
      <input
        v-model="visibleUsername"
        type="text"
        class="register-form__input"
        placeholder="Il tuo nome visibile"
        required
        autocomplete="username"
      />
    </label>

    <label class="register-form__label">
      Email
      <input
        v-model="email"
        type="email"
        class="register-form__input"
        placeholder="email@esempio.it"
        required
        autocomplete="email"
      />
    </label>

    <p v-if="auth.error" class="register-form__error">{{ auth.error }}</p>

    <button type="submit" class="register-form__btn" :disabled="submitting">
      {{ submitting ? 'Registrazione...' : 'Registrati' }}
    </button>
  </form>
</template>

<style scoped>
.register-form {
  display: flex;
  flex-direction: column;
  gap: var(--space-md);
  width: 100%;
  max-width: 420px;
}

.register-form__title {
  font-size: 1.8rem;
  font-weight: 700;
  color: var(--color-text);
  margin: 0;
}

.register-form__desc {
  color: var(--color-text-muted);
  font-size: 0.95rem;
  margin: 0;
}

.register-form__label {
  display: flex;
  flex-direction: column;
  gap: var(--space-xs);
  color: var(--color-text-muted);
  font-size: 0.875rem;
  font-weight: 500;
}

.register-form__input {
  padding: var(--space-sm) var(--space-md);
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  color: var(--color-text);
  font-size: 1rem;
  outline: none;
  transition: border-color var(--transition-fast);
}

.register-form__input:focus {
  border-color: var(--color-brand);
}

.register-form__error {
  color: #ef4444;
  font-size: 0.875rem;
  margin: 0;
}

.register-form__btn {
  padding: var(--space-sm) var(--space-lg);
  background: var(--gradient-brand);
  color: var(--color-text);
  font-size: 1rem;
  font-weight: 600;
  border: none;
  border-radius: var(--radius-md);
  cursor: pointer;
  transition:
    transform var(--transition-fast),
    box-shadow var(--transition-fast);
}

.register-form__btn:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: var(--shadow-glow);
}

.register-form__btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>
