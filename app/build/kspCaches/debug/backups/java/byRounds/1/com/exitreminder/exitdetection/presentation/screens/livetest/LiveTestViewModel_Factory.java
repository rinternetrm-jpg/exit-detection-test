package com.exitreminder.exitdetection.presentation.screens.livetest;

import androidx.lifecycle.SavedStateHandle;
import com.exitreminder.exitdetection.domain.repository.ExitRepository;
import com.exitreminder.exitdetection.service.BarometerService;
import com.exitreminder.exitdetection.service.ExitPredictor;
import com.exitreminder.exitdetection.service.LightSensorService;
import com.exitreminder.exitdetection.service.LocationService;
import com.exitreminder.exitdetection.service.StepCounterService;
import com.exitreminder.exitdetection.service.WifiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class LiveTestViewModel_Factory implements Factory<LiveTestViewModel> {
  private final Provider<ExitRepository> repositoryProvider;

  private final Provider<WifiService> wifiServiceProvider;

  private final Provider<LocationService> locationServiceProvider;

  private final Provider<StepCounterService> stepCounterServiceProvider;

  private final Provider<BarometerService> barometerServiceProvider;

  private final Provider<LightSensorService> lightSensorServiceProvider;

  private final Provider<ExitPredictor> exitPredictorProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public LiveTestViewModel_Factory(Provider<ExitRepository> repositoryProvider,
      Provider<WifiService> wifiServiceProvider, Provider<LocationService> locationServiceProvider,
      Provider<StepCounterService> stepCounterServiceProvider,
      Provider<BarometerService> barometerServiceProvider,
      Provider<LightSensorService> lightSensorServiceProvider,
      Provider<ExitPredictor> exitPredictorProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.repositoryProvider = repositoryProvider;
    this.wifiServiceProvider = wifiServiceProvider;
    this.locationServiceProvider = locationServiceProvider;
    this.stepCounterServiceProvider = stepCounterServiceProvider;
    this.barometerServiceProvider = barometerServiceProvider;
    this.lightSensorServiceProvider = lightSensorServiceProvider;
    this.exitPredictorProvider = exitPredictorProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public LiveTestViewModel get() {
    return newInstance(repositoryProvider.get(), wifiServiceProvider.get(), locationServiceProvider.get(), stepCounterServiceProvider.get(), barometerServiceProvider.get(), lightSensorServiceProvider.get(), exitPredictorProvider.get(), savedStateHandleProvider.get());
  }

  public static LiveTestViewModel_Factory create(Provider<ExitRepository> repositoryProvider,
      Provider<WifiService> wifiServiceProvider, Provider<LocationService> locationServiceProvider,
      Provider<StepCounterService> stepCounterServiceProvider,
      Provider<BarometerService> barometerServiceProvider,
      Provider<LightSensorService> lightSensorServiceProvider,
      Provider<ExitPredictor> exitPredictorProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new LiveTestViewModel_Factory(repositoryProvider, wifiServiceProvider, locationServiceProvider, stepCounterServiceProvider, barometerServiceProvider, lightSensorServiceProvider, exitPredictorProvider, savedStateHandleProvider);
  }

  public static LiveTestViewModel newInstance(ExitRepository repository, WifiService wifiService,
      LocationService locationService, StepCounterService stepCounterService,
      BarometerService barometerService, LightSensorService lightSensorService,
      ExitPredictor exitPredictor, SavedStateHandle savedStateHandle) {
    return new LiveTestViewModel(repository, wifiService, locationService, stepCounterService, barometerService, lightSensorService, exitPredictor, savedStateHandle);
  }
}
