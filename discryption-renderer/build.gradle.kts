import org.gradle.internal.os.OperatingSystem

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    application
}

val skijaPlatform = when(OperatingSystem.current()) {
    OperatingSystem.MAC_OS -> "macos-x64"
    OperatingSystem.LINUX -> "linux"
    OperatingSystem.WINDOWS -> "windows"
    else -> {
        throw IllegalStateException("Unsupported operating system")
    }
}

dependencies {
    implementation(project(":discryption-commons"))
    implementation("ch.qos.logback:logback-classic:1.3.0-alpha12")
    implementation("org.jetbrains.skija:skija-shared:0.93.1")
    implementation("org.jetbrains.skija:skija-$skijaPlatform:0.93.1")
}
