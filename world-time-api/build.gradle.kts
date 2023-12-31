plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
    id("signing")
}

android {
    namespace = "vn.alphalabs.worldtimeapi"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

tasks {
    val sourceFiles = android.sourceSets.getByName("main").java.srcDirs

    register<Javadoc>("withJavadoc") {
        isFailOnError = false

        // the code needs to be compiled before we can create the Javadoc
        dependsOn(android.libraryVariants.toList().last().javaCompileProvider)

        if (! project.plugins.hasPlugin("org.jetbrains.kotlin.android")) {
            setSource(sourceFiles)
        }

        // add Android runtime classpath
        android.bootClasspath.forEach { classpath += project.fileTree(it) }

        // add classpath for all dependencies
        android.libraryVariants.forEach { variant ->
            variant.javaCompileProvider.get().classpath.files.forEach { file ->
                classpath += project.fileTree(file)
            }
        }

        // We don't need javadoc for internals.
        exclude("**/internal/*")

        // Append Java 8 and Android references
        val options = options as StandardJavadocDocletOptions
        options.links("https://developer.android.com/reference")
        options.links("https://docs.oracle.com/javase/8/docs/api/")

        // Workaround for the following error when running on on JDK 9+
        // "The code being documented uses modules but the packages defined in ... are in the unnamed module."
        if (JavaVersion.current() >= JavaVersion.VERSION_1_9) {
            options.addStringOption("-release", "8")
        }
    }

    register<Jar>("withJavadocJar") {
        archiveClassifier.set("javadoc")
        dependsOn(named("withJavadoc"))
        val destination = named<Javadoc>("withJavadoc").get().destinationDir
        from(destination)
    }

    register<Jar>("withSourcesJar") {
        archiveClassifier.set("sources")
        from(sourceFiles)
    }
}

afterEvaluate {
    fun Project.getRepositoryUrl(): java.net.URI {
        val isReleaseBuild = properties["POM_VERSION_NAME"]?.toString()?.contains("SNAPSHOT") == false
        val releaseRepoUrl = properties["RELEASE_REPOSITORY_URL"]?.toString() ?: "https://s01.oss.sonatype.org/content/repositories/releases/"
        val snapshotRepoUrl = properties["SNAPSHOT_REPOSITORY_URL"]?.toString() ?: "https://s01.oss.sonatype.org/content/repositories/snapshots/"
        return uri(if (isReleaseBuild) releaseRepoUrl else snapshotRepoUrl)
    }

    publishing {
        publications {
            val props = project.properties

            // 1. configure repositories
            repositories {
                maven {
                    url = getRepositoryUrl()
                    // credentials are stored in ~/.gradle/gradle.properties with ~ being the path of the home directory
                    credentials {
                        username = props["ossUsername"]?.toString() ?: throw IllegalStateException("oss.username not found")
                        password = props["ossPassword"]?.toString() ?: throw IllegalStateException("oss.password not found")
                    }
                }
            }

            // 2. configure publication
            val publicationName = props["POM_NAME"]?.toString() ?: "publication"
            create<MavenPublication>(publicationName) {
                from(project.components["release"])

                artifact(tasks.named<Jar>("withJavadocJar"))
                tasks.named("generateMetadataFileForVn.alphalabsPublication") {
                    dependsOn("withSourcesJar")
                }

                pom {
                    groupId = props["POM_GROUP_ID"].toString()
                    artifactId = props["POM_ARTIFACT_ID"].toString()
                    version = props["POM_VERSION_NAME"].toString()

                    name.set(props["POM_NAME"].toString())
                    description.set(props["POM_DESCRIPTION"].toString())
                    url.set(props["POM_URL"].toString())
                    packaging = props["POM_PACKAGING"].toString()

                    scm {
                        url.set(props["POM_SCM_URL"].toString())
                        connection.set(props["POM_SCM_CONNECTION"].toString())
                        developerConnection.set(props["POM_SCM_DEV_CONNECTION"].toString())
                    }

                    organization {
                        name.set(props["POM_COMPANY_NAME"].toString())
                        url.set(props["POM_COMPANY_URL"].toString())
                    }

                    developers {
                        developer {
                            id.set(props["POM_DEVELOPER_ID"].toString())
                            name.set(props["POM_DEVELOPER_NAME"].toString())
                            email.set(props["POM_DEVELOPER_EMAIL"].toString())
                        }
                    }

                    licenses {
                        license {
                            name.set(props["POM_LICENCE_NAME"].toString())
                            url.set(props["POM_LICENCE_URL"].toString())
                            distribution.set(props["POM_LICENCE_DIST"].toString())
                        }
                    }
                }
            }

            // 3. sign the artifacts
//            signing {
//                // in-memory signing (only required when publishing with a build pipeline)
//                val signingKeyId = props["signingKeyId"]?.toString()
//                    ?: throw IllegalStateException("signingKeyId not found")
//                val signingKeyPassword = props["signingKeyPassword"]?.toString()
//                    ?: throw IllegalStateException("signingKeyPassword not found")
//                val signingKey = props["signingKey"]?.toString()
//                    ?: throw IllegalStateException("signingKey not found")
//                useInMemoryPgpKeys(signingKeyId, signingKey, signingKeyPassword)
//
//                sign(publishing.publications.getByName(publicationName))
//            }
        }
    }
}