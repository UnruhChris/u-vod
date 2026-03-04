<script setup lang="ts">
import { onMounted } from 'vue'
import { useCatalogStore } from '@/stores/catalog'
import { useAuthStore } from '@/stores/auth'
import HeroBanner from '@/components/HeroBanner.vue'
import FilmRow from '@/components/FilmRow.vue'
import FilmCard from '@/components/FilmCard.vue'
import LoadingSpinner from '@/components/LoadingSpinner.vue'

const catalog = useCatalogStore()
const auth = useAuthStore()

onMounted(() => {
  if (catalog.films.length === 0) {
    catalog.fetchAllFilms()
  }
})
</script>

<template>
  <main class="home">
    <LoadingSpinner v-if="catalog.isLoading && catalog.films.length === 0" />

    <template v-else>
      <HeroBanner v-if="catalog.heroFilm" :film="catalog.heroFilm" />

      <!-- Favorites row -->
      <div v-if="auth.favorites.length > 0" class="home__favorites">
        <h2 class="home__row-title">La mia lista</h2>
        <div class="home__favorites-scroll">
          <FilmCard
            v-for="fav in auth.favorites"
            :key="fav.movieId"
            :film="{
              id: fav.movieId,
              slug: fav.slug,
              title: fav.title,
              description: fav.description,
              releaseYear: fav.releaseYear,
              genre: fav.genre,
              durationInMinutes: fav.durationInMinutes,
              thumbnailUrl: fav.thumbnailUrl,
              blobName: '',
              cast: fav.cast,
              tags: fav.tags,
            }"
          />
        </div>
      </div>

      <div class="home__rows">
        <FilmRow
          v-for="genre in catalog.allGenres"
          :key="genre"
          :genre="genre"
          :films="catalog.filmsByGenre[genre] ?? []"
        />
      </div>
    </template>
  </main>
</template>

<style scoped>
.home {
  padding-top: 0;
  padding-bottom: var(--space-xl);
}

.home__favorites {
  padding: var(--space-md) var(--space-lg) 0;
}

.home__row-title {
  font-size: 1.3rem;
  font-weight: 700;
  color: var(--color-text);
  margin: 0 0 var(--space-sm);
}

.home__favorites-scroll {
  display: flex;
  gap: var(--space-sm);
  overflow-x: auto;
  padding-bottom: var(--space-sm);
  scrollbar-width: thin;
  scrollbar-color: var(--color-border) transparent;
}

.home__rows {
  margin-top: var(--space-md);
}
</style>
