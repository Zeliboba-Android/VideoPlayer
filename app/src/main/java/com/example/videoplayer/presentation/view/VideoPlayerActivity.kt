package com.example.videoplayer.presentation.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.videoplayer.R
import com.google.android.material.snackbar.Snackbar

class VideoPlayerActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)
        webView = findViewById(R.id.webView)

        val videoUrl = intent.getStringExtra("VIDEO_URL")
        if (videoUrl != null) {
            initializeWebView(videoUrl)
        } else {
            showError("Invalid video URL")
            finish()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initializeWebView(videoUrl: String) {
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            mediaPlaybackRequiresUserGesture = false
        }

        webView.webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java")
            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                showError("Error loading video: $description")
            }
        }

        webView.loadUrl(videoUrl)
    }

    private fun showError(message: String) {
        Snackbar.make(webView, message, Snackbar.LENGTH_LONG).show()
    }

    @Deprecated("This method has been deprecated in favor of using the\n      " +
            "{@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      " +
            "The OnBackPressedDispatcher controls how back button events are dispatched\n      " +
            "to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}