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
public final class WorkerSignUpViewModel_Factory implements Factory<WorkerSignUpViewModel> {
  @Override
  public WorkerSignUpViewModel get() {
    return newInstance();
  }

  public static WorkerSignUpViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static WorkerSignUpViewModel newInstance() {
    return new WorkerSignUpViewModel();
  }

  private static final class InstanceHolder {
    private static final WorkerSignUpViewModel_Factory INSTANCE = new WorkerSignUpViewModel_Factory();
  }
}
