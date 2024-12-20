tracer-demo
=======

Tracer - это сервис от OK.Tech для сбора и анализа ошибок в мобильных приложениях под iOS и Android

## Регистрация и настройка

Для начала нужно:
1. Зайти в аккаунт Tracer.
2. Создать или вступить в организацию.
3. Добавить андроид проект (нужно быть администратором или владельцем организации).

## Подключение зависимостей на трейсер к проекту

**ВАЖНО!** При каждом обновлении версии SDK настоятельно рекомендуется делать `clean build`.

В вашем `<project>/settings.gradle.kts` добавьте репозитории с плагином трейсера и рантаймом трейсера.
~~~kotlin
pluginManagement {
    repositories {
        // другие репозитории c вашими зависимостями
        maven { setUrl("https://artifactory-external.vkpartner.ru/artifactory/maven/") }
    }
}
dependencyResolutionManagement {
    repositories {
        // другие репозитории c вашими зависимостями
        maven { setUrl("https://artifactory-external.vkpartner.ru/artifactory/maven/") }
    }
}
~~~

В вашем `<project>/<app-module>/build.gradle.kts`
~~~kotlin
plugins {
    id("ru.ok.tracer").version("1.0.0")
}

tracer {
    create("defaultConfig") {
        // См. в разделе "Настройки"
        pluginToken = "PLUGIN_TOKEN"
        appToken = "APP_TOKEN"

        // Включает загрузку маппингов для билда. По умолчанию включена
        uploadMapping = true

        // Включает загрузку отладочной информации из native-библиотек для обработки нативных крешей
        // Обрабатывает всё, что попадает в build/intermediates/merged_native_libs
        // т.е. модули с NDK-кодом, библиотеки из зависимостей, библиотеки из jniLibs, ...
        // Не загружает отладочную информацию для библиотек, у которых нет отладочной информации
        // 
        // Без сборщика нативных крешей включать не очень осмысленно
        // По умолчанию выключено
        uploadNativeSymbols = true

        // Передаёт дополнительный путь с native-библиотеками в загрузчик
        // Отладочная информация с библиотек, содержащихся в нем, "перекрывает" ту, что
        // собирается из build/intermediates/merged_native_libs
        // По умолчанию null
        additionalLibrariesPath = projectDir.toString() + "/aVeryNonstandardLibsDirectory"
    }

    // Также можно задавать конфигурацию для каждого flavor, buildType, buildVariant.
    // Конфигурации наследуют defaultConfig.
    create("debug") {
        // Параметры...
    }
    create("demoDebug") {
        // Параметры...
    }
}

dependencies {
    implementation(platform("ru.ok.tracer:tracer-platform:1.0.0"))

    // Плагины независимы друг от друга. Можно подключать только те,
    // которые необходимы в данный момент.

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
}
~~~

## Включение плагинов трейсера и их конфигурация в проекте

 В вашем `Application.kt` реализуйте интерфейс `HasTracerConfiguration`
~~~kotlin
class MyApplication : Application(), HasTracerConfiguration {
    override val tracerConfiguration: List<TracerConfiguration>
        get() = listOf(
            CoreTracerConfiguration.build {
                // опции ядра трейсера
            },
            CrashReportConfiguration.build {
                // опции сборщика крэшей
            },
            CrashFreeConfiguration.build {
                // опции подсчета crash free
            },
            HeapDumpConfiguration.build {
                // опции сборщика хипдампов при ООМ
            },
            DiskUsageConfiguration.build {
                // опции анализатора дискового пространства
            },
            SystraceProfilerConfiguration.build {
                // опции systrace-профайлера в продакшене
            },
            SamplingProfilerConfiguration.build {
                // опции семплирующего профайлера
            },
        )
}
~~~

Проперти `HasTracerConfiguration.tracerConfiguration` будет запрошена ровно один раз при старте процесса до вызова `Application.onCreate`, но после `Application.attachBaseContext`. В геттере уже можно обращаться к контексту приложения, но еще рано обращаться к тому, что проинициализируется в `onCreate`.

## Описание `CoreTracerConfiguration`

В том редком случае, когда вы захотите сконфигурировать ядро трейсера в вашем проекте, нужно вернуть `CoreTracerConfiguration` из `HasTracerConfiguration`

~~~kotlin
class MyApplication : Application(), HasTracerConfiguration {
    override val tracerConfiguration: List<TracerConfiguration>
        get() = listOf(
            CoreTracerConfiguration.build {
                // ваши опции
            },
        )
}
~~~

Опции `CoreTracerConfiguration.Builder`:

| Опция                                               | Описание                                                                                                                   |
|-----------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------|
| `setApiUrl` или `provideApiUrl`                     | Изменяет адреса api Tracer-а. Используйте с умом.                                                                          |
| `setOverrideAppToken` или `provideOverrideAppToken` | Переопределяет `appToken` из конфигурации gradle-плагина. Используйте с умом.                                              |
| `setDebugUpload`                                    | Отвечает за включение/выключение загрузки данных в дебажной сборке приложения. По умолчанию загрузка данных **выключена**. |
