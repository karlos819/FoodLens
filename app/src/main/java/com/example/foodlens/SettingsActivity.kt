package com.example.foodlens

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        val btnAccount = findViewById<TextView>(R.id.btnAccountDummy)
        val rowUnitsValue = findViewById<TextView>(R.id.tvUnitsValue)
        val rowPrivacy = findViewById<TextView>(R.id.rowPrivacy)
        val rowOss = findViewById<TextView>(R.id.rowOpenSource)


        val swPush = findViewById<SwitchCompat>(R.id.swPush)
        val swDark = findViewById<SwitchCompat>(R.id.swDark)


        val btnLogout = findViewById<AppCompatButton>(R.id.btnLogout)

        btnAccount.setOnClickListener {
            Toast.makeText(this, "계정 관리", Toast.LENGTH_SHORT).show()
        }

        swPush.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, "알림: ${if (isChecked) "ON" else "OFF"}", Toast.LENGTH_SHORT).show()
        }

        swDark.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, "다크모드: ${if (isChecked) "ON" else "OFF"}", Toast.LENGTH_SHORT).show()
        }

        rowUnitsValue.setOnClickListener {
            Toast.makeText(this, "단위 변경", Toast.LENGTH_SHORT).show()
        }

        rowPrivacy.setOnClickListener {
            Toast.makeText(this, "개인정보 처리방침", Toast.LENGTH_SHORT).show()
        }

        rowOss.setOnClickListener {
            Toast.makeText(this, "오픈소스 라이선스", Toast.LENGTH_SHORT).show()
        }

        btnLogout.setOnClickListener {
            Toast.makeText(this, "로그아웃", Toast.LENGTH_SHORT).show()
        }
    }
}


