package com.oajstudios.pocketshop.activity

import android.app.Activity
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.oajstudios.pocketshop.AppBaseActivity
import com.oajstudios.pocketshop.R
import com.oajstudios.pocketshop.utils.BroadcastReceiverExt
import com.oajstudios.pocketshop.utils.Constants
import com.oajstudios.pocketshop.utils.extensions.fetchAndStoreCartData
import kotlinx.android.synthetic.main.activity_webview_payment.*
import kotlinx.android.synthetic.main.toolbar.*

class WebViewActivity : AppBaseActivity() {
    var mIsError = false
    private var mWebViewClient = WebViewClient()
    private var webChromeClient = WebChromeClient()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview_payment)
        setToolbar(toolbar)
        mAppBarColor()
        setupWebView()
        title = getString(R.string.lbl_payment)

        webView.loadUrl(
            intent.getStringExtra(Constants.KeyIntent.CHECKOUT_URL)!!
        )
        BroadcastReceiverExt(this@WebViewActivity) {
            onAction(Constants.AppBroadcasts.CART_COUNT_CHANGE) {
            }
        }
    }

    private fun setupWebView() {
        showProgress(true)
        configureWebView()
        configureWebClient()
        initChromeClient()
    }

    private fun initChromeClient() {
        var customView: View? = null
        var customViewCallback: WebChromeClient.CustomViewCallback? = null
        var originalOrientation = 0
        var originalSystemUiVisibility = 0

        webChromeClient = object : WebChromeClient() {

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                toolbar.title = title
            }

            override fun onShowCustomView(
                paramView: View?,
                paramCustomViewCallback: CustomViewCallback,
            ) {
                if (customView != null) {
                    onHideCustomView()
                    return
                }
                customView = paramView
                originalSystemUiVisibility = window.decorView.systemUiVisibility
                originalOrientation = requestedOrientation
                customViewCallback = paramCustomViewCallback
                (window.decorView as FrameLayout).addView(
                    customView,
                    FrameLayout.LayoutParams(-1, -1)
                )
                window.decorView.systemUiVisibility = 3846
            }

            override fun onHideCustomView() {
                (window.decorView as FrameLayout).removeView(customView)
                customView = null
                window.decorView.systemUiVisibility = originalSystemUiVisibility
                requestedOrientation = originalOrientation
                customViewCallback?.onCustomViewHidden()
                customViewCallback = null
            }
        }

        webView.webChromeClient = webChromeClient
    }

    private fun configureWebClient() {
        mWebViewClient = object : WebViewClient() {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                showProgress(true)
            }

            override fun onPageFinished(view: WebView, url: String) {
                Log.e("Error", "onPageFinished")
                super.onPageFinished(view, url)
                if (!mIsError) {
                    if (url.contains("checkout/order-received")) {
                        showProgress(false)
                        setResult(Activity.RESULT_OK)
                        Toast.makeText(
                            this@WebViewActivity,
                            "Order placed successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        fetchAndStoreCartData()
                        finish()

                    } else {
                        showProgress(false)
                    }
                }
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError,
            ) {
                mIsError = true
                super.onReceivedError(view, request, error)
            }

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?,
            ) {
                Log.e("Error", "onReceivedHttpError")
                super.onReceivedHttpError(view, request, errorResponse)
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?,
            ) {
                Log.e("Error", "onReceivedSslError")
                super.onReceivedSslError(view, handler, error)
                Toast.makeText(this@WebViewActivity, "onReceivedSslError", Toast.LENGTH_LONG)
                    .show()
                finish()
            }

        }
        // Assign Web View Client to webview
        webView.webViewClient = mWebViewClient

    }

    private fun configureWebView() {
        try {
            webView.settings.apply {
                javaScriptCanOpenWindowsAutomatically = true
                javaScriptEnabled = true
                useWideViewPort = true
                loadWithOverviewMode = true
                databaseEnabled = true
                domStorageEnabled = true
                allowFileAccess = true
                allowContentAccess = true
                webView.isFocusable = true
                webView.isFocusableInTouchMode = true
                webView.isScrollContainer = false
                webView.isVerticalScrollBarEnabled = false
                webView.isHorizontalScrollBarEnabled = false

                setGeolocationEnabled(true)
                cacheMode = WebSettings.LOAD_NO_CACHE
                layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
                loadsImagesAutomatically = true
                loadWithOverviewMode = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                }
                setSupportMultipleWindows(false)
                builtInZoomControls = false
                setSupportZoom(
                    false
                )
                userAgentString =
                    "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.0.0 Mobile Safari/537.36"
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        webView.clearHistory()
        webView.clearCache(true)
    }

}
