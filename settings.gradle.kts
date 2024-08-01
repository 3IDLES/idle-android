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
        maven("https://repository.map.naver.com/archive/maven")
    }
}

rootProject.name = "Care"
include(":app")
include(":core:domain")
include(":core:data")
include(":core:network")
include(":core:datastore")
include(":core:designsystem:binding")
include(":core:designsystem:compose")
include(":core:common-ui:compose")
include(":core:common-ui:binding")
include(":presentation")
include(":feature:auth")
include(":feature:signup")
include(":feature:signin")
include(":feature:center:home")
include(":feature:center:profile")
include(":feature:worker:home")
include(":feature:center:setting")
include(":core:designresource")
include(":feature:worker:recruitment-detail")
include(":feature:worker:profile")
include(":feature:center:register-info")
include(":feature:postcode")
include(":feature:center:job-posting")
include(":feature:center:job-edit")
