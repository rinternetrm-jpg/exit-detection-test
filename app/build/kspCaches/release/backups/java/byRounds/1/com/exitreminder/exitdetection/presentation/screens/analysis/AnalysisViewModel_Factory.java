package com.exitreminder.exitdetection.presentation.screens.analysis;

import com.exitreminder.exitdetection.service.BarometerService;
import com.exitreminder.exitdetection.service.LocationService;
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
public final class AnalysisViewModel_Factory implements Factory<AnalysisViewModel> {
  private final Provider<WifiService> wifiServiceProvider;

  private final Provider<LocationService> locationServiceProvider;

  private final Provider<BarometerService> barometerServiceProvider;

  public AnalysisViewModel_Factory(Provider<WifiService> wifiServiceProvider,
      Provider<LocationService> locationServiceProvider,
      Provider<BarometerService> barometerServiceProvider) {
    this.wifiServiceProvider = wifiServiceProvider;
    this.locationServiceProvider = locationServiceProvider;
    this.barometerServiceProvider = barometerServiceProvider;
  }

  @Override
  public AnalysisViewModel get() {
    return newInstance(wifiServiceProvider.get(), locationServiceProvider.get(), barometerServiceProvider.get());
  }

  public static AnalysisViewModel_Factory create(Provider<WifiService> wifiServiceProvider,
      Provider<LocationService> locationServiceProvider,
      Provider<BarometerService> barometerServiceProvider) {
    return new AnalysisViewModel_Factory(wifiServiceProvider, locationServiceProvider, barometerServiceProvider);
  }

  public static AnalysisViewModel newInstance(WifiService wifiService,
      LocationService locationService, BarometerService barometerService) {
    return new AnalysisViewModel(wifiService, locationService, barometerService);
  }
}
