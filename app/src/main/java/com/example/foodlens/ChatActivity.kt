package com.example.foodlens

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*

class ChatActivity : AppCompatActivity() {

    private lateinit var rvChat: RecyclerView
    private lateinit var etMessage: EditText
    private lateinit var btnSend: AppCompatButton

    private var lastSendAt = 0L


    private val messages = mutableListOf<ChatMessage>()
    private lateinit var adapter: ChatAdapter

    private lateinit var gemini: GeminiClient
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        rvChat = findViewById(R.id.rvChat)
        etMessage = findViewById(R.id.etMessage)
        btnSend = findViewById(R.id.btnSend)

        adapter = ChatAdapter(messages)
        rvChat.layoutManager = LinearLayoutManager(this).apply { stackFromEnd = true }
        rvChat.adapter = adapter

        android.util.Log.d("GEMINI", "API KEY length = " + BuildConfig.GEMINI_API_KEY.length)
        gemini = GeminiClient(BuildConfig.GEMINI_API_KEY)


        addMessage("안녕하세요! 음식/칼로리/식단 질문을 도와드릴게요.", isUser = false)

        btnSend.setOnClickListener {
            val userText = etMessage.text.toString().trim()
            if (userText.isBlank()) return@setOnClickListener

            val now = System.currentTimeMillis()
            if (now - lastSendAt < 1200) return@setOnClickListener // 1.2초 쿨다운
            lastSendAt = now


            etMessage.setText("")
            sendToGemini(userText)
        }
    }

    private fun sendToGemini(userText: String) {

        addMessage(userText, isUser = true)


        val placeholderIndex = messages.size
        messages.add(ChatMessage("응답 생성 중...", isUser = false))
        adapter.notifyItemInserted(placeholderIndex)
        rvChat.scrollToPosition(messages.size - 1)

        val history = messages
            .filterIndexed { idx, _ -> idx != placeholderIndex }
            .takeLast(12)



        btnSend.isEnabled = false

        scope.launch {
            val replyText = try {
                withContext(Dispatchers.IO) {
                    // placeholder는 history에서 제외
                    val history = messages.filterIndexed { idx, _ -> idx != placeholderIndex }
                    gemini.reply(history, userText)
                }
            } catch (e: Exception) {
                "오류가 발생했어요: ${e.message ?: "알 수 없음"}"
            }

            // placeholder를 실제 응답으로 교체
            messages[placeholderIndex] = ChatMessage(replyText, isUser = false)
            adapter.notifyItemChanged(placeholderIndex)
            rvChat.scrollToPosition(messages.size - 1)

            btnSend.isEnabled = true
        }
    }

    private fun addMessage(text: String, isUser: Boolean) {
        messages.add(ChatMessage(text, isUser))
        adapter.notifyItemInserted(messages.size - 1)
        rvChat.scrollToPosition(messages.size - 1)
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}

