package com.exitreminder.exitdetection.service;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class StepCounterService_Factory implements Factory<StepCounterService> {
  private final Provider<Context> contextProvider;

  public StepCounterService_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public StepCounterService get() {
    return newInstance(contextProvider.get());
  }

  public static StepCounterService_Factory create(Provider<Context> contextProvider) {
    return new StepCounterService_Factory(contextProvider);
  }

  public static StepCounterService newInstance(Context context) {
    return new StepCounterService(context);
  }
}
