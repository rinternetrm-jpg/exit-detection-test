package com.exitreminder.exitdetection.presentation.screens.livetest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exitreminder.exitdetection.domain.model.*
import com.exitreminder.exitdetection.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveTestScreen(
    onNavigateBack: () -> Unit,
    viewModel: LiveTestViewModel = hiltViewModel()
) {
    val reminder by viewModel.reminder.collectAsState()
    val sensorData by viewModel.sensorData.collectAsState()
    val prediction by viewModel.prediction.collectAsState()
    val isRecording by viewModel.isRecording.collectAsState()
    val recordingDuration by viewModel.recordingDuration.collectAsState()

    DisposableEffect(Unit) {
        viewModel.startMonitoring()
        onDispose {
            viewModel.stopMonitoring()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Live-Test")
                        if (reminder != null) {
                            Text(
                                text = ": ${reminder?.name}",
                                color = TextSecondary
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Zurück")
                    }
                },
                actions = {
                    if (isRecording) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(StatusOutside)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "REC ${formatDuration(recordingDuration)}",
                                color = StatusOutside
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background,
                    titleContentColor = TextPrimary
                )
            )
        },
        containerColor = Background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Exit Prediction Card
            PredictionCard(prediction = prediction)

            // Sensor Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SensorCard(
                    title = "WLAN",
                    emoji = "📶",
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Signal: ${sensorData.wifiSignal} dBm",
                        color = getSignalColor(sensorData.wifiSignal)
                    )
                    LinearProgressIndicator(
                        progress = sensorData.wifiSignalPercent / 100f,
                        modifier = Modifier.fillMaxWidth(),
                        color = getSignalColor(sensorData.wifiSignal)
                    )
                    Text(
                        text = "Trend: ${sensorData.wifiSignalTrend.symbol}",
                        color = TextSecondary
                    )
                    Text(
                        text = "SSID: ${sensorData.wifiSsid ?: "—"}",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                SensorCard(
                    title = "POSITION",
                    emoji = "📍",
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Accuracy: ${sensorData.gpsAccuracy.toInt()}m",
                        color = if (sensorData.gpsAccuracy < 20) StatusHome else TextSecondary
                    )
                    Text(
                        text = "Speed: ${String.format("%.1f", sensorData.gpsSpeed)} m/s",
                        color = TextPrimary
                    )
                    Text(
                        text = "Distanz Start: ${sensorData.distanceFromStart.toInt()}m",
                        color = TextSecondary
                    )
                    Text(
                        text = "Distanz Straße: ${sensorData.distanceToStreet.toInt()}m",
                        color = if (sensorData.distanceToStreet < 10) StatusLeaving else TextSecondary
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SensorCard(
                    title = "BEWEGUNG",
                    emoji = "👣",
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Schritte: ${sensorData.steps}",
                        color = TextPrimary
                    )
                    Text(
                        text = "Status: ${if (sensorData.isWalking) "🚶 GEHEND" else "🧍 STEHEND"}",
                        color = if (sensorData.isWalking) StatusLeaving else TextSecondary
                    )
                    Text(
                        text = "Richtung: ${sensorData.walkingDirection?.symbol ?: "—"}",
                        color = TextSecondary
                    )
                    Text(
                        text = "Letzte 10s: ${sensorData.stepsSinceLastCheck}",
                        color = TextSecondary
                    )
                }

                SensorCard(
                    title = "HÖHE",
                    emoji = "📐",
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Höhe: ${String.format("%.1f", sensorData.altitude)}m",
                        color = TextPrimary
                    )
                    Text(
                        text = "Änderung: ${String.format("%+.1f", sensorData.altitudeChange)}m",
                        color = TextSecondary
                    )
                    Text(
                        text = "Etage: ${sensorData.estimatedFloor}",
                        color = TextSecondary
                    )
                    Row {
                        Text("Treppe: ", color = TextSecondary)
                        Text(
                            if (sensorData.stairsDetected) "✅" else "❌",
                            color = if (sensorData.stairsDetected) StatusHome else TextSecondary
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SensorCard(
                    title = "LICHT",
                    emoji = "💡",
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Level: ${sensorData.lightLevel.toInt()} Lux",
                        color = TextPrimary
                    )
                    Text(
                        text = "Status: ${if (sensorData.lightLevel > 1000) "☀️ OUTDOOR?" else "🏠 INDOOR"}",
                        color = if (sensorData.lightLevel > 1000) StatusLeaving else TextSecondary
                    )
                    Text(
                        text = "Trend: ${sensorData.lightTrend.symbol}",
                        color = TextSecondary
                    )
                }

                SensorCard(
                    title = "RICHTUNG",
                    emoji = "🧭",
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Bearing: ${sensorData.gpsBearing.toInt()}°",
                        color = TextPrimary
                    )
                    Row {
                        Text("Zur Straße: ", color = TextSecondary)
                        Text(
                            if (sensorData.movingTowardsStreet) "✅ JA" else "—",
                            color = if (sensorData.movingTowardsStreet) StatusHome else TextSecondary
                        )
                    }
                    Row {
                        Text("Zum Exit: ", color = TextSecondary)
                        Text(
                            if (sensorData.movingTowardsExit) "✅ JA" else "—",
                            color = if (sensorData.movingTowardsExit) StatusHome else TextSecondary
                        )
                    }
                }
            }

            // Trigger warning
            if (prediction.shouldTrigger) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = StatusLeaving.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🔔 REMINDER WÜRDE JETZT TRIGGERN!",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = StatusLeaving
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "\"${reminder?.reminderText}\"",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PredictionCard(prediction: ExitPrediction) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🎯 EXIT PREDICTION",
                style = MaterialTheme.typography.titleMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Probability bar
            Text(
                text = "${prediction.exitProbabilityPercent}%",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = getStatusColor(prediction.exitProbability)
            )

            LinearProgressIndicator(
                progress = prediction.exitProbability,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = getStatusColor(prediction.exitProbability),
                trackColor = SurfaceLight
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = prediction.status.displayName.uppercase(),
                style = MaterialTheme.typography.labelLarge,
                color = getStatusColor(prediction.exitProbability)
            )

            prediction.estimatedSecondsToExit?.let { seconds ->
                Text(
                    text = "Geschätzt: ~$seconds Sekunden bis Exit",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun SensorCard(
    title: String,
    emoji: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "$emoji $title",
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            content()
        }
    }
}

private fun formatDuration(seconds: Long): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", mins, secs)
}
