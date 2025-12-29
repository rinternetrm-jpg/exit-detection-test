package com.exitreminder.exitdetection.presentation.screens.reminder;

import androidx.lifecycle.SavedStateHandle;
import com.exitreminder.exitdetection.domain.repository.ExitRepository;
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
public final class ReminderConfigViewModel_Factory implements Factory<ReminderConfigViewModel> {
  private final Provider<ExitRepository> repositoryProvider;

  private final Provider<WifiService> wifiServiceProvider;

  private final Provider<LocationService> locationServiceProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public ReminderConfigViewModel_Factory(Provider<ExitRepository> repositoryProvider,
      Provider<WifiService> wifiServiceProvider, Provider<LocationService> locationServiceProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.repositoryProvider = repositoryProvider;
    this.wifiServiceProvider = wifiServiceProvider;
    this.locationServiceProvider = locationServiceProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public ReminderConfigViewModel get() {
    return newInstance(repositoryProvider.get(), wifiServiceProvider.get(), locationServiceProvider.get(), savedStateHandleProvider.get());
  }

  public static ReminderConfigViewModel_Factory create(Provider<ExitRepository> repositoryProvider,
      Provider<WifiService> wifiServiceProvider, Provider<LocationService> locationServiceProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new ReminderConfigViewModel_Factory(repositoryProvider, wifiServiceProvider, locationServiceProvider, savedStateHandleProvider);
  }

  public static ReminderConfigViewModel newInstance(ExitRepository repository,
      WifiService wifiService, LocationService locationService, SavedStateHandle savedStateHandle) {
    return new ReminderConfigViewModel(repository, wifiService, locationService, savedStateHandle);
  }
}
