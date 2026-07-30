package com.shami.dramalib

import android.app.DownloadManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import java.io.File
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var emptyText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPreviousCrash()

        recyclerView = findViewById(R.id.recycler_episodes)
        progressBar = findViewById(R.id.progress_bar)
        swipeRefresh = findViewById(R.id.swipe_refresh)
        emptyText = findViewById(R.id.text_empty)

        // 2 columns, upar se neeche scroll
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        swipeRefresh.setOnRefreshListener { loadEpisodes() }
        loadEpisodes()
    }

    private fun checkPreviousCrash() {
        val file = File(filesDir, "crash.txt")
        if (file.exists()) {
            val content = file.readText()
            AlertDialog.Builder(this)
                .setTitle("Pichli crash ka error")
                .setMessage(content)
                .setPositiveButton("Copy") { _, _ ->
                    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("crash", content))
                    Toast.makeText(this, "Copy ho gaya", Toast.LENGTH_SHORT).show()
                    file.delete()
                }
                .setNegativeButton("Band karein") { _, _ -> file.delete() }
                .show()
        }
    }

    private fun loadEpisodes() {
        progressBar.visibility = ProgressBar.VISIBLE
        emptyText.visibility = TextView.GONE
        thread {
            val episodes = try {
                EpisodesRepository.loadEpisodes(this)
            } catch (e: Exception) {
                emptyList()
            }
            runOnUiThread {
                progressBar.visibility = ProgressBar.GONE
                swipeRefresh.isRefreshing = false
                if (episodes.isEmpty()) {
                    emptyText.visibility = TextView.VISIBLE
                    emptyText.text = getString(R.string.load_failed)
                } else {
                    recyclerView.adapter = EpisodeAdapter(episodes) { episode -> showEpisodeOptions(episode) }
                }
            }
        }
    }

    private fun showEpisodeOptions(episode: Episode) {
        AlertDialog.Builder(this)
            .setTitle(episode.title)
            .setItems(arrayOf(getString(R.string.watch), getString(R.string.download))) { _, which ->
                when (which) {
                    0 -> PlayerActivity.start(this, episode.title, episode.videoUrl)
                    1 -> downloadEpisode(episode)
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun downloadEpisode(episode: Episode) {
        try {
            val fileName = "${episode.title}.mp4"
            val request = DownloadManager.Request(Uri.parse(episode.downloadUrl))
                .setTitle(episode.title)
                .setDescription(getString(R.string.downloading))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "Dramas/$fileName"
                )
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

            val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)
            Toast.makeText(this, getString(R.string.download_started, fileName), Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Download shuru nahi ho saka", Toast.LENGTH_SHORT).show()
        }
    }
}
