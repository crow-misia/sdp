import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import java.net.URI

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.dokka)
    alias(libs.plugins.dokka.javadoc)
    alias(libs.plugins.kotlinx.kover)
    alias(libs.plugins.detekt)
    id("maven-publish")
    id("signing")
}

object Maven {
    const val groupId = "io.github.crow-misia.sdp"
    const val artifactId = "sdp"
    const val name = "sdp"
    const val desc = "SDP(Session Description Protocol) library"
    const val version = "1.4.0"
    const val siteUrl = "https://github.com/crow-misia/sdp"
    const val gitUrl = "https://github.com/crow-misia/sdp.git"
    const val githubRepo = "crow-misia/sdp"
    const val licenseName = "The Apache Software License, Version 2.0"
    const val licenseUrl = "https://www.apache.org/licenses/LICENSE-2.0.txt"
    const val licenseDist = "repo"
}

group = Maven.groupId
version = Maven.version

dependencies {
    compileOnly(platform(libs.kotlin.bom))
    compileOnly(libs.kotlin.stdlib)

    testImplementation(platform(libs.kotest.bom))
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.property)
    testImplementation(libs.mockk)
}

val sourcesJar by tasks.registering(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles sources JAR"
    archiveClassifier = "sources"
    from(sourceSets.main.get().allSource)
}

val dokkaJavadocJar by tasks.registering(Jar::class) {
    description = "A Javadoc JAR containing Dokka Javadoc"
    archiveClassifier = "javadoc"
    from(tasks.dokkaGeneratePublicationJavadoc.flatMap { it.outputDirectory })
}

publishing {
    publications {
        register<MavenPublication>("maven") {
            groupId = Maven.groupId
            artifactId = Maven.artifactId

            println("""
                |Creating maven publication
                |    Group: $groupId
                |    Artifact: $artifactId
                |    Version: $version
            """.trimMargin())

            artifact(sourcesJar)
            artifact(dokkaJavadocJar)
            from(components["kotlin"])

            pom {
                name = Maven.name
                description = Maven.desc
                url = Maven.siteUrl

                scm {
                    val scmUrl = "scm:git:${Maven.gitUrl}"
                    connection = scmUrl
                    developerConnection = scmUrl
                    url = Maven.gitUrl
                    tag = "HEAD"
                }

                developers {
                    developer {
                        id = "crow-misia"
                        name = "Zenichi Amano"
                        email = "crow.misia@gmail.com"
                        roles = listOf("Project-Administrator", "Developer")
                        timezone = "+9"
                    }
                }

                licenses {
                    license {
                        name = Maven.licenseName
                        url = Maven.licenseUrl
                        distribution = Maven.licenseDist
                    }
                }
            }
        }
    }
    repositories {
        maven {
            val releasesRepoUrl = URI("https://oss.sonatype.org/service/local/staging/deploy/maven2")
            val snapshotsRepoUrl = URI("https://oss.sonatype.org/content/repositories/snapshots")
            url = if (Maven.version.endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            credentials {
                username = providers.gradleProperty("sona.user").orElse(providers.environmentVariable("SONA_USER")).orNull
                password = providers.gradleProperty("sona.password").orElse(providers.environmentVariable("SONA_PASSWORD")).orNull
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}

detekt {
    parallel = true
    buildUponDefaultConfig = true
    allRules = false
    autoCorrect = true
    config.from(rootDir.resolve("config/detekt.yml"))
}

java {
    toolchain {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        languageVersion = JavaLanguageVersion.of(23)
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xconsistent-data-class-copy-visibility")
        javaParameters = true
        jvmTarget = JvmTarget.JVM_1_8
        apiVersion = KotlinVersion.KOTLIN_2_0
        languageVersion = KotlinVersion.KOTLIN_2_0
    }
}


tasks {
    withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        jvmTarget = "1.8"
        reports {
            html.required = false
            xml.required = false
            txt.required = false
            sarif.required = true
            md.required = true
        }
        exclude("build/")
        exclude("resources/")
    }

    withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
        jvmTarget = "1.8"
        exclude("build/")
        exclude("resources/")
    }

    withType<Test>().configureEach {
        useJUnitPlatform()
        testLogging {
            showStandardStreams = true
            events("passed", "skipped", "failed")
        }
    }
}
