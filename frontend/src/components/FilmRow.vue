<script setup lang="ts">
import type { Film } from '@/types/Film'
import FilmCard from '@/components/FilmCard.vue'

defineProps<{
  genre: string
  films: Film[]
}>()
</script>

<template>
  <section class="film-row">
    <h2 class="film-row__title">{{ genre }}</h2>
    <div class="film-row__scroll">
      <FilmCard v-for="film in films" :key="film.id" :film="film" />
    </div>
  </section>
</template>

<style scoped>
.film-row {
  padding: var(--space-md) 0;
}

.film-row__title {
  font-size: 1.3rem;
  font-weight: 700;
  color: var(--color-text);
  margin: 0 0 var(--space-sm);
  padding-left: var(--space-lg);
}

.film-row__scroll {
  display: flex;
  gap: var(--space-sm);
  overflow-x: auto;
  padding: var(--space-xs) var(--space-lg);
  scroll-snap-type: x proximity;
  -webkit-overflow-scrolling: touch;
}

.film-row__scroll::-webkit-scrollbar {
  height: 4px;
}

.film-row__scroll::-webkit-scrollbar-track {
  background: transparent;
}

.film-row__scroll::-webkit-scrollbar-thumb {
  background: var(--color-border);
  border-radius: 2px;
}
</style>
