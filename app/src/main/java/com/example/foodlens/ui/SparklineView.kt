package com.example.foodlens.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import kotlin.math.max

class SparklineView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 6f
        style = Paint.Style.STROKE
        color = 0xFF43A047.toInt()
    }

    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 2f
        style = Paint.Style.STROKE
        color = 0x22FFFFFF
    }

    private var values: List<Float> = emptyList()

    fun setData(newValues: List<Float>) {
        values = newValues
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (values.size < 2) return

        val h = height.toFloat()
        val w = width.toFloat()

        canvas.drawLine(0f, h * 0.33f, w, h * 0.33f, gridPaint)
        canvas.drawLine(0f, h * 0.66f, w, h * 0.66f, gridPaint)

        val minV = values.minOrNull() ?: 0f
        val maxV = values.maxOrNull() ?: 1f
        val range = max(0.0001f, maxV - minV)

        val padding = 8f
        val usableW = w - padding * 2
        val usableH = h - padding * 2

        val path = Path()
        values.forEachIndexed { i, v ->
            val x = padding + (usableW * i / (values.size - 1))
            val normalized = (v - minV) / range
            val y = padding + (usableH * (1f - normalized))

            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        canvas.drawPath(path, linePaint)
    }
}
