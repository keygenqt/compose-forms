plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    `maven-publish`
    id("com.jfrog.artifactory")
}

// dependencies versions
val composeVersion: String by project
val googleAccompanistVersion: String by project
val material3Version: String by project

// lib info
version = "0.0.15"
group = "ru.surfstudio.compose"

publishing {
    publications {
        register("aar", MavenPublication::class) {
            groupId = group.toString()
            artifactId = project.name
            artifact("$buildDir/outputs/aar/compose-forms-$version-release.aar")
        }
    }
}

artifactory {
    setContextUrl("https://artifactory.surfstudio.ru/artifactory")
    publish {
        repository {
            setRepoKey("libs-release-local")
            setUsername(findProperty("surf_maven_username").toString())
            setPassword(findProperty("surf_maven_password").toString())
        }
        defaults {
            publications("aar")
            setPublishArtifacts(true)
           // setPublishPom(true)
        }
    }
}

android {

    compileSdk = 30

    defaultConfig {
        minSdk = 23
        targetSdk = 31
        setProperty("archivesBaseName", "compose-forms-$version")
    }

    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
    }

    buildFeatures {
        compose = true
    }

    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    //todo use EmojiUtils instead
    implementation("com.vdurmont:emoji-java:5.1.1")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("com.google.accompanist:accompanist-insets:$googleAccompanistVersion")
    implementation("com.google.accompanist:accompanist-insets-ui:$googleAccompanistVersion")
    implementation("androidx.compose.material3:material3:$material3Version")
}