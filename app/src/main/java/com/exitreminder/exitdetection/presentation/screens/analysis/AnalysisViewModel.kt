package com.exitreminder.exitdetection.presentation.screens.analysis

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exitreminder.exitdetection.domain.model.*
import com.exitreminder.exitdetection.service.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalysisViewModel @Inject constructor(
    private val wifiService: WifiService,
    private val locationService: LocationService,
    private val barometerService: BarometerService,
    private val openAIService: OpenAIService,
    private val mapCaptureService: MapCaptureService
) : ViewModel() {

    companion object {
        private const val TAG = "AnalysisViewModel"
    }

    private val _analysis = MutableStateFlow(LocationAnalysis())
    val analysis: StateFlow<LocationAnalysis> = _analysis.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _profile = MutableStateFlow<LocationProfile?>(null)
    val profile: StateFlow<LocationProfile?> = _profile.asStateFlow()

    private val _needsApiKey = MutableStateFlow(false)
    val needsApiKey: StateFlow<Boolean> = _needsApiKey.asStateFlow()

    init {
        startAnalysis()
    }

    fun startAnalysis() {
        viewModelScope.launch {
            try {
                _error.value = null
                _needsApiKey.value = false
                _analysis.value = LocationAnalysis(currentStep = "GPS Position holen...")

                // Step 1: Get GPS
                delay(500)
                val location = locationService.getCurrentLocation()
                if (location == null) {
                    _error.value = "GPS Position konnte nicht ermittelt werden"
                    return@launch
                }
                Log.d(TAG, "GPS: ${location.latitude}, ${location.longitude}")
                _analysis.value = _analysis.value.copy(
                    gpsComplete = true,
                    currentStep = "WLAN erkennen..."
                )

                // Step 2: Get WiFi
                delay(300)
                wifiService.updateWifiState()
                val wifiState = wifiService.wifiState.value
                if (!wifiState.isConnected) {
                    _error.value = "Kein WLAN verbunden"
                    return@launch
                }
                Log.d(TAG, "WLAN: ${wifiState.ssid} (${wifiState.rssi} dBm)")
                _analysis.value = _analysis.value.copy(
                    wifiComplete = true,
                    currentStep = "Lade Kartenausschnitt..."
                )

                // Step 3: Load map
                val mapUrl = mapCaptureService.getStaticMapUrl(location.latitude, location.longitude)
                Log.d(TAG, "Loading map from: $mapUrl")
                val mapBitmap = mapCaptureService.downloadMapImage(mapUrl)

                if (mapBitmap == null) {
                    Log.w(TAG, "Could not load map, using defaults")
                    _analysis.value = _analysis.value.copy(
                        mapLoaded = false,
                        currentStep = "Karte konnte nicht geladen werden, verwende Standardwerte..."
                    )
                    delay(500)
                } else {
                    Log.d(TAG, "Map loaded: ${mapBitmap.width}x${mapBitmap.height}")
                    _analysis.value = _analysis.value.copy(
                        mapLoaded = true,
                        currentStep = "Analysiere Umgebung mit KI..."
                    )
                }

                // Step 4: AI Analysis (if API key configured)
                var aiData: MapAnalysisData? = null

                if (openAIService.hasApiKey() && mapBitmap != null) {
                    Log.d(TAG, "Starting GPT-4 Vision analysis...")

                    val result = openAIService.analyzeMapImage(
                        mapBitmap = mapBitmap,
                        currentLatitude = location.latitude,
                        currentLongitude = location.longitude,
                        wifiSsid = wifiState.ssid
                    )

                    when (result) {
                        is MapAnalysisResult.Success -> {
                            aiData = result.data
                            Log.d(TAG, "AI Analysis complete: ${result.data}")
                            _analysis.value = _analysis.value.copy(
                                aiAnalysisComplete = true,
                                currentStep = "KI-Analyse abgeschlossen!"
                            )
                        }
                        is MapAnalysisResult.Error -> {
                            Log.e(TAG, "AI Analysis failed: ${result.message}")
                            _analysis.value = _analysis.value.copy(
                                aiAnalysisComplete = false,
                                currentStep = "KI-Analyse fehlgeschlagen: ${result.message}"
                            )
                        }
                    }
                    delay(500)
                } else if (!openAIService.hasApiKey()) {
                    Log.d(TAG, "No API key configured, skipping AI analysis")
                    _needsApiKey.value = true
                    _analysis.value = _analysis.value.copy(
                        aiAnalysisComplete = false,
                        currentStep = "Kein OpenAI API Key - verwende Standardwerte..."
                    )
                    delay(500)
                }

                // Step 5: Create profile
                _analysis.value = _analysis.value.copy(
                    currentStep = "Erstelle Profil..."
                )
                delay(300)

                val newProfile = LocationProfile(
                    wifiSsid = wifiState.ssid,
                    wifiBssid = wifiState.bssid,
                    wifiSignalAtStart = wifiState.rssi,
                    latitude = location.latitude,
                    longitude = location.longitude,
                    gpsAccuracyAtStart = location.accuracy,
                    altitude = location.altitude,
                    // Use AI data if available, otherwise defaults
                    buildingType = aiData?.toBuildingType() ?: BuildingType.HOUSE,
                    estimatedFloor = aiData?.estimatedFloor ?: 0,
                    totalFloors = aiData?.totalFloors ?: 1,
                    hasGarden = aiData?.hasGarden ?: false,
                    gardenDirection = aiData?.toGardenDirection(),
                    nearestStreetName = aiData?.nearestStreetName ?: "Unbekannte Straße",
                    nearestStreetDistance = aiData?.nearestStreetDistance ?: 15f,
                    nearestStreetDirection = aiData?.toStreetDirection() ?: Direction.NORTH,
                    streetType = aiData?.toStreetType() ?: StreetType.RESIDENTIAL,
                    possibleExits = aiData?.toExitPoints() ?: emptyList(),
                    surroundingBuildings = aiData?.surroundingBuildings ?: 0,
                    isUrbanArea = aiData?.isUrbanArea ?: true,
                    nearestPOIs = aiData?.nearestPOIs ?: emptyList()
                )

                _profile.value = newProfile
                _analysis.value = _analysis.value.copy(
                    profileComplete = true,
                    isComplete = true,
                    profile = newProfile
                )

                Log.d(TAG, "Profile created: $newProfile")

            } catch (e: Exception) {
                Log.e(TAG, "Analysis error", e)
                _error.value = "Fehler: ${e.message}"
            }
        }
    }

    fun retry() {
        _analysis.value = LocationAnalysis()
        startAnalysis()
    }
}
