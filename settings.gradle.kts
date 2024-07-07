pluginManagement {
    includeBuild("build-logic")

    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Care"
include(":app")
include(":core:domain")
include(":core:data")
include(":core:network")
include(":core:designsystem:binding")
include(":core:designsystem:compose")
include(":core:common-ui")
include(":presentation")
include(":feature:auth")
include(":feature:signup")
include(":feature:signin")
