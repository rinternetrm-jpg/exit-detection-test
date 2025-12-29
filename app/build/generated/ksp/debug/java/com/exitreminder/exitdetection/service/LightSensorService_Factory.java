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
public final class LightSensorService_Factory implements Factory<LightSensorService> {
  private final Provider<Context> contextProvider;

  public LightSensorService_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public LightSensorService get() {
    return newInstance(contextProvider.get());
  }

  public static LightSensorService_Factory create(Provider<Context> contextProvider) {
    return new LightSensorService_Factory(contextProvider);
  }

  public static LightSensorService newInstance(Context context) {
    return new LightSensorService(context);
  }
}
