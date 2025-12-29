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
public final class BarometerService_Factory implements Factory<BarometerService> {
  private final Provider<Context> contextProvider;

  public BarometerService_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public BarometerService get() {
    return newInstance(contextProvider.get());
  }

  public static BarometerService_Factory create(Provider<Context> contextProvider) {
    return new BarometerService_Factory(contextProvider);
  }

  public static BarometerService newInstance(Context context) {
    return new BarometerService(context);
  }
}
