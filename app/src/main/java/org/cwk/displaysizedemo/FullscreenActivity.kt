package org.cwk.displaysizedemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_fullscreen.*

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : AppCompatActivity() {

    private var screenWidthPixels = 0

    private var screenHeightPixels = 0

    private var screenDensity = 2f

    private var minSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.attributes.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View
                .SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        setContentView(R.layout.activity_fullscreen)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View
                .SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        onInitScreen()

        onInitTouch()
    }

    @SuppressLint("SetTextI18n")
    private fun onInitScreen() {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(metrics)

        screenWidthPixels = metrics.widthPixels
        screenHeightPixels = metrics.heightPixels
        screenDensity = metrics.density

        minSize = (32 * screenDensity).toInt()

        screen_density.text = String.format("%.1f", screenDensity)
        screen_width.text = "${screenWidthPixels}px,${String.format("%.1f", screenWidthPixels / screenDensity)}dp"
        screen_height.text = "${screenHeightPixels}px,${String.format("%.1f", screenHeightPixels / screenDensity)}dp"

        window.decorView.viewTreeObserver.addOnGlobalLayoutListener {
            onSizeChange()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onSizeChange() {
        right_width.text = "${right_layout.width}px,${String.format("%.1f", right_layout.width /
                screenDensity)}dp"
        right_height.text = "${right_layout.height}px,${String.format("%.1f", right_layout.height /
                screenDensity)}dp"

        left_width.text = "${left_layout.width}px\n${String.format("%.1f", left_layout.width / screenDensity)}dp"
        left_height.text = "${left_layout.height}px\n${String.format("%.1f", left_layout.height / screenDensity)}dp"

        main_width.text = "${main_layout.width}px,${String.format("%.1f", main_layout.width / screenDensity)}dp"
        main_height.text = "${main_layout.height}px,${String.format("%.1f", main_layout.height / screenDensity)}dp"
    }

    private fun onInitTouch() {

        var mainX = 0f
        var mainWidth = 0
        var minWidth = 0

        main_layout.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    mainX = event.x
                    mainWidth = main_layout.width
                    minWidth = right_layout.width
                }
                MotionEvent.ACTION_MOVE -> {
                    val offset = event.x - mainX

                    if (minWidth - offset < minSize) {
                        right_layout.layoutParams.width = minSize
                    } else {
                        main_layout.layoutParams.width = mainWidth + offset.toInt()
                        right_layout.layoutParams.width = minWidth - offset.toInt()
                    }
                    main_layout.requestLayout()
                }
            }

            return@setOnTouchListener true
        }

        var leftX = 0f
        var leftWidth = 0

        left_layout.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    leftX = event.x
                    leftWidth = left_layout.width
                }
                MotionEvent.ACTION_MOVE -> {
                    val offset = event.x - leftX

                    if (leftWidth + offset < minSize) {
                        left_layout.layoutParams.width = minSize
                    } else {
                        left_layout.layoutParams.width = leftWidth + offset.toInt()
                    }
                    left_layout.requestLayout()
                }
            }

            return@setOnTouchListener true
        }
    }
}
