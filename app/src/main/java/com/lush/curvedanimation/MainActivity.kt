package com.lush.curvedanimation

import android.animation.ObjectAnimator
import android.graphics.Path
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
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

        val selectedProducts = mutableListOf<String>()
        val inflater = LayoutInflater.from(this)
        val sushiItems = mutableListOf<View>()
        val productData = listOf("Product A", "Product B", "Product C", "Product D", "Product E", "Product F", "Product G", "Product H")
                .forEach { product ->
                    val sushiItem = inflater.inflate(R.layout.sushi_item, sushi_container, false)
                    sushiItem.productTextView.text = product
                    sushiItem.setOnClickListener { view ->
                        if (view.selectedOverlay.visibility == GONE) {
                            view.selectedOverlay.visibility = VISIBLE
                            view.selectedCheckMark.visibility = VISIBLE
                            selectedProducts.add(product)
                        } else {
                            view.selectedOverlay.visibility = GONE
                            view.selectedCheckMark.visibility = GONE
                            selectedProducts.remove(product)
                        }
                    }
                    sushi_container.addView(sushiItem)
                    sushiItems.add(sushiItem)
                }
        sushi_container.post {
            val imageWidthHalf = sushiItem.width / 2
            val imageHeightHalf = sushiItem.height / 2
            val linePath1StartX = windowWidth / 4f - imageWidthHalf
            val linePath1EndY = windowHeight / 3
            val linePath2StartX = windowWidth / 4f * 3f - imageHeightHalf
            val linePath2StartY = windowHeight / 3
            val path = Path().apply {
                setLastPoint(linePath1StartX, windowHeight)
                lineTo(linePath1StartX, linePath1EndY)
                arcTo(linePath1StartX, linePath1EndY - 250, linePath2StartX, linePath2StartY + 250, 180f, 180f, true)
                lineTo(linePath2StartX, windowHeight)
            }
            var animation: ObjectAnimator
            val animStartFraction = 1.0f / sushiItems.size
            sushiItems.forEachIndexed { index, view ->
                animation = ObjectAnimator.ofFloat(view, X, Y, path)
                animation.duration = 8_000
                animation.setCurrentFraction(animStartFraction * index)
                animation.interpolator = LinearInterpolator()
                animation.repeatCount = ObjectAnimator.INFINITE
                animation.start()
            }
        }
    }
}