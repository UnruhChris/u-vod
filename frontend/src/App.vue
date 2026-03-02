<script setup lang="ts">
import { RouterView } from 'vue-router'
import TheNavbar from '@/components/TheNavbar.vue'
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()
</script>

<template>
  <TheNavbar v-if="auth.isAuthenticated && auth.isRegistered" />
  <RouterView />
</template>

<style scoped>
/* No specific styles: layout is managed by individual components */
</style>
