plugins {
    alias(libs.plugins.android.application)
}

// URL base de la API. En el emulador, 10.0.2.2 apunta al equipo anfitrión.
// En un dispositivo físico se puede sobrescribir con:
//   ./gradlew assembleDebug -PapiBaseUrl=http://IP_DEL_EQUIPO:8085/api/v1/apirest/
val apiBaseUrl = providers.gradleProperty("apiBaseUrl")
    .orElse("http://10.0.2.2:8085/api/v1/apirest/")

android {
    namespace = "edu.sv.ues.dam135.guiaapirest"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "edu.sv.ues.dam135.guiaapirest"
        minSdk = 30
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "API_BASE_URL", "\"${apiBaseUrl.get()}\"")
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    implementation(libs.recyclerview)

    // Consumo de la API REST
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.gson)
    implementation(libs.okhttp.logging)

    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}
