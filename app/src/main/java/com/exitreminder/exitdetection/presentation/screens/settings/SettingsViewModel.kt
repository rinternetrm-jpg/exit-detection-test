package com.exitreminder.exitdetection.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exitreminder.exitdetection.service.OpenAIService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val openAIService: OpenAIService
) : ViewModel() {

    private val _apiKey = MutableStateFlow("")
    val apiKey: StateFlow<String> = _apiKey.asStateFlow()

    private val _hasApiKey = MutableStateFlow(false)
    val hasApiKey: StateFlow<Boolean> = _hasApiKey.asStateFlow()

    private val _isTesting = MutableStateFlow(false)
    val isTesting: StateFlow<Boolean> = _isTesting.asStateFlow()

    private val _testResult = MutableStateFlow<TestResult?>(null)
    val testResult: StateFlow<TestResult?> = _testResult.asStateFlow()

    init {
        loadApiKey()
    }

    private fun loadApiKey() {
        val savedKey = openAIService.getApiKey() ?: ""
        _apiKey.value = savedKey
        _hasApiKey.value = savedKey.isNotBlank()
    }

    fun saveApiKey(key: String) {
        openAIService.setApiKey(key)
        _apiKey.value = key
        _hasApiKey.value = key.isNotBlank()
        _testResult.value = TestResult(true, "API Key gespeichert!")
    }

    fun testApiKey(key: String) {
        viewModelScope.launch {
            _isTesting.value = true
            _testResult.value = null

            try {
                val client = OkHttpClient()

                // Simple test request to check if API key is valid
                val testBody = """
                    {
                        "model": "gpt-4o",
                        "messages": [{"role": "user", "content": "Hi"}],
                        "max_tokens": 5
                    }
                """.trimIndent()

                val request = Request.Builder()
                    .url("https://api.openai.com/v1/chat/completions")
                    .addHeader("Authorization", "Bearer $key")
                    .addHeader("Content-Type", "application/json")
                    .post(testBody.toRequestBody("application/json".toMediaType()))
                    .build()

                val response = client.newCall(request).execute()

                _testResult.value = if (response.isSuccessful) {
                    TestResult(true, "API Key funktioniert!")
                } else {
                    when (response.code) {
                        401 -> TestResult(false, "Ungültiger API Key")
                        429 -> TestResult(false, "Rate Limit erreicht")
                        else -> TestResult(false, "Fehler: ${response.code}")
                    }
                }

            } catch (e: Exception) {
                _testResult.value = TestResult(false, "Verbindungsfehler: ${e.message}")
            } finally {
                _isTesting.value = false
            }
        }
    }
}

data class TestResult(
    val success: Boolean,
    val message: String
)
