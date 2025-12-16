package com.example.foodlens

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class GeminiClient(private val apiKey: String) {

    private val client = OkHttpClient()
    private val jsonType = "application/json; charset=utf-8".toMediaType()


    private val modelId = "gemini-2.5-flash"

    suspend fun reply(history: List<ChatMessage>, userText: String): String = withContext(Dispatchers.IO) {
        val url =
            "https://generativelanguage.googleapis.com/v1/models/$modelId:generateContent?key=$apiKey"


        val contents = JSONArray().apply {
            history.forEach { m ->
                put(
                    JSONObject().apply {
                        put("role", if (m.isUser) "user" else "model")
                        put(
                            "parts",
                            JSONArray().put(JSONObject().put("text", m.text))
                        )
                    }
                )
            }
            put(
                JSONObject().apply {
                    put("role", "user")
                    put("parts", JSONArray().put(JSONObject().put("text", userText)))
                }
            )
        }

        val bodyJson = JSONObject().apply {
            put("contents", contents)
        }

        val req = Request.Builder()
            .url(url)
            .post(bodyJson.toString().toRequestBody(jsonType))
            .build()

        client.newCall(req).execute().use { resp ->
            val raw = resp.body?.string().orEmpty()
            if (!resp.isSuccessful) {

                return@withContext "오류가 발생했어요: HTTP ${resp.code}"
            }


            val root = JSONObject(raw)
            val candidates = root.optJSONArray("candidates")
            if (candidates == null || candidates.length() == 0) return@withContext "응답이 비어 있어요."

            val content = candidates.getJSONObject(0).optJSONObject("content") ?: return@withContext "응답 파싱 실패"
            val parts = content.optJSONArray("parts") ?: return@withContext "응답 파싱 실패"
            val text = parts.optJSONObject(0)?.optString("text").orEmpty()

            text.ifBlank { "응답을 생성하지 못했어요. 다시 시도해 주세요." }
        }
    }
}
