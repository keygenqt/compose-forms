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

dependencies {
    implementation("androidx.compose.ui:ui:1.1.0-rc01")
    implementation("androidx.compose.material:material:1.1.0-rc01")
    implementation("com.google.accompanist:accompanist-insets:0.21.5-rc")
    implementation("com.google.accompanist:accompanist-insets-ui:0.21.5-rc")
    implementation("androidx.compose.material3:material3:1.0.0-alpha02")
}