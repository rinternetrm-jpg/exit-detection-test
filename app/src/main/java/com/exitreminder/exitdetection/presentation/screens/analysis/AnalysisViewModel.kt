package com.exitreminder.exitdetection.presentation.screens.analysis

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
    private val barometerService: BarometerService
) : ViewModel() {

    private val _analysis = MutableStateFlow(LocationAnalysis())
    val analysis: StateFlow<LocationAnalysis> = _analysis.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _profile = MutableStateFlow<LocationProfile?>(null)
    val profile: StateFlow<LocationProfile?> = _profile.asStateFlow()

    init {
        startAnalysis()
    }

    fun startAnalysis() {
        viewModelScope.launch {
            try {
                _error.value = null
                _analysis.value = LocationAnalysis(currentStep = "GPS Position holen...")

                // Step 1: Get GPS
                delay(500)
                val location = locationService.getCurrentLocation()
                if (location == null) {
                    _error.value = "GPS Position konnte nicht ermittelt werden"
                    return@launch
                }
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
                _analysis.value = _analysis.value.copy(
                    wifiComplete = true,
                    currentStep = "Lade Kartenausschnitt..."
                )

                // Step 3: Load map (simulated)
                delay(1000)
                _analysis.value = _analysis.value.copy(
                    mapLoaded = true,
                    currentStep = "Analysiere Umgebung..."
                )

                // Step 4: AI Analysis (simulated for now)
                delay(1500)
                _analysis.value = _analysis.value.copy(
                    aiAnalysisComplete = true,
                    currentStep = "Erstelle Profil..."
                )

                // Step 5: Create profile
                delay(500)
                val newProfile = LocationProfile(
                    wifiSsid = wifiState.ssid,
                    wifiBssid = wifiState.bssid,
                    wifiSignalAtStart = wifiState.rssi,
                    latitude = location.latitude,
                    longitude = location.longitude,
                    gpsAccuracyAtStart = location.accuracy,
                    altitude = location.altitude,
                    buildingType = BuildingType.HOUSE,  // Default, would be from AI
                    nearestStreetName = "Unbekannte Straße",  // Would be from map
                    nearestStreetDistance = 15f,
                    nearestStreetDirection = Direction.NORTH
                )

                _profile.value = newProfile
                _analysis.value = _analysis.value.copy(
                    profileComplete = true,
                    isComplete = true,
                    profile = newProfile
                )

            } catch (e: Exception) {
                _error.value = "Fehler: ${e.message}"
            }
        }
    }

    fun retry() {
        _analysis.value = LocationAnalysis()
        startAnalysis()
    }
}
