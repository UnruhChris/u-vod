<template>
  <div class="spinner">
    <div class="spinner__ring" />
  </div>
</template>

<style scoped>
.spinner {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: var(--space-xl);
}

.spinner__ring {
  width: 40px;
  height: 40px;
  border: 3px solid var(--color-border);
  border-top-color: var(--color-brand);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}
</style>
