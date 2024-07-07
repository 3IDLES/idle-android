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
public final class CenterSignInViewModel_Factory implements Factory<CenterSignInViewModel> {
  @Override
  public CenterSignInViewModel get() {
    return newInstance();
  }

  public static CenterSignInViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CenterSignInViewModel newInstance() {
    return new CenterSignInViewModel();
  }

  private static final class InstanceHolder {
    private static final CenterSignInViewModel_Factory INSTANCE = new CenterSignInViewModel_Factory();
  }
}
