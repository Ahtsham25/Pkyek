package com.shami.dramalib

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

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

    private var player: ExoPlayer? = null
    private lateinit var playerView: PlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        supportActionBar?.hide()

        val title = intent.getStringExtra(EXTRA_TITLE) ?: ""
        val url = intent.getStringExtra(EXTRA_URL) ?: ""

        playerView = findViewById(R.id.player_view)
        val progressBar: ProgressBar = findViewById(R.id.progress_player)

        try {
            val exoPlayer = ExoPlayer.Builder(this).build()
            playerView.player = exoPlayer
            playerView.controllerShowTimeoutMs = 3000
            playerView.setShowFastForwardButton(true)
            playerView.setShowRewindButton(true)
            playerView.setShowNextButton(false)
            playerView.setShowPreviousButton(false)

            exoPlayer.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    if (state == Player.STATE_READY) {
                        progressBar.visibility = ProgressBar.GONE
                    }
                }

                override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                    progressBar.visibility = ProgressBar.GONE
                    Toast.makeText(this@PlayerActivity, "$title chalne mein masla hua", Toast.LENGTH_LONG).show()
                }
            })

            val mediaItem = MediaItem.fromUri(Uri.parse(url))
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true

            player = exoPlayer
        } catch (e: Exception) {
            progressBar.visibility = ProgressBar.GONE
            Toast.makeText(this, "Player start nahi ho saka", Toast.LENGTH_LONG).show()
        }
    }

    override fun onStop() {
        super.onStop()
        player?.pause()
    }

    override fun onDestroy() {
        player?.release()
        player = null
        super.onDestroy()
    }
}
