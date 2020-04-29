import com.jfrog.bintray.gradle.BintrayExtension
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("kapt")
    `maven-publish`
    id("org.jetbrains.dokka") version Versions.dokkaPlugin
    id("com.jfrog.bintray") version Versions.bintrayPlugin
}

group = Maven.groupId
version = Versions.name

dependencies {
    api(kotlin("stdlib"))

    implementation(Deps.moshi)
    kapt(Deps.moshiCodegen)

    testImplementation(Deps.mockk)
    testImplementation(Deps.assertk)
    testImplementation(Deps.junit5)
}

kapt {
    correctErrorTypes = true
}

val sourcesJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles sources JAR"
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

val dokka by tasks.getting(DokkaTask::class) {
    outputFormat = "html"
    outputDirectory = "$buildDir/javadoc"
}

val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    archiveClassifier.set("javadoc")
    from(dokka)
}

val publicationName = "sdp"
publishing {
    publications {
        create<MavenPublication>(publicationName) {
            groupId = Maven.groupId
            artifactId = Maven.artifactId
            version = Versions.name

            println("""
                    |Creating maven publication '$publicationName'
                    |    Group: $groupId
                    |    Artifact: $artifactId
                    |    Version: $version
                """.trimMargin())

            artifact(sourcesJar)
            artifact(dokkaJar)
            from(components["kotlin"])

            pom {
                name.set(Maven.name)
                description.set(Maven.desc)
                url.set(Maven.siteUrl)

                scm {
                    val scmUrl = "scm:git:${Maven.gitUrl}"
                    connection.set(scmUrl)
                    developerConnection.set(scmUrl)
                    url.set(this@pom.url)
                    tag.set("HEAD")
                }

                developers {
                    developer {
                        id.set("crow-misia")
                        name.set("Zenichi Amano")
                        email.set("crow.misia@gmail.com")
                        roles.set(listOf("Project-Administrator", "Developer"))
                        timezone.set("+9")
                    }
                }

                licenses {
                    license {
                        name.set(Maven.licenseName)
                        url.set(Maven.licenseUrl)
                        distribution.set(Maven.licenseDist)
                    }
                }
            }
        }
    }
}

fun findProperty(s: String) = project.findProperty(s) as String?
bintray {
    user = findProperty("bintray_user")
    key = findProperty("bintray_apikey")
    publish = true
    setPublications(publicationName)
    override=true
    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
        repo = "maven"
        name = Maven.name
        desc = Maven.desc
        setLicenses(*Maven.licenses)
        setLabels(*Maven.labels)
        issueTrackerUrl = Maven.issueTrackerUrl
        vcsUrl = Maven.gitUrl
        githubRepo = Maven.githubRepo
        description = Maven.desc
    })
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.6"
        freeCompilerArgs = listOf("-Xjsr305=strict", "-progressive")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
        events("passed", "skipped", "failed")
    }
}
