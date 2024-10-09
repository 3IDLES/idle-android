package com.idle.splash;

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
public final class SplashViewModel_Factory implements Factory<SplashViewModel> {
  @Override
  public SplashViewModel get() {
    return newInstance();
  }

  public static SplashViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SplashViewModel newInstance() {
    return new SplashViewModel();
  }

  private static final class InstanceHolder {
    private static final SplashViewModel_Factory INSTANCE = new SplashViewModel_Factory();
  }
}
