import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useCatalogStore } from '@/stores/catalog'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'landing',
      component: () => import('@/views/LandingView.vue'),
      meta: { public: true },
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/RegisterView.vue'),
    },
    {
      path: '/home',
      name: 'home',
      component: () => import('@/views/HomeView.vue'),
    },
    {
      path: '/profile',
      name: 'profile',
      component: () => import('@/views/ProfileView.vue'),
    },
    {
      path: '/title/:slug',
      name: 'film-detail',
      component: () => import('@/views/FilmDetailView.vue'),
      meta: { searchFlow: true },
    },
    {
      path: '/search',
      name: 'search',
      component: () => import('@/views/SearchView.vue'),
      meta: { searchFlow: true },
    },
  ],
})

router.beforeEach(async (to) => {
  const auth = useAuthStore()

  // Initialize the auth store if not done yet
  if (!auth.isAuthenticated && !auth.isLoading) {
    await auth.initialize()
  }

  // Public route (landing): if authenticated, redirect appropriately
  if (to.meta.public) {
    if (auth.isAuthenticated && auth.isRegistered) return '/home'
    if (auth.isAuthenticated && !auth.isRegistered) return '/register'
    return true
  }

  // Protected routes: if not authenticated, go back to landing
  if (!auth.isAuthenticated) return '/'

  // If authenticated but not registered, force registration
  if (!auth.isRegistered && to.name !== 'register') return '/register'

  // If already registered, prevent access to register page
  if (auth.isRegistered && to.name === 'register') return '/home'

  return true
})

router.afterEach((to, from) => {
  if (from.meta.searchFlow && !to.meta.searchFlow) {
    const catalog = useCatalogStore()
    catalog.clearSearch()
  }
})

export default router
