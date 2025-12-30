package com.exitreminder.exitdetection.presentation.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exitreminder.exitdetection.domain.model.Reminder
import com.exitreminder.exitdetection.domain.repository.ExitRepository
import com.exitreminder.exitdetection.service.WifiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ExitRepository,
    private val wifiService: WifiService
) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _reminders = MutableStateFlow<List<Reminder>>(emptyList())
    val reminders: StateFlow<List<Reminder>> = _reminders.asStateFlow()

    private val _currentWifiSsid = MutableStateFlow<String?>(null)
    val currentWifiSsid: StateFlow<String?> = _currentWifiSsid.asStateFlow()

    init {
        Log.d(TAG, "HomeViewModel init started")
        loadReminders()
        updateWifiState()
        Log.d(TAG, "HomeViewModel init completed")
    }

    private fun loadReminders() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.getAllReminders()
                    .catch { e ->
                        Log.e(TAG, "Error loading reminders", e)
                        _isLoading.value = false
                    }
                    .collect { list ->
                        _reminders.value = list
                        _isLoading.value = false
                    }
            } catch (e: Exception) {
                Log.e(TAG, "Exception in loadReminders", e)
                _isLoading.value = false
            }
        }
    }

    fun updateWifiState() {
        try {
            _currentWifiSsid.value = wifiService.getCurrentSsid()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting WiFi SSID", e)
            _currentWifiSsid.value = null
        }
    }

    fun toggleActive(reminderId: Long, isActive: Boolean) {
        viewModelScope.launch {
            try {
                repository.setActive(reminderId, isActive)
            } catch (e: Exception) {
                Log.e(TAG, "Error toggling active", e)
            }
        }
    }

    fun deleteReminder(reminderId: Long) {
        viewModelScope.launch {
            try {
                repository.deleteReminder(reminderId)
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting reminder", e)
            }
        }
    }
}
