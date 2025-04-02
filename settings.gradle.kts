pluginManagement {
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

rootProject.name = "RickAndMorty"
include(":app")
include(":common")
include(":common:common-core")
include(":common:common-ui")
include(":common:common-exceptions")
include(":network")
include(":network:network-core")
include(":navigation")
include(":navigation:navigation-core")
include(":feature")
include(":feature:feature-characters")
