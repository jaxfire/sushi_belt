package com.lush.curvedanimation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class CustomView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var path: Path? = null
    private var paint: Paint = Paint()
    private var itemDiameter = 100

    fun setPath(path: Path, xOffset: Float, yOffset: Float, itemDiameter: Int) {
        this.path = path
        path.offset(xOffset, yOffset)
        this.itemDiameter = (itemDiameter * 1.25).toInt()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        paint.style = Paint.Style.FILL;
        paint.color = Color.TRANSPARENT;
        canvas!!.drawPaint(paint);

        if (path != null) {
            paint.strokeWidth = itemDiameter.toFloat()
            paint.pathEffect = null
            paint.color = resources.getColor(R.color.sushiBeltBackground)
            paint.style = Paint.Style.STROKE
            canvas!!.drawPath(path!!, paint)
        }
    }
}