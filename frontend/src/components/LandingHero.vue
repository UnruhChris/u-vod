<script setup lang="ts">
/* Hero section for the landing page — U-VoD branding with login CTA */
</script>

<template>
  <section class="hero">
    <div class="hero__backdrop" />

    <div class="hero__content">
      <h1 class="hero__title"><span class="hero__title-u">U</span>-VoD</h1>
      <p class="hero__subtitle">Il tuo cinema universitario, sempre in streaming.</p>
      <a href="/.auth/login/github" class="hero__cta"> Accedi con GitHub </a>
    </div>
  </section>
</template>

<style scoped>
.hero {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  overflow: hidden;
}

.hero__backdrop {
  position: absolute;
  inset: 0;
  background: var(--gradient-hero);
  z-index: 0;
}

.hero__content {
  position: relative;
  z-index: 1;
  text-align: center;
  padding: var(--space-xl);
}

.hero__title {
  font-size: clamp(3rem, 10vw, 7rem);
  font-weight: 900;
  letter-spacing: -2px;
  color: var(--color-text);
  margin: 0 0 var(--space-md);
}

.hero__title-u {
  color: var(--color-brand);
}

.hero__subtitle {
  font-size: clamp(1rem, 2.5vw, 1.4rem);
  color: var(--color-text-muted);
  margin: 0 0 var(--space-xl);
  max-width: 480px;
  margin-inline: auto;
}

.hero__cta {
  display: inline-block;
  padding: var(--space-sm) var(--space-xl);
  background: var(--gradient-brand);
  color: var(--color-text);
  font-size: 1.1rem;
  font-weight: 600;
  text-decoration: none;
  border-radius: var(--radius-md);
  transition:
    transform var(--transition-fast),
    box-shadow var(--transition-fast);
  box-shadow: var(--shadow-glow);
}

.hero__cta:hover {
  transform: translateY(-2px);
  box-shadow: 0 0 30px rgba(123, 47, 247, 0.5);
}
</style>
