plugins {
    id("com.android.application")
    kotlin("android")
    id("ru.ok.tracer") version("1.0.2")
}

android {
    namespace = "ru.ok.tracer.demo"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.ok.tracer.demo"
        minSdk = 24
        targetSdk = 34

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
    implementation(platform("ru.ok.tracer:tracer-platform:1.0.2"))

    // Сбор и анализ крешей и ANR
    implementation("ru.ok.tracer:tracer-crash-report")
    // Сбор и анализ нативных крешей
    implementation("ru.ok.tracer:tracer-crash-report-native")
    // Сбор и анализ хипдапмов при OOM
    implementation("ru.ok.tracer:tracer-heap-dumps")
    // Анализ потребления дискового места на устройстве
    implementation("ru.ok.tracer:tracer-disk-usage")
    // Семплирующий профайлер
    implementation("ru.ok.tracer:tracer-profiler-sampling")
    // Систрейс
    implementation("ru.ok.tracer:tracer-profiler-systrace")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}