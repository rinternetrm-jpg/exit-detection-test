package com.exitreminder.exitdetection.presentation.screens.reminder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exitreminder.exitdetection.domain.model.*
import com.exitreminder.exitdetection.domain.repository.ExitRepository
import com.exitreminder.exitdetection.service.WifiService
import com.exitreminder.exitdetection.service.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderConfigViewModel @Inject constructor(
    private val repository: ExitRepository,
    private val wifiService: WifiService,
    private val locationService: LocationService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _profile = MutableStateFlow<LocationProfile?>(null)
    val profile: StateFlow<LocationProfile?> = _profile.asStateFlow()

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _reminderText = MutableStateFlow("")
    val reminderText: StateFlow<String> = _reminderText.asStateFlow()

    private val _isCreating = MutableStateFlow(false)
    val isCreating: StateFlow<Boolean> = _isCreating.asStateFlow()

    var onReminderCreated: (() -> Unit)? = null

    init {
        // Create profile from current location
        viewModelScope.launch {
            wifiService.updateWifiState()
            val wifiState = wifiService.wifiState.value
            val location = locationService.getCurrentLocation()

            _profile.value = LocationProfile(
                wifiSsid = wifiState.ssid,
                wifiBssid = wifiState.bssid,
                wifiSignalAtStart = wifiState.rssi,
                latitude = location?.latitude ?: 0.0,
                longitude = location?.longitude ?: 0.0,
                gpsAccuracyAtStart = location?.accuracy ?: 100f,
                altitude = location?.altitude ?: 0.0,
                buildingType = BuildingType.HOUSE,
                nearestStreetName = "",
                nearestStreetDistance = 15f,
                nearestStreetDirection = Direction.NORTH
            )

            // Default name suggestion
            _name.value = "Zuhause"
        }
    }

    fun setName(value: String) {
        _name.value = value
    }

    fun setReminderText(value: String) {
        _reminderText.value = value
    }

    fun createReminder() {
        val currentProfile = _profile.value ?: return
        val currentName = _name.value.takeIf { it.isNotBlank() } ?: return
        val currentText = _reminderText.value.takeIf { it.isNotBlank() } ?: return

        viewModelScope.launch {
            _isCreating.value = true

            val reminder = Reminder(
                name = currentName,
                reminderText = currentText,
                profile = currentProfile
            )

            repository.insertReminder(reminder)

            // Log creation event
            repository.logEvent(
                reminderId = 0,  // Will be set by DB
                event = ExitEvent(
                    type = ExitEventType.PROFILE_CREATED,
                    title = "Profil erstellt",
                    description = "Ort: $currentName, WLAN: ${currentProfile.wifiSsid}"
                )
            )

            _isCreating.value = false
            onReminderCreated?.invoke()
        }
    }
}
