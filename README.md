# Drama Library

"Pyaar Kii Ye Ek Kahaani" (331 episodes) dekhne aur download karne ki Android app.

## Features
- 2-column grid list (Netflix jaisi), upar se neeche scroll
- Har episode ki thumbnail image khud archive.org se fetch hoti hai
- Click karne par: **Dekhein** (video app ke andar hi VideoView player mein khulta hai) ya **Download** (phone ke `Downloads/Dramas/` folder mein save hoti hai)
- Episode list `episodes.json` se aati hai — GitHub par edit karke bina naya APK banaye list update ho sakti hai

## APK banane ka tareeqa (Imran Series Library jaisa hi)

1. Naya GitHub repo banayein (ya Imran Series wali app ka structure follow karein)
2. Poora content upload karein
3. `app/src/main/java/com/shami/dramalib/EpisodesRepository.kt` mein `REMOTE_EPISODES_URL` ko apni repo ke `episodes.json` raw link se replace karein
4. Actions tab se "Build APK" workflow chalayein
5. Complete hone par Artifacts se APK download kar ke install karein

## Naya episode add karne ke liye
`episodes.json` mein naya entry add karein:
```json
{
  "id": "006",
  "title": "Episode 06",
  "video_url": "https://archive.org/download/IDENTIFIER/FILE_NAME.mp4",
  "download_url": "https://archive.org/download/IDENTIFIER/FILE_NAME.mp4",
  "thumbnail_url": "https://archive.org/services/img/IDENTIFIER"
}
```

**Zaroori:** `video_url` mein space ko `%20` se encode karna hai, warna video load nahi hogi.

Har archive.org item mein multiple episodes (files) bundled ho sakti hain — item ka "Download Options" section check kar ke har `.mp4` file ka link nikal lein.
