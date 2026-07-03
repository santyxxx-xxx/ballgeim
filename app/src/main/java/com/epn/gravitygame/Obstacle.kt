package com.epn.gravitygame

import android.graphics.*

class Obstacle(val rect: RectF) {

    fun draw(canvas: Canvas) {
        val paint = Paint().apply {
            color = Color.rgb(220, 38, 38)
        }
        canvas.drawRoundRect(rect, 25f, 25f, paint)
    }
}