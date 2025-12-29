package com.exitreminder.exitdetection.di;

import com.exitreminder.exitdetection.data.local.AppDatabase;
import com.exitreminder.exitdetection.data.local.dao.ReminderDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideReminderDaoFactory implements Factory<ReminderDao> {
  private final Provider<AppDatabase> databaseProvider;

  public AppModule_ProvideReminderDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ReminderDao get() {
    return provideReminderDao(databaseProvider.get());
  }

  public static AppModule_ProvideReminderDaoFactory create(Provider<AppDatabase> databaseProvider) {
    return new AppModule_ProvideReminderDaoFactory(databaseProvider);
  }

  public static ReminderDao provideReminderDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideReminderDao(database));
  }
}
