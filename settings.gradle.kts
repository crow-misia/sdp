plugins {
    id("jp.co.gahojin.refreshVersions") version "0.2.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
}

refreshVersions {
    sortSection = true
}

rootProject.name = "sdp"
include("lib")
