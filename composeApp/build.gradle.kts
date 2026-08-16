import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.secrets)
    alias(libs.plugins.google.services) apply false
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.retrofit)
            implementation(libs.converter.moshi)
            implementation(libs.okhttp)
            implementation(libs.logging.interceptor)
            
            // Android-only logic dependencies
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.room.ktx)
            implementation(libs.moshi.kotlin)
            implementation(platform(libs.firebase.bom))
            implementation(libs.firebase.ai)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)
            
            // Multiplatform lifecycle
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)
            
            implementation(libs.kotlinx.coroutines.core)
        }
        iosMain.dependencies {
        }
        wasmJsMain.dependencies {
        }
    }
}

compose.resources {
    publicResClass = true
}

android {
    namespace = "com.nezco.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nezco.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    
    signingConfigs {
        getByName("debug") {
            storeFile = file("debug.keystore")
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

// FIX 1: Aplica google-services solo si existe el archivo real o no se pidió omitirlo (CI usa -Pandroid.skip.google.services=true)
val skipGoogleServices = project.hasProperty("android.skip.google.services")
if (!skipGoogleServices && file("google-services.json").exists()) {
    apply(plugin = "com.google.gms.google-services")
}

secrets {
    propertiesFileName = ".env"
    defaultPropertiesFileName = ".env.example"
    ignoreList.add("FIREBASE_APPCHECK_DEBUG_TOKEN")
}

dependencies {
    debugImplementation(compose.uiTooling)
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspAndroid", libs.moshi.kotlin.codegen)
}

/**
 * HALLAZGOS DE SEGURIDAD / ARQUITECTURA (TODOs):
 * 1. [ALTO] signingConfig de release usa la de debug y isMinifyEnabled = false. Activar R8 y usar llaves reales en CI.
 * 2. [ALTO] GEMINI_API_KEY expuesta en BuildConfig. Mover a un backend proxy para mayor seguridad.
 * 3. [MEDIO] Limpiar dependencias muertas (google-services, firestore, etc.) que no se usen.
 */

