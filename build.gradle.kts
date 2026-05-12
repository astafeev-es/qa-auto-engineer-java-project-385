import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("java")
    id("com.github.ben-manes.versions") version "0.53.0"
    id("checkstyle")
    id("org.sonarqube") version "7.2.3.7755"
    id("io.qameta.allure") version "2.12.0"
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
    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.seleniumhq.selenium:selenium-java:4.27.0")
    implementation("org.apache.commons:commons-lang3:3.17.0")
    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("ch.qos.logback:logback-classic:1.5.16")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("io.qameta.allure:allure-junit5:2.29.1")
}

allure {
    version.set("2.29.1")
    adapter {
        aspectjVersion.set("1.9.22.1")
    }
}

tasks.test {
    useJUnitPlatform()
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
