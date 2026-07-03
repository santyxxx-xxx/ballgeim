package com.epn.gravitygame

import android.graphics.*
import kotlin.random.Random

class Target(
    var position: Vector2 = Vector2(800f, 800f),
    private val radius: Float = 40f
) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(167, 139, 250)
    }

    fun radius() = radius

    fun relocate(width: Int, height: Int) {
        val margin = 100
        position.x = Random.nextInt(margin, width - margin).toFloat()
        position.y = Random.nextInt(margin, height - margin).toFloat()
    }

    fun draw(canvas: Canvas) {
        canvas.drawCircle(position.x, position.y, radius, paint)
    }
}