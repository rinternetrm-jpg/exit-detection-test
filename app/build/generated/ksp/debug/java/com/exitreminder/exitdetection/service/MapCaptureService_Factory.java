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
public final class MapCaptureService_Factory implements Factory<MapCaptureService> {
  private final Provider<Context> contextProvider;

  public MapCaptureService_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public MapCaptureService get() {
    return newInstance(contextProvider.get());
  }

  public static MapCaptureService_Factory create(Provider<Context> contextProvider) {
    return new MapCaptureService_Factory(contextProvider);
  }

  public static MapCaptureService newInstance(Context context) {
    return new MapCaptureService(context);
  }
}
