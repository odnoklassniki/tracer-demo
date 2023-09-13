tracer-demo
=======

Tracer - это сервис от OK.Tech для сбора и анализа ошибок в мобильных приложениях под iOS и Android

## Регистрация и настройка

Для начала нужно:
1. Зайти в аккаунт Tracer.
2. Создать или вступить в организацию.
3. Добавить андроид проект (нужно быть администратором или владельцем организации).

## Подключение зависимостей на трейсер к проекту

В вашем `<project>/settings.gradle`
~~~groovy
pluginManagement {
    repositories {
        maven { url 'https://artifactory-external.vkpartner.ru/artifactory/maven/' }
    }
}
dependencyResolutionManagement {
    repositories {
        maven { url 'https://artifactory-external.vkpartner.ru/artifactory/maven/' }
    }
}
~~~

В вашем `<project>/<app-module>/build.gradle`
~~~groovy
plugins {
    id 'ru.ok.tracer' version '0.2.7'
}

tracer {
    defaultConfig {
         // См. в разделе "Настройки"
        pluginToken = "PLUGIN_TOKEN"
        appToken = "APP_TOKEN"

        // Включает загрузку маппингов для билда. По умолчанию включена
        uploadMapping = true
    }

    // Также можно задавать конфигурацию для каждого flavor, buildType, buildVariant.
    // Конфигурации наследуют defaultConfig.
    debug {
        // Параметры...
    }
    demoDebug {
        // Параметры...
    }
}

dependencies {
    // Плагины независимы друг от друга. Можно подключать только те,
    // которые необходимы в данный момент.

    // Сбор и анализ крешей и ANR
    implementation "ru.ok.tracer:tracer-crash-report:0.2.7"
    // Сбор и анализ нативных крешей
    implementation "ru.ok.tracer:tracer-crash-report-native:0.2.7"
    // Сбор и анализ хипдапмов при OOM
    implementation "ru.ok.tracer:tracer-heap-dumps:0.2.7"
    // Анализ потребления дискового места на устройстве
    implementation "ru.ok.tracer:tracer-disk-usage:0.2.7"
    // Семплирующий профайлер
    implementation "ru.ok.tracer:tracer-profiler-sampling:0.2.7"
     // Систрейс
    implementation "ru.ok.tracer:tracer-profiler-systrace:0.2.7"
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

Проперти `HasTracerConfiguration.tracerConfiguration` будет запрошена ровно один раз при старте процесса до вызова `Application.onCreate` но после `Application.attachBaseContext`. В геттере уже можно обращаться к контексту приложения, но еще рано обращаться к тому, что проинициализируется в `onCreate`.

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
- Таких нет )) Все работает из коробки

Устаревшие или опасные опции `CoreTracerConfiguration.Builder`:
- `setEnabled` — не используется и будет удалена в версии 0.3.x. Ядро трейсера всегда включено, но не активно пока нет включенных плагинов
- `setHost`, `provideHost` — изменение адреса трейсера
- `setStatHost`, `provideStatHost` — изменение адреса трейсера для фичи crash free
- `setCustomAppKey`, `provideCustomAppKey` — заменяет `sampleUploadToken` из конфигурации gradle-плагина
