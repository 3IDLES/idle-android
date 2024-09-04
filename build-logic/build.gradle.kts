plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.compiler.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidHilt") {
            id = "care.android.hilt"
            implementationClass = "com.idle.app.HiltAndroidPlugin"
        }
        register("kotlinHilt") {
            id = "care.kotlin.hilt"
            implementationClass = "com.idle.app.HiltKotlinPlugin"
        }
    }
}
