plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    application
}

kotlin {
    js(IR) {
        binaries.executable()
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
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(project(":discryption-renderer"))

                // Kotlinx libraries
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")

                // Ktor server
                implementation("io.ktor:ktor-server-core:1.6.7")
                implementation("io.ktor:ktor-server-tomcat:1.6.7")
                implementation("io.ktor:ktor-server-sessions:1.6.7")
                implementation("io.ktor:ktor-auth:1.6.7")
                implementation("io.ktor:ktor-html-builder:1.6.7")
                implementation("io.ktor:ktor-serialization:1.6.7")

                // Ktor client
                implementation("io.ktor:ktor-client-core:1.6.7")
                implementation("io.ktor:ktor-client-cio:1.6.7")
                implementation("io.ktor:ktor-client-serialization:1.6.7")

                // Html Builder
                implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.3")

                // Database libraries
                implementation("org.jetbrains.exposed:exposed-core:0.37.3")
                implementation("org.jetbrains.exposed:exposed-jdbc:0.37.3")
                implementation("org.jetbrains.exposed:exposed-java-time:0.37.3")

                // Database drivers
                implementation("org.xerial:sqlite-jdbc:3.36.0.3")
                implementation("org.postgresql:postgresql:42.3.1")

                // Logging
                implementation("ch.qos.logback:logback-classic:1.3.0-alpha12")
            }
        }

        val jsMain by getting {
            dependencies {
                // React Kotlin
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react:17.0.2-pre.290-kotlin-1.6.10")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:17.0.2-pre.290-kotlin-1.6.10")

                // Sass tools
                implementation(devNpm("sass", "^1.48.0"))
                implementation(devNpm("sass-loader", "^7.1.0"))
            }
        }
    }
}


val sass = tasks.register<Copy>("sass"){
    destinationDir = file("${rootProject.buildDir.absolutePath}/js/node_modules/frontend_sass")
    from("src/jsMain/sass")
}

tasks.named("compileKotlinJs") {
    dependsOn(sass)
}

tasks.named<Copy>("jvmProcessResources") {
    val output = tasks.getByName("jsBrowserDistribution")

    dependsOn(output)

    into("static/js") {
        from(output)
    }
}
