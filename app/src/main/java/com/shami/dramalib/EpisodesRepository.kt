package com.shami.dramalib

import android.content.Context
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object EpisodesRepository {

    // TODO: apna GitHub raw episodes.json link yahan daalein
    private const val REMOTE_EPISODES_URL = "https://raw.githubusercontent.com/YOUR_USERNAME/YOUR_REPO/main/episodes.json"

    fun loadEpisodes(context: Context): List<Episode> {
        val json = fetchRemote() ?: fetchLocalAsset(context)
        return parseEpisodes(json)
    }

    private fun fetchRemote(): String? {
        return try {
            val conn = URL(REMOTE_EPISODES_URL).openConnection() as HttpURLConnection
            conn.connectTimeout = 8000
            conn.readTimeout = 8000
            conn.requestMethod = "GET"
            if (conn.responseCode == 200) {
                val reader = BufferedReader(InputStreamReader(conn.inputStream))
                val text = reader.readText()
                reader.close()
                text
            } else null
        } catch (e: Exception) {
            null
        }
    }

    private fun fetchLocalAsset(context: Context): String {
        val input = context.assets.open("episodes.json")
        val reader = BufferedReader(InputStreamReader(input))
        val text = reader.readText()
        reader.close()
        return text
    }

    private fun parseEpisodes(json: String): List<Episode> {
        val root = JSONObject(json)
        val arr = root.getJSONArray("episodes")
        val list = mutableListOf<Episode>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            list.add(
                Episode(
                    id = o.getString("id"),
                    title = o.getString("title"),
                    videoUrl = o.getString("video_url"),
                    downloadUrl = o.getString("download_url"),
                    thumbnailUrl = o.optString("thumbnail_url", "")
                )
            )
        }
        return list
    }
}
