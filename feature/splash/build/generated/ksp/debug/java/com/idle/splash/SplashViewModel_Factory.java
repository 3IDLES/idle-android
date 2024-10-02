package com.idle.splash;

import com.idle.domain.usecase.auth.GetAccessTokenUseCase;
import com.idle.domain.usecase.auth.GetUserTypeUseCase;
import com.idle.domain.usecase.profile.GetCenterStatusUseCase;
import com.idle.domain.usecase.profile.GetMyCenterProfileUseCase;
import com.idle.domain.usecase.profile.GetMyWorkerProfileUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
  private final Provider<GetAccessTokenUseCase> getAccessTokenUseCaseProvider;

  private final Provider<GetUserTypeUseCase> getMyUserRoleUseCaseProvider;

  private final Provider<GetMyCenterProfileUseCase> getMyCenterProfileUseCaseProvider;

  private final Provider<GetMyWorkerProfileUseCase> getMyWorkerProfileUseCaseProvider;

  private final Provider<GetCenterStatusUseCase> getCenterStatusUseCaseProvider;

  public SplashViewModel_Factory(Provider<GetAccessTokenUseCase> getAccessTokenUseCaseProvider,
      Provider<GetUserTypeUseCase> getMyUserRoleUseCaseProvider,
      Provider<GetMyCenterProfileUseCase> getMyCenterProfileUseCaseProvider,
      Provider<GetMyWorkerProfileUseCase> getMyWorkerProfileUseCaseProvider,
      Provider<GetCenterStatusUseCase> getCenterStatusUseCaseProvider) {
    this.getAccessTokenUseCaseProvider = getAccessTokenUseCaseProvider;
    this.getMyUserRoleUseCaseProvider = getMyUserRoleUseCaseProvider;
    this.getMyCenterProfileUseCaseProvider = getMyCenterProfileUseCaseProvider;
    this.getMyWorkerProfileUseCaseProvider = getMyWorkerProfileUseCaseProvider;
    this.getCenterStatusUseCaseProvider = getCenterStatusUseCaseProvider;
  }

  @Override
  public SplashViewModel get() {
    return newInstance(getAccessTokenUseCaseProvider.get(), getMyUserRoleUseCaseProvider.get(), getMyCenterProfileUseCaseProvider.get(), getMyWorkerProfileUseCaseProvider.get(), getCenterStatusUseCaseProvider.get());
  }

  public static SplashViewModel_Factory create(
      Provider<GetAccessTokenUseCase> getAccessTokenUseCaseProvider,
      Provider<GetUserTypeUseCase> getMyUserRoleUseCaseProvider,
      Provider<GetMyCenterProfileUseCase> getMyCenterProfileUseCaseProvider,
      Provider<GetMyWorkerProfileUseCase> getMyWorkerProfileUseCaseProvider,
      Provider<GetCenterStatusUseCase> getCenterStatusUseCaseProvider) {
    return new SplashViewModel_Factory(getAccessTokenUseCaseProvider, getMyUserRoleUseCaseProvider, getMyCenterProfileUseCaseProvider, getMyWorkerProfileUseCaseProvider, getCenterStatusUseCaseProvider);
  }

  public static SplashViewModel newInstance(GetAccessTokenUseCase getAccessTokenUseCase,
      GetUserTypeUseCase getMyUserRoleUseCase, GetMyCenterProfileUseCase getMyCenterProfileUseCase,
      GetMyWorkerProfileUseCase getMyWorkerProfileUseCase,
      GetCenterStatusUseCase getCenterStatusUseCase) {
    return new SplashViewModel(getAccessTokenUseCase, getMyUserRoleUseCase, getMyCenterProfileUseCase, getMyWorkerProfileUseCase, getCenterStatusUseCase);
  }
}
