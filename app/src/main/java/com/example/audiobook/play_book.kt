package com.example.audiobook

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.squareup.picasso.Picasso

class play_book : AppCompatActivity() {

    private lateinit var playerView: PlayerView
    private lateinit var exoPlayer: SimpleExoPlayer
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_book)

        val button7: Button = findViewById(R.id.button7)
        playerView = findViewById(R.id.playerView)

        val intent = intent
        val image = intent.getStringExtra("IMAGE")
        val name = intent.getStringExtra("NAME")
        val file = intent.getStringExtra("FILE")
        Log.d("PlayBookActivity", "Image: $image, Name: $name, File: $file")
        Log.d("PlayBookActivity", "File path: $file")

        initializePlayer(Uri.parse(file))
        Picasso.get().load(image).into(findViewById<ImageView>(R.id.imageView))

        button7.setOnClickListener {
            finish()
        }
    }

    private fun initializePlayer(mediaUri: Uri) {
        try {
            val trackSelector = DefaultTrackSelector(this)
            val loadControl = DefaultLoadControl()

            exoPlayer = SimpleExoPlayer.Builder(this)
                .setTrackSelector(trackSelector)
                .setLoadControl(loadControl)
                .build()

            playerView.player = exoPlayer

            val dataSourceFactory = DefaultDataSourceFactory(this, "DetailAudio")
            val extractorsFactory = DefaultExtractorsFactory()

            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .setExtractorsFactory(extractorsFactory)
                .createMediaSource(mediaUri)

            exoPlayer.prepare(mediaSource)
            exoPlayer.playWhenReady = playWhenReady
            exoPlayer.seekTo(currentWindow, playbackPosition)
        } catch (e: Exception) {
            Log.e("PlayBookActivity", "Error initializing player: ${e.message}")
        }
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun releasePlayer() {
        try {
            playbackPosition = exoPlayer.currentPosition
            currentWindow = exoPlayer.currentWindowIndex
            playWhenReady = exoPlayer.playWhenReady
            exoPlayer.release()
        } catch (e: Exception) {
            Log.e("PlayBookActivity", "Error releasing player: ${e.message}")
        }
    }
}
