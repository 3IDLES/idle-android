package com.idle.signin.worker;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
    "KotlinInternalInJava",
    "cast"
})
public final class WorkerSignInViewModel_Factory implements Factory<WorkerSignInViewModel> {
  @Override
  public WorkerSignInViewModel get() {
    return newInstance();
  }

  public static WorkerSignInViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static WorkerSignInViewModel newInstance() {
    return new WorkerSignInViewModel();
  }

  private static final class InstanceHolder {
    private static final WorkerSignInViewModel_Factory INSTANCE = new WorkerSignInViewModel_Factory();
  }
}
