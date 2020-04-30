import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.plugins.ide.idea.model.Module

val mapstruct_version = "1.3.1.Final"
val hibernate_version = "5.4.10.Final"

plugins {
    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.spring") version "1.3.72"
    kotlin("plugin.jpa") version "1.3.72"

    id("org.jetbrains.kotlin.kapt").version("1.2.71")
    idea
}

group = "com.joshmlwood"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    kapt("org.mapstruct:mapstruct-processor:${mapstruct_version}")
//    annotationProcessor("org.mapstruct:mapstruct-processor:${mapstruct_version}")
    kapt("org.hibernate:hibernate-jpamodelgen:$hibernate_version")

    implementation("org.mapstruct:mapstruct:${mapstruct_version}")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun> {
    systemProperties = System.getProperties().mapKeysTo(mutableMapOf()) { it.key.toString() }
}
