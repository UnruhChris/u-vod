<script setup lang="ts">
import { onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useCatalogStore } from '@/stores/catalog'
import { useAuthStore } from '@/stores/auth'
import LoadingSpinner from '@/components/LoadingSpinner.vue'

const route = useRoute()
const router = useRouter()
const catalog = useCatalogStore()
const auth = useAuthStore()

onMounted(() => {
  const slug = route.params.slug as string
  catalog.fetchFilmBySlug(slug)
})

onUnmounted(() => {
  catalog.clearCurrentFilm()
})

function goBack() {
  router.back()
}

function toggleFavorite() {
  const film = catalog.currentFilm
  if (!film) return

  if (auth.isFavorite(film.id)) {
    auth.removeFavorite(film.id)
  } else {
    auth.addFavorite({
      movieId: film.id,
      slug: film.slug,
      title: film.title,
      description: film.description,
      releaseYear: film.releaseYear,
      genre: film.genre,
      durationInMinutes: film.durationInMinutes,
      thumbnailUrl: film.thumbnailUrl,
      cast: film.cast,
      tags: film.tags,
    })
  }
}
</script>

<template>
  <div class="detail-overlay" @click.self="goBack">
    <div class="detail-card">
      <button class="detail-card__close" @click="goBack" aria-label="Chiudi">&times;</button>

      <LoadingSpinner v-if="catalog.isLoading" />

      <template v-else-if="catalog.currentFilm">
        <img
          :src="catalog.currentFilm.thumbnailUrl"
          :alt="catalog.currentFilm.title"
          class="detail-card__img"
        />

        <div class="detail-card__body">
          <h1 class="detail-card__title">{{ catalog.currentFilm.title }}</h1>

          <div class="detail-card__actions">
            <button
              class="detail-card__fav-btn"
              :class="{ 'detail-card__fav-btn--active': auth.isFavorite(catalog.currentFilm.id) }"
              @click="toggleFavorite"
            >
              <svg viewBox="0 0 24 24" class="detail-card__fav-icon">
                <path
                  d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"
                />
              </svg>
              {{
                auth.isFavorite(catalog.currentFilm.id) ? 'Nella mia lista' : 'Aggiungi alla lista'
              }}
            </button>
          </div>

          <p class="detail-card__meta">
            {{ catalog.currentFilm.releaseYear }} &middot;
            {{ catalog.currentFilm.durationInMinutes }} min &middot;
            {{ catalog.currentFilm.genre }}
          </p>

          <p class="detail-card__desc">{{ catalog.currentFilm.description }}</p>

          <div v-if="catalog.currentFilm.cast?.length" class="detail-card__section">
            <h3 class="detail-card__label">Cast</h3>
            <p class="detail-card__value">{{ catalog.currentFilm.cast.join(', ') }}</p>
          </div>

          <div v-if="catalog.currentFilm.tags?.length" class="detail-card__section">
            <h3 class="detail-card__label">Tag</h3>
            <div class="detail-card__tags">
              <span v-for="tag in catalog.currentFilm.tags" :key="tag" class="detail-card__tag">
                {{ tag }}
              </span>
            </div>
          </div>
        </div>
      </template>

      <p v-else class="detail-card__error">Film non trovato.</p>
    </div>
  </div>
</template>

<style scoped>
.detail-overlay {
  position: fixed;
  inset: 0;
  z-index: 200;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(0, 0, 0, 0.75);
  backdrop-filter: blur(4px);
  padding: var(--space-lg);
}

.detail-card {
  position: relative;
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  max-width: 720px;
  width: 100%;
  max-height: 90vh;
  overflow-y: auto;
}

.detail-card__close {
  position: absolute;
  top: var(--space-sm);
  right: var(--space-sm);
  z-index: 10;
  background: rgba(0, 0, 0, 0.6);
  color: var(--color-text);
  border: none;
  font-size: 1.5rem;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background var(--transition-fast);
}

.detail-card__close:hover {
  background: rgba(0, 0, 0, 0.85);
}

.detail-card__img {
  width: 100%;
  aspect-ratio: 16 / 9;
  object-fit: cover;
  border-radius: var(--radius-lg) var(--radius-lg) 0 0;
}

.detail-card__body {
  padding: var(--space-lg);
  display: flex;
  flex-direction: column;
  gap: var(--space-sm);
}

.detail-card__title {
  font-size: 1.8rem;
  font-weight: 800;
  color: var(--color-text);
  margin: 0;
}

.detail-card__actions {
  display: flex;
  gap: var(--space-sm);
}

.detail-card__fav-btn {
  display: inline-flex;
  align-items: center;
  gap: var(--space-xs);
  padding: var(--space-xs) var(--space-md);
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  color: var(--color-text-muted);
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.detail-card__fav-btn:hover {
  background: rgba(255, 255, 255, 0.15);
  color: var(--color-text);
}

.detail-card__fav-btn--active {
  color: #ef4444;
  border-color: #ef4444;
}

.detail-card__fav-icon {
  width: 18px;
  height: 18px;
  fill: currentColor;
}

.detail-card__meta {
  color: var(--color-text-muted);
  font-size: 0.9rem;
  margin: 0;
}

.detail-card__desc {
  color: var(--color-text-secondary);
  font-size: 1rem;
  line-height: 1.6;
  margin: 0;
}

.detail-card__section {
  margin-top: var(--space-xs);
}

.detail-card__label {
  font-size: 0.8rem;
  font-weight: 600;
  color: var(--color-text-muted);
  text-transform: uppercase;
  letter-spacing: 0.5px;
  margin: 0 0 var(--space-xs);
}

.detail-card__value {
  color: var(--color-text-secondary);
  font-size: 0.95rem;
  margin: 0;
}

.detail-card__tags {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-xs);
}

.detail-card__tag {
  padding: 2px var(--space-sm);
  background: rgba(123, 47, 247, 0.15);
  color: var(--color-brand);
  font-size: 0.8rem;
  border-radius: var(--radius-sm);
}

.detail-card__error {
  padding: var(--space-xl);
  text-align: center;
  color: var(--color-text-muted);
}
</style>
