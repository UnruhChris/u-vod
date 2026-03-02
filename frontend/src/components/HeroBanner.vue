<script setup lang="ts">
import type { Film } from '@/types/Film'

defineProps<{ film: Film }>()
</script>

<template>
  <section class="hero-banner">
    <img :src="film.thumbnailUrl" :alt="film.title" class="hero-banner__bg" />
    <div class="hero-banner__overlay" />

    <div class="hero-banner__content">
      <h1 class="hero-banner__title">{{ film.title }}</h1>
      <p class="hero-banner__meta">
        {{ film.releaseYear }} &middot; {{ film.durationInMinutes }} min &middot; {{ film.genre }}
      </p>
      <p class="hero-banner__desc">{{ film.description }}</p>
      <RouterLink :to="`/title/${film.slug}`" class="hero-banner__btn"> Dettagli </RouterLink>
    </div>
  </section>
</template>

<style scoped>
.hero-banner {
  position: relative;
  height: 70vh;
  min-height: 400px;
  display: flex;
  align-items: flex-end;
  overflow: hidden;
}

.hero-banner__bg {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center top;
}

.hero-banner__overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(
    0deg,
    var(--color-bg) 0%,
    rgba(13, 13, 26, 0.6) 50%,
    rgba(13, 13, 26, 0.3) 100%
  );
}

.hero-banner__content {
  position: relative;
  z-index: 1;
  padding: var(--space-xl) var(--space-lg);
  max-width: 600px;
}

.hero-banner__title {
  font-size: clamp(2rem, 5vw, 3.5rem);
  font-weight: 800;
  color: var(--color-text);
  margin: 0 0 var(--space-xs);
}

.hero-banner__meta {
  color: var(--color-text-muted);
  font-size: 0.9rem;
  margin: 0 0 var(--space-sm);
}

.hero-banner__desc {
  color: var(--color-text-secondary);
  font-size: 1rem;
  line-height: 1.5;
  margin: 0 0 var(--space-md);
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.hero-banner__btn {
  display: inline-block;
  padding: var(--space-xs) var(--space-lg);
  background: var(--gradient-brand);
  color: var(--color-text);
  text-decoration: none;
  font-weight: 600;
  border-radius: var(--radius-sm);
  transition:
    transform var(--transition-fast),
    box-shadow var(--transition-fast);
}

.hero-banner__btn:hover {
  transform: translateY(-1px);
  box-shadow: var(--shadow-glow);
}
</style>
