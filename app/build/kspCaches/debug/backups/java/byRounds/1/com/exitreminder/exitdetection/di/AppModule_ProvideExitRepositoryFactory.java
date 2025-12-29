package com.exitreminder.exitdetection.di;

import com.exitreminder.exitdetection.data.local.dao.ReminderDao;
import com.exitreminder.exitdetection.domain.repository.ExitRepository;
import com.google.gson.Gson;
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
public final class AppModule_ProvideExitRepositoryFactory implements Factory<ExitRepository> {
  private final Provider<ReminderDao> reminderDaoProvider;

  private final Provider<Gson> gsonProvider;

  public AppModule_ProvideExitRepositoryFactory(Provider<ReminderDao> reminderDaoProvider,
      Provider<Gson> gsonProvider) {
    this.reminderDaoProvider = reminderDaoProvider;
    this.gsonProvider = gsonProvider;
  }

  @Override
  public ExitRepository get() {
    return provideExitRepository(reminderDaoProvider.get(), gsonProvider.get());
  }

  public static AppModule_ProvideExitRepositoryFactory create(
      Provider<ReminderDao> reminderDaoProvider, Provider<Gson> gsonProvider) {
    return new AppModule_ProvideExitRepositoryFactory(reminderDaoProvider, gsonProvider);
  }

  public static ExitRepository provideExitRepository(ReminderDao reminderDao, Gson gson) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideExitRepository(reminderDao, gson));
  }
}
