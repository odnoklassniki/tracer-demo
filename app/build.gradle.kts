plugins {
    id("com.android.application")
    kotlin("android")
    id("ru.ok.tracer") version("0.3.1")
}

android {
    namespace = "ru.ok.tracer.demo"
    compileSdk = 33

    defaultConfig {
        applicationId = "ru.ok.tracer.demo"
        minSdk = 24
        targetSdk = 33

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
    }
    packaging.resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
}

tracer {
    create("defaultConfig") {
        // См. в разделе "Настройки"
        pluginToken = "YOUR_PLUGIN_TOKEN"
        appToken = "YOUR_APP_TOKEN"

        // Включает загрузку маппингов для билда. По умолчанию включена
        uploadMapping = true
    }
}

dependencies {
    // Плагины независимы друг от друга. Можно подключать только те,
    // которые необходимы в данный момент.

    // Сбор и анализ крешей и ANR
    implementation("ru.ok.tracer:tracer-crash-report:0.3.1")
    // Сбор и анализ нативных крешей
    implementation("ru.ok.tracer:tracer-crash-report-native:0.3.1")
    // Сбор и анализ хипдапмов при OOM
    implementation("ru.ok.tracer:tracer-heap-dumps:0.3.1")
    // Анализ потребления дискового места на устройстве
    implementation("ru.ok.tracer:tracer-disk-usage:0.3.1")
    // Семплирующий профайлер
    implementation("ru.ok.tracer:tracer-profiler-sampling:0.3.1")
    // Систрейс
    implementation("ru.ok.tracer:tracer-profiler-systrace:0.3.1")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}