<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'
import { AVATARS, DEFAULT_AVATAR } from '@/constants/avatars'

const props = defineProps<{
  mode: 'register' | 'edit'
}>()

const auth = useAuthStore()
const router = useRouter()

const username = ref('')
const email = ref('')
const avatarUrl = ref<string>(DEFAULT_AVATAR)
const locale = ref('it-IT')
const submitting = ref(false)
const successMessage = ref('')

onMounted(() => {
  if (props.mode === 'edit' && auth.userProfile) {
    username.value = auth.userProfile.username ?? ''
    email.value = auth.userProfile.email ?? ''
    avatarUrl.value = auth.userProfile.avatarUrl ?? DEFAULT_AVATAR
    locale.value = auth.userProfile.locale ?? 'it-IT'
  }
})

async function onSubmit() {
  if (!username.value.trim()) return
  submitting.value = true
  successMessage.value = ''

  if (props.mode === 'register') {
    if (!email.value.trim()) return
    const ok = await auth.register({
      username: username.value.trim(),
      email: email.value.trim(),
      avatarUrl: avatarUrl.value,
      locale: locale.value,
    })
    submitting.value = false
    if (ok) {
      router.replace('/home')
    }
  } else {
    const ok = await auth.updateProfile({
      username: username.value.trim(),
      avatarUrl: avatarUrl.value,
      locale: locale.value,
    })
    submitting.value = false
    if (ok) {
      successMessage.value = 'Profilo aggiornato con successo!'
      setTimeout(() => (successMessage.value = ''), 3000)
    }
  }
}
</script>

<template>
  <form class="user-form" @submit.prevent="onSubmit">
    <h2 class="user-form__title">
      {{ mode === 'register' ? 'Completa il tuo profilo' : 'Modifica profilo' }}
    </h2>
    <p class="user-form__desc">
      {{
        mode === 'register'
          ? 'Scegli un nome, un avatar e inserisci la tua email per iniziare.'
          : 'Aggiorna le informazioni del tuo profilo.'
      }}
    </p>

    <!-- Avatar selection -->
    <div class="user-form__section">
      <span class="user-form__label-text">Avatar</span>
      <div class="user-form__avatars">
        <button
          v-for="av in AVATARS"
          :key="av"
          type="button"
          class="user-form__avatar-btn"
          :class="{ 'user-form__avatar-btn--active': avatarUrl === av }"
          @click="avatarUrl = av"
        >
          <img :src="av" alt="Avatar" class="user-form__avatar-img" />
        </button>
      </div>
    </div>

    <!-- Username -->
    <label class="user-form__label">
      Nome utente
      <input
        v-model="username"
        type="text"
        class="user-form__input"
        placeholder="Il tuo nome visibile"
        required
        minlength="3"
        maxlength="30"
        autocomplete="username"
      />
    </label>

    <!-- Email -->
    <label class="user-form__label">
      Email
      <input
        v-model="email"
        type="email"
        class="user-form__input"
        :class="{ 'user-form__input--readonly': mode === 'edit' }"
        placeholder="email@esempio.it"
        :required="mode === 'register'"
        :readonly="mode === 'edit'"
        :tabindex="mode === 'edit' ? -1 : undefined"
        autocomplete="email"
      />
    </label>

    <!-- Locale -->
    <label class="user-form__label">
      Lingua
      <select v-model="locale" class="user-form__input">
        <option value="it-IT">Italiano</option>
        <option value="en-US">English</option>
      </select>
    </label>

    <!-- Errors / Success -->
    <p v-if="auth.error" class="user-form__error">{{ auth.error }}</p>
    <p v-if="successMessage" class="user-form__success">{{ successMessage }}</p>

    <button type="submit" class="user-form__btn" :disabled="submitting">
      {{
        submitting
          ? mode === 'register'
            ? 'Registrazione...'
            : 'Salvataggio...'
          : mode === 'register'
            ? 'Registrati'
            : 'Salva modifiche'
      }}
    </button>
  </form>
</template>

<style scoped>
.user-form {
  display: flex;
  flex-direction: column;
  gap: var(--space-md);
  width: 100%;
  max-width: 420px;
}

.user-form__title {
  font-size: 1.8rem;
  font-weight: 700;
  color: var(--color-text);
  margin: 0;
}

.user-form__desc {
  color: var(--color-text-muted);
  font-size: 0.95rem;
  margin: 0;
}

.user-form__section {
  display: flex;
  flex-direction: column;
  gap: var(--space-xs);
}

.user-form__label-text {
  color: var(--color-text-muted);
  font-size: 0.875rem;
  font-weight: 500;
}

.user-form__avatars {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-sm);
}

.user-form__avatar-btn {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  border: 3px solid transparent;
  background: none;
  cursor: pointer;
  padding: 0;
  transition:
    border-color var(--transition-fast),
    transform var(--transition-fast);
}

.user-form__avatar-btn:hover {
  transform: scale(1.1);
}

.user-form__avatar-btn--active {
  border-color: var(--color-brand);
  box-shadow: 0 0 0 2px rgba(123, 47, 247, 0.3);
}

.user-form__avatar-img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
}

.user-form__label {
  display: flex;
  flex-direction: column;
  gap: var(--space-xs);
  color: var(--color-text-muted);
  font-size: 0.875rem;
  font-weight: 500;
}

.user-form__input {
  padding: var(--space-sm) var(--space-md);
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  color: var(--color-text);
  font-size: 1rem;
  outline: none;
  transition: border-color var(--transition-fast);
}

.user-form__input:focus {
  border-color: var(--color-brand);
}

.user-form__input--readonly {
  opacity: 0.6;
  cursor: not-allowed;
}

.user-form__input option {
  background: var(--color-surface);
  color: var(--color-text);
}

.user-form__error {
  color: #ef4444;
  font-size: 0.875rem;
  margin: 0;
}

.user-form__success {
  color: #22c55e;
  font-size: 0.875rem;
  margin: 0;
}

.user-form__btn {
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

.user-form__btn:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: var(--shadow-glow);
}

.user-form__btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>
