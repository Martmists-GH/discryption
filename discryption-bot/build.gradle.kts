plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    application
}

dependencies {
    implementation(project(":discryption-commons"))
    implementation(project(":discryption-client"))
    implementation("ch.qos.logback:logback-classic:1.3.0-alpha12")
}
