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

include(":core:data")
include(":core:network")
include(":core:datastore")

include(":core:domain")

include(":presentation")
include(":core:designsystem:binding")
include(":core:designsystem:compose")
include(":core:common-ui:compose")
include(":core:common-ui:binding")
include(":core:designresource")
include(":core:analytics")

include(":feature:auth")
include(":feature:signup")
include(":feature:signin")
include(":feature:setting")
include(":feature:postcode")
include(":feature:worker:home")
include(":feature:job-posting-detail")
include(":feature:worker:profile")
include(":feature:center:home")
include(":feature:center:profile")
include(":feature:center:register-info")
include(":feature:center:job-posting-post")
include(":feature:center:job-posting-edit")
include(":feature:center:applicant-inquiry")
include(":feature:withdrawal")
include(":feature:worker:job-posting")
include(":feature:center:pending")
include(":feature:notification")
