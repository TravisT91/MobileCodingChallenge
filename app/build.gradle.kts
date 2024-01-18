plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.hopskipdrive.mobileCodingChallenge"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.hopskipdrive.mobileCodingChallenge"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildFeatures{
        viewBinding = true
    }
    secrets {
        propertiesFileName = "secrets.properties"
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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val activityVersion = "1.8.2"
    val navVersion = "2.7.6"
    val lifeCycleVersion = "2.7.0"
    val retofitVersion = "2.9.0"
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.squareup.retrofit2:retrofit:$retofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retofitVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
    implementation("androidx.activity:activity-ktx:$activityVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifeCycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifeCycleVersion")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0-alpha13")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}