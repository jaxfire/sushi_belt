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

        val selectedProducts = mutableListOf<ProductData>()
        val inflater = LayoutInflater.from(this)
        val sushiItems = mutableListOf<View>()

        val itemDiameter = (windowWidth / 3.75).toInt()

        populateDevProductData().forEach { product ->
                val sushiItem =
                    inflater.inflate(R.layout.sushi_item, sushi_container, false).apply {
                        layoutParams.width = itemDiameter
                        layoutParams.height = itemDiameter
                        requestLayout()
                        invalidate()
                        productTextView.text = product.name
                        productImageView.apply {
                            setImageResource(product.image)
                            layoutParams.width = (itemDiameter * 0.6).toInt()
                            layoutParams.height = (itemDiameter * 0.6).toInt()
                            requestLayout()
                            invalidate()
                        }
                        setOnClickListener { view ->
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
                arcTo(
                    linePath1StartX,
                    linePath1EndY - itemDiameter,
                    linePath2StartX,
                    linePath2StartY + itemDiameter,
                    180f,
                    180f,
                    true
                )
                lineTo(linePath2StartX, windowHeight)
            }

            val path2 = Path().apply {
                setLastPoint(linePath1StartX, windowHeight)
                lineTo(linePath1StartX, linePath1EndY)
                arcTo(
                    linePath1StartX,
                    linePath1EndY - itemDiameter,
                    linePath2StartX,
                    linePath2StartY + itemDiameter,
                    180f,
                    180f,
                    true
                )
                lineTo(linePath2StartX, windowHeight)
            }

            // Provides a modifier value that slightly tweaks the yOffset of the sushi
            // belt's background image to make it look good on various aspect ratio screens.
            // The value 0.1333 is the value that after manual trial and error looked the best
            // on all various devices tested on.
            val yOffsetModifier = windowHeight / windowWidth / 0.133f
            customView.setPath(path2, windowWidth / 7.5f, windowHeight / yOffsetModifier, itemDiameter)
            customView.invalidate()

            var animation: ObjectAnimator
            val animStartFraction = 1.0f / sushiItems.size
            sushiItems.forEachIndexed { index, view ->
                animation = ObjectAnimator.ofFloat(view, X, Y, path)
                animation.duration = 15_000
                animation.setCurrentFraction(animStartFraction * index)
                animation.interpolator = LinearInterpolator()
                animation.repeatCount = ObjectAnimator.INFINITE
                animation.start()
            }
        }
    }

    private fun populateDevProductData(): List<ProductData> = mutableListOf(
        ProductData("Daddy-o", R.drawable.daddy_o),
        ProductData("Intergalactic", R.drawable.intergalactic),
        ProductData("Outback mate", R.drawable.outback_mate),
        ProductData("R&B", R.drawable.r_n_b),
        ProductData("Rehab", R.drawable.rehab),
        ProductData("Rouge Henna", R.drawable.rouge_henna),
        ProductData("The Comforter", R.drawable.the_comforter),
        ProductData("Twilight", R.drawable.twilight_body_spray)
    )
}