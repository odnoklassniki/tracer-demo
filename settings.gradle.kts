import java.net.URI

pluginManagement {
    repositories {
        maven { setUrl("https://artifactory-external.vkpartner.ru/artifactory/maven/") }
        google()
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { setUrl("https://artifactory-external.vkpartner.ru/artifactory/maven/") }
        google()
        mavenCentral()
        mavenLocal()
    }
}

rootProject.name = "tracer-demo"
include(":app")
 