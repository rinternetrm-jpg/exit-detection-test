package com.exitreminder.exitdetection.service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service to capture map screenshots for analysis
 */
@Singleton
class MapCaptureService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val TAG = "MapCaptureService"
        private const val MAP_SIZE = 800
        private const val ZOOM_LEVEL = 18
    }

    /**
     * Generate a static map URL for the given coordinates
     * Uses OpenStreetMap static map service
     */
    fun getStaticMapUrl(latitude: Double, longitude: Double, zoom: Int = ZOOM_LEVEL): String {
        // Using OpenStreetMap static map tile server
        return "https://staticmap.openstreetmap.de/staticmap.php?" +
                "center=$latitude,$longitude" +
                "&zoom=$zoom" +
                "&size=${MAP_SIZE}x${MAP_SIZE}" +
                "&maptype=mapnik" +
                "&markers=$latitude,$longitude,red-pushpin"
    }

    /**
     * Alternative: Google Static Maps API URL (requires API key)
     */
    fun getGoogleStaticMapUrl(latitude: Double, longitude: Double, apiKey: String): String {
        return "https://maps.googleapis.com/maps/api/staticmap?" +
                "center=$latitude,$longitude" +
                "&zoom=$ZOOM_LEVEL" +
                "&size=${MAP_SIZE}x${MAP_SIZE}" +
                "&maptype=roadmap" +
                "&markers=color:red%7C$latitude,$longitude" +
                "&key=$apiKey"
    }

    /**
     * Download a map image from URL
     */
    suspend fun downloadMapImage(url: String): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val connection = java.net.URL(url).openConnection() as java.net.HttpURLConnection
            connection.doInput = true
            connection.connect()

            val input = connection.inputStream
            val bitmap = android.graphics.BitmapFactory.decodeStream(input)
            input.close()

            Log.d(TAG, "Map image downloaded: ${bitmap?.width}x${bitmap?.height}")
            bitmap
        } catch (e: Exception) {
            Log.e(TAG, "Error downloading map", e)
            null
        }
    }

    /**
     * Create a simple marker overlay on the map
     */
    fun addMarkerToMap(mapBitmap: Bitmap, x: Int, y: Int): Bitmap {
        val result = mapBitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(result)

        // Draw a simple red circle as marker
        val paint = android.graphics.Paint().apply {
            color = android.graphics.Color.RED
            style = android.graphics.Paint.Style.FILL
            isAntiAlias = true
        }
        canvas.drawCircle(x.toFloat(), y.toFloat(), 15f, paint)

        // Draw white border
        paint.apply {
            color = android.graphics.Color.WHITE
            style = android.graphics.Paint.Style.STROKE
            strokeWidth = 3f
        }
        canvas.drawCircle(x.toFloat(), y.toFloat(), 15f, paint)

        return result
    }
}
