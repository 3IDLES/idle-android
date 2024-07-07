package com.idle.signin.center;

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
public final class CenterSignUpViewModel_Factory implements Factory<CenterSignUpViewModel> {
  @Override
  public CenterSignUpViewModel get() {
    return newInstance();
  }

  public static CenterSignUpViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CenterSignUpViewModel newInstance() {
    return new CenterSignUpViewModel();
  }

  private static final class InstanceHolder {
    private static final CenterSignUpViewModel_Factory INSTANCE = new CenterSignUpViewModel_Factory();
  }
}
