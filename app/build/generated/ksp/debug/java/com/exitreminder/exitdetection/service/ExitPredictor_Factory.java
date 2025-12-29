package com.exitreminder.exitdetection.service;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class ExitPredictor_Factory implements Factory<ExitPredictor> {
  @Override
  public ExitPredictor get() {
    return newInstance();
  }

  public static ExitPredictor_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ExitPredictor newInstance() {
    return new ExitPredictor();
  }

  private static final class InstanceHolder {
    private static final ExitPredictor_Factory INSTANCE = new ExitPredictor_Factory();
  }
}
