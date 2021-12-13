package com.example.lecturer.view.ui

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.databasework.R
import com.example.databasework.databinding.FragmentVideoBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory


class VideoFragment : Fragment(R.layout.fragment_video), Player.Listener {
    private lateinit var binding: FragmentVideoBinding
    private lateinit var simpleExoPlayer: ExoPlayer
    private var playbackPosition: Long = 0
    private val mp4Url = "https://html5demos.com/assets/dizzy.mp4"
    private val dashUrl = "https://storage.googleapis.com/wvmedia/clear/vp9/tears/tears_uhd.mpd"
    private val urlList = listOf(mp4Url to "default", dashUrl to "dash")
    private val dataSourceFactory: DataSource.Factory by lazy {
        DefaultDataSourceFactory(requireContext(), "exoplayer-sample")
    }

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (!(activity as AppCompatActivity).supportActionBar!!.isShowing){
            (activity as AppCompatActivity).supportActionBar!!.show()


        }

        navController = this.findNavController()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_video, container, false)

        binding.videoToolbar.setNavigationOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.lectures)
        }

        // this is how to get the arguments
        if (requireArguments().isEmpty) {
            val args = VideoFragmentArgs.fromBundle(requireArguments())
        }

        return binding.root.rootView
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onResume() {
        super.onResume()
        simpleExoPlayer.seekTo(playbackPosition)
    }

    private fun initializePlayer() {
        simpleExoPlayer = ExoPlayer.Builder(requireContext()).build()
        val randomUrl = urlList.random()
        preparePlayer(randomUrl.first, randomUrl.second)
        binding.videoPlayer.player = simpleExoPlayer
        simpleExoPlayer.seekTo(playbackPosition)
        simpleExoPlayer.playWhenReady = true
        simpleExoPlayer.addListener(this)

    }

    private fun buildMediaSource(uri: Uri, type: String): MediaSource {
        return if (type == "dash") {
            DashMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uri))
        } else {
            ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uri))
        }
    }

    private fun preparePlayer(videoUrl: String, type: String) {
        val url = Uri.parse(videoUrl)
        val mediaSource = buildMediaSource(url, type)
        simpleExoPlayer.prepare(mediaSource)

    }

    private fun releasePlayer() {
        playbackPosition = simpleExoPlayer.currentPosition
        simpleExoPlayer.release()
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_BUFFERING) {
            binding.progressBar.visibility = View.VISIBLE
        } else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED)
            binding.progressBar.visibility = View.INVISIBLE
    }
}