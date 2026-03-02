<script setup lang="ts">
import RegisterForm from '@/components/RegisterForm.vue'
</script>

<template>
  <main class="register-page">
    <div class="register-page__card">
      <div class="register-page__brand"><span class="register-page__brand-u">U</span>-VoD</div>
      <RegisterForm />
    </div>
  </main>
</template>

<style scoped>
.register-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: var(--gradient-hero);
  padding: var(--space-lg);
}

.register-page__card {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-xl);
  padding: var(--space-xl) var(--space-lg);
  background: rgba(26, 26, 46, 0.85);
  backdrop-filter: blur(16px);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  width: 100%;
  max-width: 500px;
}

.register-page__brand {
  font-size: 2.2rem;
  font-weight: 900;
  letter-spacing: -1px;
  color: var(--color-text);
}

.register-page__brand-u {
  color: var(--color-brand);
}
</style>
