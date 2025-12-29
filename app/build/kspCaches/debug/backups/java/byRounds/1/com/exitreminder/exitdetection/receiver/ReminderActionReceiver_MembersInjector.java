package com.exitreminder.exitdetection.receiver;

import com.exitreminder.exitdetection.domain.repository.ExitRepository;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class ReminderActionReceiver_MembersInjector implements MembersInjector<ReminderActionReceiver> {
  private final Provider<ExitRepository> repositoryProvider;

  public ReminderActionReceiver_MembersInjector(Provider<ExitRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  public static MembersInjector<ReminderActionReceiver> create(
      Provider<ExitRepository> repositoryProvider) {
    return new ReminderActionReceiver_MembersInjector(repositoryProvider);
  }

  @Override
  public void injectMembers(ReminderActionReceiver instance) {
    injectRepository(instance, repositoryProvider.get());
  }

  @InjectedFieldSignature("com.exitreminder.exitdetection.receiver.ReminderActionReceiver.repository")
  public static void injectRepository(ReminderActionReceiver instance, ExitRepository repository) {
    instance.repository = repository;
  }
}
