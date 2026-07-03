package com.epn.gravitygame

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import kotlin.math.*
import kotlin.random.Random

class GameView(context: Context) : View(context) {

    // ===== ESTADO =====
    private var started = false
    private var gameOver = false
    private var score = 0
    private var lives = 3
    private var level = 1

    private var sensorX = 0f
    private var sensorY = 0f

    // ===== OBJETOS =====
    private val ball = Ball()
    private val target = Target()
    private val obstacles = mutableListOf<Obstacle>()

    private var anim = 0f

    // ===== COLORES =====
    private val bgColor = Color.rgb(15, 23, 42)

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(226, 232, 240)
        textSize = 54f
        isFakeBoldText = true
    }

    private val smallPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(148, 163, 184)
        textSize = 36f
    }

    init {
        resetGame()
    }

    private fun resetGame() {
        score = 0
        lives = 3
        level = 1
        gameOver = false
        started = false

        ball.position.set(500f, 800f)
        target.relocate(1200, 2000)

        createObstacles()
    }

    private fun createObstacles() {
        obstacles.clear()

        val w = 1200f
        val h = 2000f

        obstacles.add(Obstacle(RectF(w * 0.2f, h * 0.3f, w * 0.8f, h * 0.33f)))
        obstacles.add(Obstacle(RectF(w * 0.1f, h * 0.6f, w * 0.7f, h * 0.63f)))
    }

    // ===== SENSOR =====
    fun updateSensor(x: Float, y: Float) {
        if (!started || gameOver) return
        sensorX = x
        sensorY = y
        updateGame()
        invalidate()
    }

    // ===== LOGICA =====
    private fun updateGame() {

        ball.update(sensorX * level, sensorY * level, width, height)

        anim += 6f

        val d = distance(ball.position, target.position)
        if (d < ball.radius() + target.radius()) {
            score += 10
            target.relocate(width, height)
        }

        for (o in obstacles) {
            if (o.rect.contains(ball.position.x, ball.position.y)) {
                lives--
                ball.position.set(width / 2f, height / 2f)
                if (lives <= 0) gameOver = true
            }
        }

        level = 1 + score / 50
    }

    // ===== DIBUJO =====
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawColor(bgColor)

        if (!started) {
            drawStartScreen(canvas)
            invalidate()
            return
        }

        drawHUD(canvas)
        target.draw(canvas)
        drawObstacles(canvas)
        ball.draw(canvas)

        if (gameOver) drawGameOver(canvas)

        invalidate()
    }

    // ===== PANTALLA INICIO =====
    private fun drawStartScreen(canvas: Canvas) {

        val title = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(56, 189, 248)
            textSize = 80f
            isFakeBoldText = true
        }

        canvas.drawText("GRAVITY GAME", width / 2f - 250f, height / 2f - 200f, title)

        canvas.drawText("Inclina el celular para mover la bola", width / 2f - 360f, height / 2f - 80f, smallPaint)

        canvas.drawText("Evita obstáculos rojos", width / 2f - 250f, height / 2f - 20f, smallPaint)

        canvas.drawText("Atrapa el objetivo morado", width / 2f - 280f, height / 2f + 40f, smallPaint)

        canvas.drawText("TOCA PARA EMPEZAR", width / 2f - 260f, height / 2f + 160f, title)
    }

    // ===== HUD =====
    private fun drawHUD(canvas: Canvas) {
        canvas.drawText("Score: $score", 40f, 80f, textPaint)
        canvas.drawText("Lives: $lives", 40f, 140f, smallPaint)
        canvas.drawText("Level: $level", 40f, 200f, smallPaint)

        canvas.drawText("X:${sensorX.toInt()} Y:${sensorY.toInt()}", 40f, 260f, smallPaint)

        canvas.drawText("Inclina el celular para mover la bola", 40f, 320f, smallPaint)
        canvas.drawText("Evita obstáculos y atrapa el objetivo", 40f, 380f, smallPaint)
    }

    private fun drawObstacles(canvas: Canvas) {
        val paint = Paint().apply {
            color = Color.rgb(220, 38, 38)
        }

        for (o in obstacles) {
            val r = RectF(o.rect)
            r.offset(sin(anim / 40) * 40f, 0f)
            canvas.drawRoundRect(r, 25f, 25f, paint)
        }
    }

    private fun drawGameOver(canvas: Canvas) {
        canvas.drawText("GAME OVER", width / 2f - 180f, height / 2f, textPaint)
        canvas.drawText("Score: $score", width / 2f - 120f, height / 2f + 80f, smallPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (gameOver) resetGame()
            started = true
        }
        return true
    }

    private fun distance(a: Vector2, b: Vector2): Float {
        return sqrt((a.x - b.x).pow(2) + (a.y - b.y).pow(2))
    }
}