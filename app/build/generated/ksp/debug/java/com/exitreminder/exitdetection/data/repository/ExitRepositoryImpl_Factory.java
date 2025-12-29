package com.exitreminder.exitdetection.data.repository;

import com.exitreminder.exitdetection.data.local.dao.ReminderDao;
import com.google.gson.Gson;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class ExitRepositoryImpl_Factory implements Factory<ExitRepositoryImpl> {
  private final Provider<ReminderDao> reminderDaoProvider;

  private final Provider<Gson> gsonProvider;

  public ExitRepositoryImpl_Factory(Provider<ReminderDao> reminderDaoProvider,
      Provider<Gson> gsonProvider) {
    this.reminderDaoProvider = reminderDaoProvider;
    this.gsonProvider = gsonProvider;
  }

  @Override
  public ExitRepositoryImpl get() {
    return newInstance(reminderDaoProvider.get(), gsonProvider.get());
  }

  public static ExitRepositoryImpl_Factory create(Provider<ReminderDao> reminderDaoProvider,
      Provider<Gson> gsonProvider) {
    return new ExitRepositoryImpl_Factory(reminderDaoProvider, gsonProvider);
  }

  public static ExitRepositoryImpl newInstance(ReminderDao reminderDao, Gson gson) {
    return new ExitRepositoryImpl(reminderDao, gson);
  }
}
