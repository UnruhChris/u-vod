/**
 * Predefined avatar paths served from public/avatars/.
 *
 * Files in Vite's `public/` directory are copied as-is (no hash).
 * These paths are stored in Cosmos DB, so they MUST be plain static
 * strings that map 1-to-1 with the actual served files.
 */
export const AVATARS: string[] = [
  '/avatars/avatar-01.svg',
  '/avatars/avatar-02.svg',
  '/avatars/avatar-03.svg',
  '/avatars/avatar-04.svg',
  '/avatars/avatar-05.svg',
  '/avatars/avatar-06.svg',
  '/avatars/avatar-07.svg',
  '/avatars/avatar-08.svg',
]

export const DEFAULT_AVATAR: string = AVATARS[0]!
