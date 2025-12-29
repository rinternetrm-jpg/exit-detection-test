package com.exitreminder.exitdetection.service;

import android.content.Context;
import com.google.gson.Gson;
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
public final class OpenAIService_Factory implements Factory<OpenAIService> {
  private final Provider<Context> contextProvider;

  private final Provider<Gson> gsonProvider;

  public OpenAIService_Factory(Provider<Context> contextProvider, Provider<Gson> gsonProvider) {
    this.contextProvider = contextProvider;
    this.gsonProvider = gsonProvider;
  }

  @Override
  public OpenAIService get() {
    return newInstance(contextProvider.get(), gsonProvider.get());
  }

  public static OpenAIService_Factory create(Provider<Context> contextProvider,
      Provider<Gson> gsonProvider) {
    return new OpenAIService_Factory(contextProvider, gsonProvider);
  }

  public static OpenAIService newInstance(Context context, Gson gson) {
    return new OpenAIService(context, gson);
  }
}
