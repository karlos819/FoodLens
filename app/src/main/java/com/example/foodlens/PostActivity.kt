package com.example.foodlens

import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import com.example.foodlens.data.db.AppDatabase
import com.example.foodlens.data.db.FoodPostEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)


        val ivFood = findViewById<ImageView>(R.id.ivFood)
        val tvFoodName = findViewById<TextView>(R.id.tvFoodName)
        val tvCalories = findViewById<TextView>(R.id.tvCalories)
        val etMemo = findViewById<EditText>(R.id.etMemo)
        val btnUpload = findViewById<AppCompatButton>(R.id.btnUpload)


        val foodName = intent.getStringExtra("foodName") ?: "알 수 없음"
        val calories = intent.getIntExtra("calories", 0)
        val imageUriString = intent.getStringExtra("imageUri") // nullable


        tvFoodName.text = foodName
        tvCalories.text = "칼로리: ${calories} kcal"

        if (!imageUriString.isNullOrBlank()) {
            try {
                ivFood.setImageURI(Uri.parse(imageUriString))
            } catch (e: Exception) {
                // 이미지 로딩 실패해도 앱은 죽지 않게
            }
        }


        btnUpload.setOnClickListener {

            val memo = etMemo.text.toString().trim()

            if (memo.isEmpty()) {
                Toast.makeText(
                    this,
                    "메모를 입력해 주세요.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val postEntity = FoodPostEntity(
                imageUri = imageUriString,
                foodName = foodName,
                calories = calories,
                memo = memo
            )

            lifecycleScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        AppDatabase
                            .get(this@PostActivity)
                            .foodPostDao()
                            .insert(postEntity)
                    }

                    Toast.makeText(
                        this@PostActivity,
                        "게시글이 저장되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()

                } catch (e: Exception) {
                    Toast.makeText(
                        this@PostActivity,
                        "저장 중 오류가 발생했습니다.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
