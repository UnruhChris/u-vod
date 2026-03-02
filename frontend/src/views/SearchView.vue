<script setup lang="ts">
import { useCatalogStore } from '@/stores/catalog'
import SearchBar from '@/components/SearchBar.vue'
import FilmCard from '@/components/FilmCard.vue'
import LoadingSpinner from '@/components/LoadingSpinner.vue'

const catalog = useCatalogStore()

function onSearch(query: string) {
  catalog.searchQuery = query
  if (query.length >= 2) {
    catalog.searchFilms(query)
  } else {
    catalog.searchResults = []
  }
}
</script>

<template>
  <main class="search-page">
    <SearchBar :initial-query="catalog.searchQuery" @search="onSearch" />
    <LoadingSpinner v-if="catalog.isSearching" />
    <div v-else-if="catalog.searchResults.length" class="search-page__grid">
      <FilmCard v-for="film in catalog.searchResults" :key="film.id" :film="film" />
    </div>
    <p v-else class="search-page__empty">Search for a film by title...</p>
  </main>
</template>

<style scoped>
.search-page {
  padding-top: 80px; /* space for the fixed navbar */
  min-height: 100vh;
}

.search-page__grid {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-md);
  padding: var(--space-sm) var(--space-lg);
}

.search-page__empty {
  text-align: center;
  color: var(--color-text-muted);
  padding: var(--space-xl);
  font-size: 1rem;
}
</style>
