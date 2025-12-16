package com.example.foodlens

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.foodlens.ui.SparklineView
import com.google.android.material.progressindicator.CircularProgressIndicator

class AnalyzeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analyze)


        val sparkWeight = findViewById<SparklineView>(R.id.sparkWeight)

        val tvWeight = findViewById<TextView>(R.id.tvWeight)
        val tvWeightDelta = findViewById<TextView>(R.id.tvWeightDelta)
        val tvBodyFat = findViewById<TextView>(R.id.tvBodyFat)
        val tvBodyFatDelta = findViewById<TextView>(R.id.tvBodyFatDelta)
        val tvHeight = findViewById<TextView>(R.id.tvHeight)
        val tvBmi = findViewById<TextView>(R.id.tvBmi)

        val tvGoalPeriod = findViewById<TextView>(R.id.tvGoalPeriod)
        val tvGoalWeight = findViewById<TextView>(R.id.tvGoalWeight)
        val tvGoalBodyFat = findViewById<TextView>(R.id.tvGoalBodyFat)

        val cpiKcal = findViewById<CircularProgressIndicator>(R.id.cpiKcal)
        val tvTotalKcal = findViewById<TextView>(R.id.tvTotalKcal)
        val mealListContainer = findViewById<LinearLayout>(R.id.mealListContainer)


        val heightCm = 178
        val weight = 79.2
        val weightDelta = -0.8
        val bodyFat = 17
        val bodyFatDelta = -0.3
        val bmi = 20.7

        val goalWeeks = 4
        val goalWeight = 77.0
        val goalBodyFat = 18

        val totalKcal = 1980
        val goalKcal = 2000

        val weightTrend = listOf(60.8f, 60.2f, 60.0f, 59.8f, 59.7f, 59.4f, 59.2f)

        val meals = listOf(
            Meal("아침", 282, "샌드위치/우유"),
            Meal("점심", 882, "버거/감자튀김"),
            Meal("저녁", 546, "닭가슴살 샐러드"),
            Meal("간식", 270, "요거트/바나나")
        )

        // --- UI 바인딩 ---
        tvHeight.text = "키 ${heightCm}cm"
        tvBmi.text = "BMI $bmi"

        tvWeight.text = String.format("%.1f kg", weight)
        tvWeightDelta.text = "지난주 대비 ${formatSigned(weightDelta)}"

        tvBodyFat.text = "$bodyFat %"
        tvBodyFatDelta.text = "지난주 대비 ${formatSigned(bodyFatDelta)}"

        tvGoalPeriod.text = "목표 달성 기간: ${goalWeeks}주"
        tvGoalWeight.text = String.format("%.1f", goalWeight)
        tvGoalBodyFat.text = goalBodyFat.toString()

        // 스파크라인(그래프)
        sparkWeight.setData(weightTrend)

        // 칼로리 게이지
        cpiKcal.max = goalKcal
        cpiKcal.progress = totalKcal.coerceIn(0, goalKcal)
        tvTotalKcal.text = String.format("%,d", totalKcal)

        // 식사 리스트(간단 카드 형태로 생성)
        mealListContainer.removeAllViews()
        meals.forEach { meal ->
            mealListContainer.addView(createMealRow(meal))
        }
    }

    private fun createMealRow(meal: Meal): LinearLayout {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(dp(10), dp(10), dp(10), dp(10))
        }

        val left = TextView(this).apply {
            text = String.format("%d\nkcal", meal.kcal)
            textSize = 12f
            setTextColor(0xFF9AA0A6.toInt())
            width = dp(52)
        }

        val mid = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        val title = TextView(this).apply {
            text = meal.title
            textSize = 14f
            setTextColor(0xFFFFFFFF.toInt())
        }

        val desc = TextView(this).apply {
            text = meal.desc
            textSize = 12f
            setTextColor(0xFF9AA0A6.toInt())
        }

        mid.addView(title)
        mid.addView(desc)

        row.addView(left)
        row.addView(mid)

        return row
    }

    private fun dp(v: Int): Int = (v * resources.displayMetrics.density).toInt()

    private fun formatSigned(v: Double): String {
        return if (v >= 0) "+$v" else v.toString()
    }

    data class Meal(val title: String, val kcal: Int, val desc: String)
}
