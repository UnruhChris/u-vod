<script setup lang="ts">
import type { Film } from '@/types/Film'
import { RouterLink } from 'vue-router'

defineProps<{ film: Film }>()
</script>

<template>
  <RouterLink :to="`/title/${film.slug}`" class="film-card">
    <img :src="film.thumbnailUrl" :alt="film.title" class="film-card__img" loading="lazy" />
    <div class="film-card__info">
      <span class="film-card__title">{{ film.title }}</span>
      <span class="film-card__meta">{{ film.releaseYear }}</span>
    </div>
  </RouterLink>
</template>

<style scoped>
.film-card {
  flex-shrink: 0;
  width: 220px;
  border-radius: var(--radius-sm);
  overflow: hidden;
  text-decoration: none;
  transition:
    transform var(--transition-fast),
    box-shadow var(--transition-fast);
  background: var(--color-surface);
}

.film-card:hover {
  transform: scale(1.05);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.4);
  z-index: 2;
}

.film-card__img {
  width: 100%;
  aspect-ratio: 3 / 2;
  object-fit: cover;
  display: block;
}

.film-card__info {
  padding: var(--space-xs) var(--space-sm);
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.film-card__title {
  color: var(--color-text);
  font-size: 0.875rem;
  font-weight: 600;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.film-card__meta {
  color: var(--color-text-muted);
  font-size: 0.75rem;
}
</style>
