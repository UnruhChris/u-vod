<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'
import { DEFAULT_AVATAR } from '@/constants/avatars'
import UserForm from '@/components/UserForm.vue'
import TheNavbar from '@/components/TheNavbar.vue'

const auth = useAuthStore()
</script>

<template>
  <TheNavbar />
  <main class="profile-page">
    <div class="profile-page__header">
      <img
        :src="auth.userProfile?.avatarUrl || DEFAULT_AVATAR"
        alt="Avatar"
        class="profile-page__avatar"
      />
      <h1 class="profile-page__name">{{ auth.userProfile?.username }}</h1>
      <p class="profile-page__email">{{ auth.userProfile?.email }}</p>
    </div>

    <div class="profile-page__card">
      <UserForm mode="edit" />
    </div>
  </main>
</template>

<style scoped>
.profile-page {
  min-height: 100vh;
  background: var(--gradient-hero);
  padding: calc(64px + var(--space-xl)) var(--space-lg) var(--space-xl);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-xl);
}

.profile-page__header {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-sm);
}

.profile-page__avatar {
  width: 96px;
  height: 96px;
  border-radius: 50%;
  border: 3px solid var(--color-brand);
  object-fit: cover;
}

.profile-page__name {
  font-size: 1.6rem;
  font-weight: 700;
  color: var(--color-text);
  margin: 0;
}

.profile-page__email {
  color: var(--color-text-muted);
  font-size: 0.9rem;
  margin: 0;
}

.profile-page__card {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: var(--space-xl) var(--space-lg);
  background: rgba(26, 26, 46, 0.85);
  backdrop-filter: blur(16px);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  width: 100%;
  max-width: 500px;
}
</style>
