package com.exitreminder.exitdetection.service

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.exitreminder.exitdetection.domain.model.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * OpenAI GPT-4 Vision API Service for map analysis
 */
@Singleton
class OpenAIService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {
    companion object {
        private const val TAG = "OpenAIService"
        private const val API_URL = "https://api.openai.com/v1/chat/completions"
        private const val PREFS_NAME = "exit_detection_prefs"
        private const val KEY_API_KEY = "openai_api_key"
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getApiKey(): String? = prefs.getString(KEY_API_KEY, null)

    fun setApiKey(apiKey: String) {
        prefs.edit().putString(KEY_API_KEY, apiKey).apply()
    }

    fun hasApiKey(): Boolean = !getApiKey().isNullOrBlank()

    /**
     * Analyze a map image to extract location information
     */
    suspend fun analyzeMapImage(
        mapBitmap: Bitmap,
        currentLatitude: Double,
        currentLongitude: Double,
        wifiSsid: String?
    ): MapAnalysisResult = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        if (apiKey.isNullOrBlank()) {
            return@withContext MapAnalysisResult.Error("Kein OpenAI API Key konfiguriert")
        }

        try {
            // Convert bitmap to base64
            val base64Image = bitmapToBase64(mapBitmap)

            // Build the prompt
            val prompt = buildAnalysisPrompt(currentLatitude, currentLongitude, wifiSsid)

            // Build request body
            val requestBody = buildRequestBody(base64Image, prompt)

            // Make API call
            val request = Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer $apiKey")
                .addHeader("Content-Type", "application/json")
                .post(requestBody.toRequestBody("application/json".toMediaType()))
                .build()

            Log.d(TAG, "Sending map analysis request to OpenAI...")

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (!response.isSuccessful) {
                Log.e(TAG, "API error: ${response.code} - $responseBody")
                return@withContext MapAnalysisResult.Error("API Fehler: ${response.code}")
            }

            // Parse response
            parseAnalysisResponse(responseBody)

        } catch (e: Exception) {
            Log.e(TAG, "Error analyzing map", e)
            MapAnalysisResult.Error("Fehler: ${e.message}")
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
        val bytes = outputStream.toByteArray()
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    private fun buildAnalysisPrompt(lat: Double, lon: Double, wifiSsid: String?): String {
        return """
Analysiere dieses Kartenbild und extrahiere folgende Informationen im JSON-Format.
Der aktuelle Standort ist markiert (Koordinaten: $lat, $lon).
${if (wifiSsid != null) "WLAN: $wifiSsid" else ""}

Antworte NUR mit einem validen JSON-Objekt, ohne zusätzlichen Text:

{
  "buildingType": "HOUSE|APARTMENT|OFFICE|SHOPPING|HOSPITAL|SCHOOL|OTHER",
  "estimatedFloor": 0,
  "totalFloors": 1,
  "hasGarden": true/false,
  "gardenDirection": "NORTH|SOUTH|EAST|WEST|NORTHEAST|NORTHWEST|SOUTHEAST|SOUTHWEST|null",
  "nearestStreetName": "Straßenname",
  "nearestStreetDistance": 15,
  "nearestStreetDirection": "NORTH|SOUTH|EAST|WEST|NORTHEAST|NORTHWEST|SOUTHEAST|SOUTHWEST",
  "streetType": "RESIDENTIAL|MAIN_ROAD|HIGHWAY|PEDESTRIAN|PATH",
  "possibleExits": [
    {
      "name": "Haupteingang",
      "type": "MAIN_ENTRANCE|SIDE_DOOR|GARAGE|GARDEN_GATE|BACK_DOOR|EMERGENCY_EXIT",
      "direction": "NORTH|SOUTH|EAST|WEST|...",
      "estimatedDistance": 5,
      "leadsTo": "STREET|GARDEN|GARAGE|COURTYARD|PARKING"
    }
  ],
  "surroundingBuildings": 3,
  "isUrbanArea": true/false,
  "nearestPOIs": ["Supermarkt 100m", "Bushaltestelle 50m"]
}

Wichtige Regeln:
- Schätze Entfernungen in Metern basierend auf der Kartenmaßstab
- Bestimme Himmelsrichtungen relativ zum markierten Standort
- Identifiziere alle sichtbaren Ausgänge/Türen
- Erkenne ob es einen Garten, Hof oder Parkplatz gibt
- Bestimme den Gebäudetyp anhand der Kartenform
""".trimIndent()
    }

    private fun buildRequestBody(base64Image: String, prompt: String): String {
        val request = ChatRequest(
            model = "gpt-4o",
            messages = listOf(
                ChatMessage(
                    role = "user",
                    content = listOf(
                        ContentPart.Text(text = prompt),
                        ContentPart.Image(
                            imageUrl = ImageUrl(
                                url = "data:image/jpeg;base64,$base64Image"
                            )
                        )
                    )
                )
            ),
            maxTokens = 1000
        )
        return gson.toJson(request)
    }

    private fun parseAnalysisResponse(responseBody: String?): MapAnalysisResult {
        if (responseBody == null) {
            return MapAnalysisResult.Error("Leere Antwort vom Server")
        }

        try {
            val response = gson.fromJson(responseBody, ChatResponse::class.java)
            val content = response.choices.firstOrNull()?.message?.content
                ?: return MapAnalysisResult.Error("Keine Antwort im Response")

            Log.d(TAG, "GPT Response: $content")

            // Extract JSON from response (in case there's extra text)
            val jsonStart = content.indexOf("{")
            val jsonEnd = content.lastIndexOf("}") + 1
            if (jsonStart == -1 || jsonEnd <= jsonStart) {
                return MapAnalysisResult.Error("Kein JSON in Antwort gefunden")
            }

            val jsonContent = content.substring(jsonStart, jsonEnd)
            val analysisData = gson.fromJson(jsonContent, MapAnalysisData::class.java)

            return MapAnalysisResult.Success(analysisData)

        } catch (e: Exception) {
            Log.e(TAG, "Error parsing response", e)
            return MapAnalysisResult.Error("Parsing-Fehler: ${e.message}")
        }
    }
}

// Request/Response data classes
data class ChatRequest(
    val model: String,
    val messages: List<ChatMessage>,
    @SerializedName("max_tokens")
    val maxTokens: Int
)

data class ChatMessage(
    val role: String,
    val content: List<ContentPart>
)

sealed class ContentPart {
    data class Text(
        val type: String = "text",
        val text: String
    ) : ContentPart()

    data class Image(
        val type: String = "image_url",
        @SerializedName("image_url")
        val imageUrl: ImageUrl
    ) : ContentPart()
}

data class ImageUrl(
    val url: String,
    val detail: String = "high"
)

data class ChatResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: ResponseMessage
)

data class ResponseMessage(
    val content: String
)

// Analysis result
sealed class MapAnalysisResult {
    data class Success(val data: MapAnalysisData) : MapAnalysisResult()
    data class Error(val message: String) : MapAnalysisResult()
}

data class MapAnalysisData(
    val buildingType: String = "HOUSE",
    val estimatedFloor: Int = 0,
    val totalFloors: Int = 1,
    val hasGarden: Boolean = false,
    val gardenDirection: String? = null,
    val nearestStreetName: String = "",
    val nearestStreetDistance: Float = 15f,
    val nearestStreetDirection: String = "NORTH",
    val streetType: String = "RESIDENTIAL",
    val possibleExits: List<ExitData> = emptyList(),
    val surroundingBuildings: Int = 0,
    val isUrbanArea: Boolean = true,
    val nearestPOIs: List<String> = emptyList()
) {
    fun toBuildingType(): BuildingType = try {
        BuildingType.valueOf(buildingType)
    } catch (e: Exception) {
        BuildingType.HOUSE
    }

    fun toGardenDirection(): Direction? = gardenDirection?.let {
        try { Direction.valueOf(it) } catch (e: Exception) { null }
    }

    fun toStreetDirection(): Direction = try {
        Direction.valueOf(nearestStreetDirection)
    } catch (e: Exception) {
        Direction.NORTH
    }

    fun toStreetType(): StreetType = try {
        StreetType.valueOf(streetType)
    } catch (e: Exception) {
        StreetType.RESIDENTIAL
    }

    fun toExitPoints(): List<ExitPoint> = possibleExits.mapNotNull { exit ->
        try {
            ExitPoint(
                direction = Direction.valueOf(exit.direction),
                distance = exit.estimatedDistance,
                exitType = ExitType.valueOf(exit.type),
                leadsTo = exit.leadsTo
            )
        } catch (e: Exception) {
            null
        }
    }
}

data class ExitData(
    val name: String = "",
    val type: String = "MAIN_ENTRANCE",
    val direction: String = "NORTH",
    val estimatedDistance: Float = 5f,
    val leadsTo: String = "STREET"
)
