plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.diffplug.spotless")
}

version = "0.0.20"
group = "com.keygenqt.forms"

spotless {
    kotlin {
        target("**/*.kt")
        licenseHeaderFile("$buildDir/../LICENSE")
    }
}

android {

    compileSdk = 33

    defaultConfig {
        minSdk = 23
        targetSdk = 33
        setProperty("archivesBaseName", "compose-forms-$version")
    }

    composeOptions {
        kotlinCompilerExtensionVersion = findProperty("composeCompilerVersion").toString()
    }

    buildFeatures {
        compose = true
    }

    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }
}

// https://developer.android.com/jetpack/androidx/releases/compose
val composeVersion = "1.2.1"
// https://developer.android.com/jetpack/androidx/releases/compose-material3
val material3Version = "1.0.0-rc01"

dependencies {
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material3:material3:$material3Version")
}