import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { Film } from '@/types/Film'

export const useCatalogStore = defineStore('catalog', () => {
  // --- State ---
  const films = ref<Film[]>([])
  const currentFilm = ref<Film | null>(null)
  const isLoading = ref(false)
  const error = ref<string | null>(null)

  // --- Getters ---

  /** Films grouped by genre: { "Sci-Fi": [...], "Thriller": [...], ... } */
  const filmsByGenre = computed(() => {
    const grouped: Record<string, Film[]> = {}
    for (const film of films.value) {
      if (!grouped[film.genre]) {
        grouped[film.genre] = []
      }
      grouped[film.genre]!.push(film)
    }
    return grouped
  })

  /** List of unique genres */
  const allGenres = computed(() => Object.keys(filmsByGenre.value))

  /** Random film for the hero banner */
  const heroFilm = computed(() => {
    if (films.value.length === 0) return null
    const index = Math.floor(Math.random() * films.value.length)
    return films.value[index]
  })

  // --- Actions ---

  async function fetchAllFilms() {
    isLoading.value = true
    error.value = null
    try {
      const response = await fetch('/api/catalog', { credentials: 'include' })
      if (!response.ok) throw new Error(`Error ${response.status}: ${response.statusText}`)
      films.value = await response.json()
    } catch (e) {
      error.value = e instanceof Error ? e.message : String(e)
    } finally {
      isLoading.value = false
    }
  }

  async function fetchFilmBySlug(slug: string) {
    isLoading.value = true
    error.value = null
    try {
      const response = await fetch(`/api/catalog/slug/${encodeURIComponent(slug)}`, {
        credentials: 'include',
      })
      if (!response.ok) throw new Error(`Error ${response.status}: ${response.statusText}`)
      currentFilm.value = await response.json()
    } catch (e) {
      error.value = e instanceof Error ? e.message : String(e)
    } finally {
      isLoading.value = false
    }
  }

  async function fetchFilmsByGenre(genre: string) {
    isLoading.value = true
    error.value = null
    try {
      const response = await fetch(`/api/catalog/genre/${encodeURIComponent(genre)}`, {
        credentials: 'include',
      })
      if (!response.ok) throw new Error(`Error ${response.status}: ${response.statusText}`)
      return (await response.json()) as Film[]
    } catch (e) {
      error.value = e instanceof Error ? e.message : String(e)
      return []
    } finally {
      isLoading.value = false
    }
  }

  // --- Search state ---
  const searchQuery = ref('')
  const searchResults = ref<Film[]>([])
  const isSearching = ref(false)

  async function searchFilms(query: string) {
    isSearching.value = true
    error.value = null
    try {
      const response = await fetch(`/api/catalog/search?q=${encodeURIComponent(query)}`, {
        credentials: 'include',
      })
      if (!response.ok) throw new Error(`Error ${response.status}: ${response.statusText}`)
      searchResults.value = await response.json()
    } catch (e) {
      error.value = e instanceof Error ? e.message : String(e)
    } finally {
      isSearching.value = false
    }
  }

  function clearSearch() {
    searchQuery.value = ''
    searchResults.value = []
  }

  function clearCurrentFilm() {
    currentFilm.value = null
  }

  return {
    films,
    searchQuery,
    searchResults,
    currentFilm,
    isLoading,
    isSearching,
    error,
    filmsByGenre,
    allGenres,
    heroFilm,
    fetchAllFilms,
    fetchFilmBySlug,
    fetchFilmsByGenre,
    searchFilms,
    clearSearch,
    clearCurrentFilm,
  }
})
