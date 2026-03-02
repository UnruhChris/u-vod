<script setup lang="ts">
import { RouterLink } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()
</script>

<template>
  <nav class="navbar">
    <div class="navbar__left">
      <RouterLink to="/home" class="navbar__logo">
        <span class="navbar__logo-u">U</span>-VoD
      </RouterLink>

      <RouterLink to="/home" class="navbar__link">Home</RouterLink>
      <RouterLink to="/search" class="navbar__link">Cerca</RouterLink>
    </div>

    <div class="navbar__right">
      <span class="navbar__user">{{ auth.userName }}</span>
      <a href="/.auth/logout" class="navbar__logout" @click.prevent="auth.logout()">Esci</a>
    </div>
  </nav>
</template>

<style scoped>
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 var(--space-lg);
  height: 64px;
  background: linear-gradient(180deg, rgba(13, 13, 26, 0.95) 0%, transparent 100%);
  backdrop-filter: blur(8px);
  transition: background var(--transition-normal);
}

.navbar__left,
.navbar__right {
  display: flex;
  align-items: center;
  gap: var(--space-md);
}

.navbar__logo {
  font-size: 1.6rem;
  font-weight: 800;
  letter-spacing: -0.5px;
  text-decoration: none;
  color: var(--color-text);
  margin-right: var(--space-md);
}

.navbar__logo-u {
  color: var(--color-brand);
}

.navbar__link {
  color: var(--color-text-muted);
  text-decoration: none;
  font-size: 0.95rem;
  font-weight: 500;
  transition: color var(--transition-fast);
}

.navbar__link:hover,
.navbar__link.router-link-active {
  color: var(--color-text);
}

.navbar__user {
  color: var(--color-text-muted);
  font-size: 0.875rem;
}

.navbar__logout {
  color: var(--color-text-muted);
  text-decoration: none;
  font-size: 0.875rem;
  padding: var(--space-xs) var(--space-sm);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  transition: all var(--transition-fast);
  cursor: pointer;
}

.navbar__logout:hover {
  color: var(--color-text);
  border-color: var(--color-text-muted);
}
</style>
