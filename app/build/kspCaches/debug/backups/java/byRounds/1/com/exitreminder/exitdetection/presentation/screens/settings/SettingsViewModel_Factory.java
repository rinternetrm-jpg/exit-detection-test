package com.exitreminder.exitdetection.presentation.screens.settings;

import com.exitreminder.exitdetection.service.OpenAIService;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<OpenAIService> openAIServiceProvider;

  public SettingsViewModel_Factory(Provider<OpenAIService> openAIServiceProvider) {
    this.openAIServiceProvider = openAIServiceProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(openAIServiceProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<OpenAIService> openAIServiceProvider) {
    return new SettingsViewModel_Factory(openAIServiceProvider);
  }

  public static SettingsViewModel newInstance(OpenAIService openAIService) {
    return new SettingsViewModel(openAIService);
  }
}
