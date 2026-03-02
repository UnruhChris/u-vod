<script setup lang="ts">
import { ref, watch } from 'vue'

const props = withDefaults(defineProps<{ initialQuery?: string }>(), {
  initialQuery: '',
})

const emit = defineEmits<{
  search: [query: string]
}>()

const query = ref(props.initialQuery)
let debounceTimer: ReturnType<typeof setTimeout> | null = null

watch(query, (val) => {
  if (debounceTimer) clearTimeout(debounceTimer)
  debounceTimer = setTimeout(() => {
    emit('search', val.trim())
  }, 300)
})
</script>

<template>
  <div class="search-bar">
    <input
      v-model="query"
      type="text"
      class="search-bar__input"
      placeholder="Cerca film per titolo..."
      autofocus
    />
  </div>
</template>

<style scoped>
.search-bar {
  padding: var(--space-md) var(--space-lg);
}

.search-bar__input {
  width: 100%;
  padding: var(--space-sm) var(--space-md);
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  color: var(--color-text);
  font-size: 1rem;
  outline: none;
  transition: border-color var(--transition-fast);
}

.search-bar__input:focus {
  border-color: var(--color-brand);
}

.search-bar__input::placeholder {
  color: var(--color-text-muted);
}
</style>
