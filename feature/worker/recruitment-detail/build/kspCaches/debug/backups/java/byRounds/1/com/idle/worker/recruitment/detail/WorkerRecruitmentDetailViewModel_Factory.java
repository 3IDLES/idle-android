package com.idle.worker.recruitment.detail;

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
public final class WorkerRecruitmentDetailViewModel_Factory implements Factory<WorkerRecruitmentDetailViewModel> {
    public static WorkerRecruitmentDetailViewModel_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static WorkerRecruitmentDetailViewModel newInstance() {
        return new WorkerRecruitmentDetailViewModel();
    }

    @Override
    public WorkerRecruitmentDetailViewModel get() {
        return newInstance();
    }

    private static final class InstanceHolder {
        private static final WorkerRecruitmentDetailViewModel_Factory INSTANCE = new WorkerRecruitmentDetailViewModel_Factory();
    }
}
