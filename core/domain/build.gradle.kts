plugins {
    id("care.kotlin.library")
    id("care.kotlin.hilt")
}

kotlin {
    jvmToolchain(17) // JVM 17로 설정
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation(libs.coroutines.core)
}
