<script setup lang="ts">
import { onMounted } from 'vue'
import { useCatalogStore } from '@/stores/catalog'
import HeroBanner from '@/components/HeroBanner.vue'
import FilmRow from '@/components/FilmRow.vue'
import LoadingSpinner from '@/components/LoadingSpinner.vue'

const catalog = useCatalogStore()

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

.home__rows {
  margin-top: var(--space-md);
}
</style>
