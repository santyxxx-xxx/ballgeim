package com.epn.gravitygame

import android.graphics.*
import kotlin.math.max
import kotlin.math.min

class Ball(
    var position: Vector2 = Vector2(500f, 800f),
    private val radius: Float = 15f
) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(56, 189, 248)
        setShadowLayer(25f, 0f, 0f, Color.rgb(59, 130, 246))
    }

    fun radius() = radius

    fun update(sensorX: Float, sensorY: Float, width: Int, height: Int) {
        position.x += sensorX * 8
        position.y += sensorY * 8

        position.x = max(radius, min(width - radius, position.x))
        position.y = max(radius, min(height - radius, position.y))
    }

    fun draw(canvas: Canvas) {
        canvas.drawCircle(position.x, position.y, radius, paint)
    }
}