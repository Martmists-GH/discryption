import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    kotlin("jvm") version "1.6.10" apply false
    kotlin("multiplatform") version "1.6.10" apply false
    kotlin("plugin.serialization") version "1.6.10" apply false

    id("com.github.johnrengelman.shadow") version "7.1.2" apply false

    id("com.github.ben-manes.versions") version "0.42.0"
    id("se.patrikerdes.use-latest-versions") version "0.2.18"

}

allprojects {
    tasks.withType<DependencyUpdatesTask> {
        fun isNonStable(version: String): Boolean {
            val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
            val regex = "^[0-9,.v-]+(-r)?$".toRegex()
            val isStable = stableKeyword || regex.matches(version)
            return isStable.not()
        }

        rejectVersionIf {
            isNonStable(candidate.version) && !isNonStable(currentVersion)
        }
    }
}

subprojects {
    group = "com.martmists"

    this.buildDir = File(rootProject.buildDir.absolutePath + "/" + name).also {
        it.mkdirs()
    }

    repositories {
        mavenCentral()
        google()
        maven("https://maven.martmists.com/releases/")
        maven("https://repo1.maven.org/maven2")
        maven("https://jitpack.io")
        maven("https://packages.jetbrains.team/maven/p/skija/maven")
    }
}
