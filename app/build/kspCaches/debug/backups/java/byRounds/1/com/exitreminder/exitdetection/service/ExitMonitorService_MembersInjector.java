package com.exitreminder.exitdetection.service;

import com.exitreminder.exitdetection.domain.repository.ExitRepository;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class ExitMonitorService_MembersInjector implements MembersInjector<ExitMonitorService> {
  private final Provider<ExitRepository> repositoryProvider;

  private final Provider<WifiService> wifiServiceProvider;

  private final Provider<LocationService> locationServiceProvider;

  private final Provider<StepCounterService> stepCounterServiceProvider;

  private final Provider<BarometerService> barometerServiceProvider;

  private final Provider<LightSensorService> lightSensorServiceProvider;

  private final Provider<ExitPredictor> exitPredictorProvider;

  public ExitMonitorService_MembersInjector(Provider<ExitRepository> repositoryProvider,
      Provider<WifiService> wifiServiceProvider, Provider<LocationService> locationServiceProvider,
      Provider<StepCounterService> stepCounterServiceProvider,
      Provider<BarometerService> barometerServiceProvider,
      Provider<LightSensorService> lightSensorServiceProvider,
      Provider<ExitPredictor> exitPredictorProvider) {
    this.repositoryProvider = repositoryProvider;
    this.wifiServiceProvider = wifiServiceProvider;
    this.locationServiceProvider = locationServiceProvider;
    this.stepCounterServiceProvider = stepCounterServiceProvider;
    this.barometerServiceProvider = barometerServiceProvider;
    this.lightSensorServiceProvider = lightSensorServiceProvider;
    this.exitPredictorProvider = exitPredictorProvider;
  }

  public static MembersInjector<ExitMonitorService> create(
      Provider<ExitRepository> repositoryProvider, Provider<WifiService> wifiServiceProvider,
      Provider<LocationService> locationServiceProvider,
      Provider<StepCounterService> stepCounterServiceProvider,
      Provider<BarometerService> barometerServiceProvider,
      Provider<LightSensorService> lightSensorServiceProvider,
      Provider<ExitPredictor> exitPredictorProvider) {
    return new ExitMonitorService_MembersInjector(repositoryProvider, wifiServiceProvider, locationServiceProvider, stepCounterServiceProvider, barometerServiceProvider, lightSensorServiceProvider, exitPredictorProvider);
  }

  @Override
  public void injectMembers(ExitMonitorService instance) {
    injectRepository(instance, repositoryProvider.get());
    injectWifiService(instance, wifiServiceProvider.get());
    injectLocationService(instance, locationServiceProvider.get());
    injectStepCounterService(instance, stepCounterServiceProvider.get());
    injectBarometerService(instance, barometerServiceProvider.get());
    injectLightSensorService(instance, lightSensorServiceProvider.get());
    injectExitPredictor(instance, exitPredictorProvider.get());
  }

  @InjectedFieldSignature("com.exitreminder.exitdetection.service.ExitMonitorService.repository")
  public static void injectRepository(ExitMonitorService instance, ExitRepository repository) {
    instance.repository = repository;
  }

  @InjectedFieldSignature("com.exitreminder.exitdetection.service.ExitMonitorService.wifiService")
  public static void injectWifiService(ExitMonitorService instance, WifiService wifiService) {
    instance.wifiService = wifiService;
  }

  @InjectedFieldSignature("com.exitreminder.exitdetection.service.ExitMonitorService.locationService")
  public static void injectLocationService(ExitMonitorService instance,
      LocationService locationService) {
    instance.locationService = locationService;
  }

  @InjectedFieldSignature("com.exitreminder.exitdetection.service.ExitMonitorService.stepCounterService")
  public static void injectStepCounterService(ExitMonitorService instance,
      StepCounterService stepCounterService) {
    instance.stepCounterService = stepCounterService;
  }

  @InjectedFieldSignature("com.exitreminder.exitdetection.service.ExitMonitorService.barometerService")
  public static void injectBarometerService(ExitMonitorService instance,
      BarometerService barometerService) {
    instance.barometerService = barometerService;
  }

  @InjectedFieldSignature("com.exitreminder.exitdetection.service.ExitMonitorService.lightSensorService")
  public static void injectLightSensorService(ExitMonitorService instance,
      LightSensorService lightSensorService) {
    instance.lightSensorService = lightSensorService;
  }

  @InjectedFieldSignature("com.exitreminder.exitdetection.service.ExitMonitorService.exitPredictor")
  public static void injectExitPredictor(ExitMonitorService instance, ExitPredictor exitPredictor) {
    instance.exitPredictor = exitPredictor;
  }
}
