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
public final class BootReceiver_MembersInjector implements MembersInjector<BootReceiver> {
  private final Provider<ExitRepository> repositoryProvider;

  public BootReceiver_MembersInjector(Provider<ExitRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  public static MembersInjector<BootReceiver> create(Provider<ExitRepository> repositoryProvider) {
    return new BootReceiver_MembersInjector(repositoryProvider);
  }

  @Override
  public void injectMembers(BootReceiver instance) {
    injectRepository(instance, repositoryProvider.get());
  }

  @InjectedFieldSignature("com.exitreminder.exitdetection.receiver.BootReceiver.repository")
  public static void injectRepository(BootReceiver instance, ExitRepository repository) {
    instance.repository = repository;
  }
}
