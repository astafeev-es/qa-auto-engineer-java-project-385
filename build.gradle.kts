import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("java")
    id("com.github.ben-manes.versions") version "0.53.0"
    id("checkstyle")
    id("org.sonarqube") version "7.2.3.7755"
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.14.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.seleniumhq.selenium:selenium-java:4.43.0")
    implementation("org.apache.commons:commons-lang3:3.20.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    environment("APP_BASE_URL", providers.environmentVariable("APP_BASE_URL").getOrElse("http://localhost:5173"))
    environment("USERNAME", providers.environmentVariable("USERNAME").getOrElse("aleksei98"))
    environment("PASSWORD", providers.environmentVariable("PASSWORD").getOrElse("[trcktnghjtrn_98"))
}

tasks.withType<DependencyUpdatesTask> {
    fun isNonStable(version: String): Boolean {
        val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
        val regex = "^[0-9,.v-]+(-r)?$".toRegex()
        val isStable = stableKeyword || regex.matches(version)
        return isStable.not()
    }

    rejectVersionIf {
        isNonStable(candidate.version) && !isNonStable(currentVersion)
    }
}

checkstyle {
    toolVersion = "13.4.0"
    configFile = rootProject.file("config/checkstyle/checkstyle.xml")
}

sonar {
    properties {
        property("sonar.projectKey", "astafeev-es_qa-auto-engineer-java-project-385")
        property("sonar.organization", "astafeev-es")
    }
}
