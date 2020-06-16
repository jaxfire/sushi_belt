package com.lush.curvedanimation

import android.animation.ObjectAnimator
import android.graphics.Path
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sushi_item.*
import kotlinx.android.synthetic.main.sushi_item.view.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val windowWidth = displayMetrics.widthPixels.toFloat()
        val windowHeight = displayMetrics.heightPixels.toFloat()

        val inflater = LayoutInflater.from(this)

        val sushiItems = mutableListOf<View>()
        val productData = listOf("Intergalactic", "Melusine", "Peachy", "Twilight", "Big Blue", "Goddess", "Kitsune", "Outback mate").forEach {
            val inflatedLayout = inflater.inflate(R.layout.sushi_item, sushi_container, false)
            inflatedLayout.productTextView.text = it
            sushi_container.addView(inflatedLayout)
            sushiItems.add(inflatedLayout)
        }

        sushi_container.post {

            val imageWidthHalf = sushiItem.width / 2
            val imageHeightHalf = sushiItem.height / 2

            val linePath1StartX = windowWidth / 4 - imageWidthHalf
            val linePath1EndY = windowHeight / 2

            val linePath2StartX = windowWidth / 4 * 3 - imageHeightHalf
            val linePath2StartY = windowHeight / 2

            val path = Path()

            path.setLastPoint(linePath1StartX, windowHeight)
            path.lineTo(linePath1StartX, linePath1EndY)

            path.arcTo(linePath1StartX, linePath1EndY - 250, linePath2StartX, linePath2StartY + 250, 180f, 180f, true)
            path.arcTo(500f, 500f, 500f, 500f, 180f, 0f, true)

            path.setLastPoint(linePath2StartX, linePath2StartY)
            path.lineTo(linePath2StartX, windowHeight)

            var animation: ObjectAnimator
            val animFraction = 1.0f / sushiItems.size
            sushiItems.forEachIndexed { index, view ->
                animation = ObjectAnimator.ofFloat(view, View.X, View.Y, path)
                animation.duration = 5_000
                animation.setCurrentFraction(animFraction * index)
                animation.interpolator = LinearInterpolator()
                animation.repeatCount = ObjectAnimator.INFINITE
                animation.start()
            }
        }
    }
}