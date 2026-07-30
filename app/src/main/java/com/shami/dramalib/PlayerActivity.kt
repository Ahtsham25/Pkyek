package com.shami.dramalib

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class PlayerActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_TITLE = "extra_title"
        private const val EXTRA_URL = "extra_url"

        fun start(context: Context, title: String, url: String) {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra(EXTRA_TITLE, title)
            intent.putExtra(EXTRA_URL, url)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        supportActionBar?.hide()

        val title = intent.getStringExtra(EXTRA_TITLE) ?: ""
        val url = intent.getStringExtra(EXTRA_URL) ?: ""

        val videoView: VideoView = findViewById(R.id.video_view)
        val progressBar: ProgressBar = findViewById(R.id.progress_player)

        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        videoView.setOnPreparedListener {
            progressBar.visibility = ProgressBar.GONE
            videoView.start()
        }

        videoView.setOnErrorListener { _, _, _ ->
            progressBar.visibility = ProgressBar.GONE
            Toast.makeText(this, "$title chalne mein masla hua", Toast.LENGTH_LONG).show()
            true
        }

        try {
            videoView.setVideoURI(Uri.parse(url))
        } catch (e: Exception) {
            Toast.makeText(this, "Video URL theek nahi hai", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
