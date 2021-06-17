plugins {
    kotlin("jvm") version Versions.kotlin apply false
}

allprojects {
    repositories {
        mavenCentral()
    }
}

val clean by tasks.creating(Delete::class) {
    delete(rootProject.buildDir)
}
