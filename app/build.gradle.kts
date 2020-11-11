val kotlin_version: String = "1.4.10"

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    id("kotlin-android")
}

android {
    compileSdkVersion (30)
    buildToolsVersion ("30.0.2")

    defaultConfig {
        applicationId= "com.dysen.widgets"
        minSdkVersion (21)
        targetSdkVersion (30)
        versionCode =1
        versionName ="1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    // Configure Java compiler compatible with Java 1.8
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    // Configure Kotlin compiler target Java 1.8 when compile Kotlin to bytecode
    kotlinOptions {
        this as org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
        jvmTarget = "1.8"
    }

    packagingOptions {
        exclude("META-INF/*.kotlin_module")
    }
}

dependencies {

    implementation ("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
    implementation ("androidx.core:core-ktx:1.3.2")
    implementation ("androidx.appcompat:appcompat:1.2.0")
    implementation ("com.google.android.material:material:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation(project(mapOf("path" to ":MeWidgets")))
    testImplementation ("junit:junit:4.+")
    androidTestImplementation ("androidx.test.ext:junit:1.1.2")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.3.0")

}