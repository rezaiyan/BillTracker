plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.9.10"
    id("org.jetbrains.compose") version "1.6.1"
}

dependencies {
    // Compose for Desktop
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation(compose.materialIconsExtended)
    
    // Kotlin coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    
    // HTTP client for API calls
    implementation("io.ktor:ktor-client-core:2.3.7")
    implementation("io.ktor:ktor-client-cio:2.3.7")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
    
    // JSON serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    
    // Logging
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    
    // Testing
    testImplementation(kotlin("test"))
}

compose.desktop {
    application {
        mainClass = "com.example.frontend.MainKt"
        
        nativeDistributions {
            targetFormats(org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg, 
                          org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi, 
                          org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb)
            packageName = "SubscriptionManagerUI"
            packageVersion = "1.0.0"
            
            windows {
                menuGroup = "Subscription Manager"
                upgradeUuid = "6b29b069-9087-4ee6-9b31-b32f85fa2be6"
            }
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}