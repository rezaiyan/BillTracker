plugins {
    kotlin("jvm") version "1.9.22" apply false
    kotlin("plugin.spring") version "1.9.22" apply false
    kotlin("plugin.jpa") version "1.9.22" apply false
    kotlin("multiplatform") version "1.9.22" apply false
    id("org.springframework.boot") version "3.5.3" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    id("org.jetbrains.compose") version "1.6.1" apply false
    id("com.android.application") version "8.1.0" apply false
    id("com.android.library") version "8.1.0" apply false
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

subprojects {
    if (name != "client") {
        apply(plugin = "org.jetbrains.kotlin.jvm")
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
