import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    alias(libs.plugins.detekt)
    alias(libs.plugins.dokka)
    alias(libs.plugins.dokka.javadoc)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlinx.kover)
    alias(libs.plugins.maven.publish)
    id("signing")
}

group = Maven.GROUP_ID
version = Maven.VERSION

dependencies {
    compileOnly(platform(libs.kotlin.bom))
    compileOnly(libs.kotlin.stdlib)

    testImplementation(platform(libs.kotest.bom))
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.property)
    testImplementation(libs.mockk)
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

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
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

tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
    jvmTarget = "1.8"
    exclude("build/")
    exclude("resources/")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
        events("passed", "skipped", "failed")
    }
}

val dokkaJavadocJar by tasks.registering(Jar::class) {
    description = "A Javadoc JAR containing Dokka Javadoc"
    archiveClassifier = "javadoc"
    from(tasks.dokkaGeneratePublicationJavadoc.flatMap { it.outputDirectory })
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}

mavenPublishing {
    configure(KotlinJvm(
        javadocJar = JavadocJar.Dokka("dokkaJavadocJar"),
        sourcesJar = true,
    ))

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    coordinates(Maven.GROUP_ID, Maven.ARTIFACT_ID, Maven.VERSION)

    pom {
        name = Maven.ARTIFACT_ID
        description = Maven.DESCRIPTION
        url = "https://github.com/${Maven.GITHUB_REPOSITORY}/"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "crow-misia"
                name = "Zenichi Amano"
                email = "crow.misia@gmail.com"
            }
        }
        scm {
            url = "https://github.com/${Maven.GITHUB_REPOSITORY}/"
            connection = "scm:git:git://github.com/${Maven.GITHUB_REPOSITORY}.git"
            developerConnection = "scm:git:ssh://git@github.com/${Maven.GITHUB_REPOSITORY}.git"
        }
    }
}
