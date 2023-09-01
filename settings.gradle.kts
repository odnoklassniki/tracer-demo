import java.net.URI

pluginManagement {
    repositories {
        maven { setUrl("https://artifactory-external.vkpartner.ru/artifactory/maven/") }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { setUrl("https://artifactory-external.vkpartner.ru/artifactory/maven/") }
        google()
        mavenCentral()
    }
}

rootProject.name = "tracer-demo"
include(":app")
 