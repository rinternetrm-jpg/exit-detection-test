package com.exitreminder.exitdetection.presentation.screens.analysis

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exitreminder.exitdetection.domain.model.LocationAnalysis
import com.exitreminder.exitdetection.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScreen(
    onNavigateBack: () -> Unit,
    onAnalysisComplete: (Long) -> Unit,
    onNavigateToSettings: (() -> Unit)? = null,
    viewModel: AnalysisViewModel = hiltViewModel()
) {
    val analysis by viewModel.analysis.collectAsState()
    val error by viewModel.error.collectAsState()
    val needsApiKey by viewModel.needsApiKey.collectAsState()

    LaunchedEffect(analysis.isComplete) {
        if (analysis.isComplete && analysis.profile != null) {
            // Auto-navigate to reminder config after 1 second
            kotlinx.coroutines.delay(1000)
            onAnalysisComplete(analysis.profile?.id ?: 0)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Exit Reminder") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Close, "Abbrechen")
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Loading indicator
            if (!analysis.isComplete && error == null) {
                Text(
                    text = "⏳",
                    fontSize = 48.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Analysiere deinen Standort...",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(48.dp))
            }

            // Success indicator
            if (analysis.isComplete) {
                Text(
                    text = "✅",
                    fontSize = 48.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Analyse abgeschlossen!",
                    style = MaterialTheme.typography.titleLarge,
                    color = StatusHome
                )

                Spacer(modifier = Modifier.height(48.dp))
            }

            // Analysis steps
            AnalysisSteps(analysis = analysis, needsApiKey = needsApiKey)

            // API Key hint
            if (needsApiKey) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = StatusUncertain.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Info,
                                null,
                                tint = StatusUncertain
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Ohne OpenAI API Key",
                                style = MaterialTheme.typography.titleSmall,
                                color = StatusUncertain
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Die KI-Kartenanalyse ist deaktiviert. Gehe in die Einstellungen um einen API Key zu hinterlegen für genauere Exit-Erkennung.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Error message
            if (error != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = StatusOutside.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Error,
                            null,
                            tint = StatusOutside
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = error ?: "",
                            color = StatusOutside
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.retry() },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text("Erneut versuchen")
                }
            }

            // Complete message
            if (analysis.isComplete) {
                Text(
                    text = "Weiter zur Konfiguration...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun AnalysisSteps(analysis: LocationAnalysis, needsApiKey: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AnalysisStep(
                icon = if (analysis.gpsComplete) "✅" else if (analysis.currentStep.contains("GPS")) "⏳" else "○",
                text = if (analysis.gpsComplete) "GPS Position geholt" else "GPS Position holen...",
                isComplete = analysis.gpsComplete
            )

            AnalysisStep(
                icon = if (analysis.wifiComplete) "✅" else if (analysis.currentStep.contains("WLAN")) "⏳" else "○",
                text = if (analysis.wifiComplete) "WLAN erkannt" else "WLAN erkennen...",
                isComplete = analysis.wifiComplete
            )

            AnalysisStep(
                icon = if (analysis.mapLoaded) "✅" else if (analysis.currentStep.contains("Karte")) "⏳" else "○",
                text = if (analysis.mapLoaded) "Kartenausschnitt geladen" else "Lade Kartenausschnitt...",
                isComplete = analysis.mapLoaded
            )

            AnalysisStep(
                icon = when {
                    analysis.aiAnalysisComplete -> "✅"
                    needsApiKey -> "⚠️"
                    analysis.currentStep.contains("KI") || analysis.currentStep.contains("Analyse") -> "⏳"
                    else -> "○"
                },
                text = when {
                    analysis.aiAnalysisComplete -> "KI-Analyse abgeschlossen"
                    needsApiKey -> "KI-Analyse übersprungen (kein API Key)"
                    else -> "Analysiere mit GPT-4 Vision..."
                },
                isComplete = analysis.aiAnalysisComplete,
                isWarning = needsApiKey
            )

            AnalysisStep(
                icon = if (analysis.profileComplete) "✅" else if (analysis.currentStep.contains("Profil")) "⏳" else "○",
                text = if (analysis.profileComplete) "Profil erstellt" else "Erstelle Profil...",
                isComplete = analysis.profileComplete
            )
        }
    }
}

@Composable
private fun AnalysisStep(
    icon: String,
    text: String,
    isComplete: Boolean,
    isWarning: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = when {
                isComplete -> StatusHome
                isWarning -> StatusUncertain
                else -> TextSecondary
            }
        )
    }
}
