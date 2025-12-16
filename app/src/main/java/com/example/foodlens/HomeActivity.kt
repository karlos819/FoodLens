package com.example.foodlens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodlens.data.db.AppDatabase
import com.example.foodlens.data.db.FoodRecordEntity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.util.Locale

class HomeActivity : AppCompatActivity() {

    private lateinit var cameraLauncher: ActivityResultLauncher<Void?>
    private lateinit var requestCameraPermission: ActivityResultLauncher<String>

    private lateinit var classifier: FoodClassifier


    private lateinit var recordAdapter: RecordAdapter
    private val recentList = mutableListOf<FoodRecordUi>()


    private var tvCalories: TextView? = null
    private var tvProteinLeft: TextView? = null
    private var tvCarbsLeft: TextView? = null
    private var tvFatsLeft: TextView? = null


    private val goalProtein = 150
    private val goalCarbs = 350
    private val goalFats = 90


    private val dao by lazy { AppDatabase.get(this).foodDao() }
    private val today = "today"


    private val kcalPrefs by lazy { getSharedPreferences("food_kcal_prefs", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomBar)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_post -> { startActivity(Intent(this, PostActivity::class.java)); true }
                R.id.nav_analyze -> { startActivity(Intent(this, AnalyzeActivity::class.java)); true }
                R.id.nav_chat -> { startActivity(Intent(this, ChatActivity::class.java)); true }
                R.id.nav_settings -> { startActivity(Intent(this, SettingsActivity::class.java)); true }
                else -> false
            }
        }


        tvCalories = findViewByIdOrNull(R.id.tvCalories)
        tvProteinLeft = findViewByIdOrNull(R.id.tvProteinLeft)
        tvCarbsLeft = findViewByIdOrNull(R.id.tvCarbsLeft)
        tvFatsLeft = findViewByIdOrNull(R.id.tvFatsLeft)


        try {
            classifier = FoodClassifier(this)
        } catch (e: Exception) {
            Toast.makeText(this, "AI 모델을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            Log.e("FoodLens", "Error initializing classifier", e)
        }


        val rv = findViewById<RecyclerView>(R.id.rvRecentFoods)
        rv.layoutManager = LinearLayoutManager(this)
        recordAdapter = RecordAdapter()
        rv.adapter = recordAdapter


        loadRecordsFromDb()


        val fabCamera = findViewById<FloatingActionButton>(R.id.fabCamera)

        cameraLauncher = registerForActivityResult(
            ActivityResultContracts.TakePicturePreview()
        ) { bitmap: Bitmap? ->
            if (bitmap != null) classifyImage(bitmap)
            else Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
        }

        requestCameraPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) openCamera()
                else Toast.makeText(this, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }

        fabCamera.setOnClickListener { checkCameraAndOpen() }
    }

    private fun checkCameraAndOpen() {
        val granted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (granted) openCamera()
        else requestCameraPermission.launch(Manifest.permission.CAMERA)
    }

    private fun openCamera() {
        cameraLauncher.launch(null)
    }

    private fun classifyImage(bitmap: Bitmap) {
        if (!::classifier.isInitialized) return

        val (foodName, accuracy) = classifier.classify(bitmap)

        val message = "결과: $foodName (정확도: ${String.format(Locale.US, "%.2f", accuracy)})"
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        Log.d("FoodLens", "AI 분석 결과: $message")


        val macro = guessMacros(foodName)


        val fixedRandomKcal = getOrCreateFixedRandomKcal(foodName)


        lifecycleScope.launch {
            dao.insert(
                FoodRecordEntity(
                    date = today,
                    name = foodName,
                    kcal = fixedRandomKcal,  // ✅ 핵심
                    carb = macro.carb,
                    protein = macro.protein,
                    fat = macro.fat
                )
            )
            loadRecordsFromDb()
        }
    }

    private fun loadRecordsFromDb() {
        lifecycleScope.launch {
            val rows = dao.getByDate(today)

            recentList.clear()
            recentList.addAll(
                rows.map {
                    FoodRecordUi(
                        name = it.name,
                        kcal = it.kcal,
                        carb = it.carb,
                        protein = it.protein,
                        fat = it.fat
                    )
                }
            )

            recordAdapter.submit(recentList)
            updateSummaryUi()
        }
    }

    private fun updateSummaryUi() {
        val totalKcal = recentList.sumOf { it.kcal }
        val totalCarb = recentList.sumOf { it.carb }
        val totalProtein = recentList.sumOf { it.protein }
        val totalFat = recentList.sumOf { it.fat }

        tvCalories?.text = totalKcal.toString()

        val proteinLeft = (goalProtein - totalProtein).coerceAtLeast(0)
        val carbsLeft = (goalCarbs - totalCarb).coerceAtLeast(0)
        val fatsLeft = (goalFats - totalFat).coerceAtLeast(0)

        tvProteinLeft?.text = "${proteinLeft}g"
        tvCarbsLeft?.text = "${carbsLeft}g"
        tvFatsLeft?.text = "${fatsLeft}g"
    }


    private data class MacroGuess(val kcal: Int, val carb: Int, val protein: Int, val fat: Int)

    private fun guessMacros(foodName: String): MacroGuess {
        val n = foodName.lowercase().trim()

        return when {
            n.contains("사과") || n.contains("apple") -> MacroGuess(150, 25, 0, 0)
            n.contains("샐러드") || n.contains("salad") -> MacroGuess(320, 15, 35, 10)
            n.contains("라면") || n.contains("ramen") -> MacroGuess(520, 80, 10, 18)
            n.contains("피자") || n.contains("pizza") -> MacroGuess(700, 75, 25, 30)
            n.contains("버거") || n.contains("burger") -> MacroGuess(650, 55, 25, 30)
            n.contains("치킨") || n.contains("chicken") -> MacroGuess(800, 20, 55, 45)
            n.contains("밥") || n.contains("rice") -> MacroGuess(500, 100, 8, 2)
            else -> MacroGuess(400, 50, 15, 15)
        }
    }


    private fun getOrCreateFixedRandomKcal(foodName: String): Int {
        // key는 “공백/대소문자 차이”로 매번 달라지지 않게 정규화
        val key = foodName.lowercase().trim()

        val saved = kcalPrefs.getInt(key, -1)
        if (saved != -1) return saved

        val newValue = (95..285).random()
        kcalPrefs.edit().putInt(key, newValue).apply()
        return newValue
    }

    private fun <T> AppCompatActivity.findViewByIdOrNull(id: Int): T? {
        return try { findViewById(id) } catch (_: Exception) { null }
    }

    override fun onDestroy() {
        if (::classifier.isInitialized) classifier.close()
        super.onDestroy()
    }
}
