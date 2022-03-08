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
                implementation(project(":discryption-commons"))
                implementation("io.ktor:ktor-client-core:1.6.7")
                implementation("io.ktor:ktor-client-serialization:1.6.7")
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(project(":discryption-commons"))
                implementation("io.ktor:ktor-client-js:1.6.7")
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(project(":discryption-commons"))
                implementation("io.ktor:ktor-client-cio:1.6.7")
            }
        }
    }
}

