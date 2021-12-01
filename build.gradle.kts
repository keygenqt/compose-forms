buildscript {

    val kotlinVersion: String by project
    val gradleVersion: String by project

    extra.apply {
        set("google_accompanist_version", "0.20.0")
        set("material3_version", "1.0.0-alpha01")
    }
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:$gradleVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

plugins {
    id("com.diffplug.spotless")
}

subprojects {
    apply(plugin = "com.diffplug.spotless")

    spotless {
        kotlin {
            target("**/*.kt")
            licenseHeaderFile(file("${project.rootDir}/spotless/LicenseHeader"))
        }
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}