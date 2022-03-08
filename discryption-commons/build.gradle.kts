plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    js(IR) {
        nodejs()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "16"
        }
        withJava()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("com.fasterxml.jackson.core:jackson-core:2.13.1")
                implementation("com.fasterxml.jackson.core:jackson-databind:2.13.1")
                implementation("com.fasterxml.jackson.core:jackson-annotations:2.13.1")
                implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")
                implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.13.1")
                implementation("org.yaml:snakeyaml:1.30")
            }
        }
    }
}

