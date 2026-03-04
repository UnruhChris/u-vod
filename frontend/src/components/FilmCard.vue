<script setup lang="ts">
import type { Film } from '@/types/Film'
import { RouterLink } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const props = defineProps<{ film: Film }>()
const auth = useAuthStore()

function toggleFavorite(e: Event) {
  e.preventDefault()
  e.stopPropagation()

  if (auth.isFavorite(props.film.id)) {
    auth.removeFavorite(props.film.id)
  } else {
    auth.addFavorite({
      movieId: props.film.id,
      slug: props.film.slug,
      title: props.film.title,
      description: props.film.description,
      releaseYear: props.film.releaseYear,
      genre: props.film.genre,
      durationInMinutes: props.film.durationInMinutes,
      thumbnailUrl: props.film.thumbnailUrl,
      cast: props.film.cast,
      tags: props.film.tags,
    })
  }
}
</script>

<template>
  <RouterLink :to="`/title/${film.slug}`" class="film-card">
    <div class="film-card__img-wrapper">
      <img :src="film.thumbnailUrl" :alt="film.title" class="film-card__img" loading="lazy" />
      <button
        class="film-card__fav"
        :class="{ 'film-card__fav--active': auth.isFavorite(film.id) }"
        @click="toggleFavorite"
        :aria-label="auth.isFavorite(film.id) ? 'Rimuovi dai preferiti' : 'Aggiungi ai preferiti'"
      >
        <svg viewBox="0 0 24 24" class="film-card__fav-icon">
          <path
            d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"
          />
        </svg>
      </button>
    </div>
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

.film-card__img-wrapper {
  position: relative;
}

.film-card__img {
  width: 100%;
  aspect-ratio: 3 / 2;
  object-fit: cover;
  display: block;
}

.film-card__fav {
  position: absolute;
  top: var(--space-xs);
  right: var(--space-xs);
  width: 32px;
  height: 32px;
  border-radius: 50%;
  border: none;
  background: rgba(0, 0, 0, 0.55);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition:
    opacity var(--transition-fast),
    background var(--transition-fast);
}

.film-card:hover .film-card__fav,
.film-card__fav--active {
  opacity: 1;
}

.film-card__fav:hover {
  background: rgba(0, 0, 0, 0.8);
}

.film-card__fav-icon {
  width: 18px;
  height: 18px;
  fill: rgba(255, 255, 255, 0.7);
  transition: fill var(--transition-fast);
}

.film-card__fav--active .film-card__fav-icon {
  fill: #ef4444;
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
