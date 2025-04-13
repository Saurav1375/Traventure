import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)

}

android {
    namespace = "com.example.tripapplication"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.tripapplication"
        manifestPlaceholders["appAuthRedirectScheme"] = "tripapplication"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "CLIENT_SECRET", "\"${getApiKey("CLIENT_SECRET")}\"")

    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"https://adequate-margo-saurav5575-1fabb40e.koyeb.app/api/v1/\"")

        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL", "\"http://adequate-margo-saurav5575-1fabb40e.koyeb.app/api/v1/\"")


        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

fun getApiKey(name : String): String {
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())
    return properties.getProperty(name) ?: ""
}


dependencies {

    implementation(libs.bundles.compose)
    implementation(libs.androidx.ui.text.google.fonts)
    debugImplementation(libs.bundles.compose.debug)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation(libs.androidMap.sdk)
    implementation(libs.coil.compose)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.data.store)
    implementation(libs.appauth)
    implementation(libs.androidx.browser)
    implementation(libs.dotlottie.android)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}