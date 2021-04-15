plugins {
    kotlin("jvm") version Versions.kotlin apply false
}

allprojects {
    repositories {
        mavenCentral()
        maven { setUrl("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") }
    }
}

val clean by tasks.creating(Delete::class) {
    delete(rootProject.buildDir)
}
