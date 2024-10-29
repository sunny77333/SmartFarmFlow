import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.smartfarmflow"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.smartfarmflow"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        buildFeatures {
            buildConfig = true
        }
        
        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())

        val apiKey = properties.getProperty("MAPS_API_KEY") ?: ""
        manifestPlaceholders["MAPS_API_KEY"] = apiKey
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
}

dependencies {
    androidTestImplementation ("androidx.benchmark:benchmark-macro-junit4:<version>")
    implementation ("com.google.firebase:firebase-perf")
    implementation ("com.google.firebase:firebase-database:20.0.3")
    implementation(platform("com.google.firebase:firebase-bom:33.5.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.android.gms:play-services-maps:18.0.2")
    implementation ("com.google.android.gms:play-services-location:21.0.1")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
