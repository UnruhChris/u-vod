export interface Film {
  id: string
  title: string
  description: string
  releaseYear: number
  genre: string
  durationInMinutes: number
  thumbnailUrl: string
  blobName: string
  cast: string[]
  tags: string[]
  slug: string
}
