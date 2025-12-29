package com.exitreminder.exitdetection.presentation.screens.home

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

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val reminders: StateFlow<List<Reminder>> = repository.getAllReminders()
        .onStart { _isLoading.value = true }
        .onEach { _isLoading.value = false }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _currentWifiSsid = MutableStateFlow<String?>(null)
    val currentWifiSsid: StateFlow<String?> = _currentWifiSsid.asStateFlow()

    init {
        updateWifiState()
    }

    fun updateWifiState() {
        _currentWifiSsid.value = wifiService.getCurrentSsid()
    }

    fun toggleActive(reminderId: Long, isActive: Boolean) {
        viewModelScope.launch {
            repository.setActive(reminderId, isActive)
        }
    }

    fun deleteReminder(reminderId: Long) {
        viewModelScope.launch {
            repository.deleteReminder(reminderId)
        }
    }
}
