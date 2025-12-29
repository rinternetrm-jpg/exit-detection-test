package com.exitreminder.exitdetection.presentation.screens.home;

import com.exitreminder.exitdetection.domain.repository.ExitRepository;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<ExitRepository> repositoryProvider;

  private final Provider<WifiService> wifiServiceProvider;

  public HomeViewModel_Factory(Provider<ExitRepository> repositoryProvider,
      Provider<WifiService> wifiServiceProvider) {
    this.repositoryProvider = repositoryProvider;
    this.wifiServiceProvider = wifiServiceProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(repositoryProvider.get(), wifiServiceProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<ExitRepository> repositoryProvider,
      Provider<WifiService> wifiServiceProvider) {
    return new HomeViewModel_Factory(repositoryProvider, wifiServiceProvider);
  }

  public static HomeViewModel newInstance(ExitRepository repository, WifiService wifiService) {
    return new HomeViewModel(repository, wifiService);
  }
}
