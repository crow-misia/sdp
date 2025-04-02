plugins {
    id("jp.co.gahojin.refreshVersions") version "0.1.4"
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
