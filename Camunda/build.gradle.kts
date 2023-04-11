plugins {
    java
    id("org.springframework.boot") version "2.7.10"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("https://artifacts.camunda.com/artifactory/public")
    }
}

dependencies {
    implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter-rest:7.18.0")
    implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter-webapp:7.18.0")
    implementation("org.camunda.bpm:camunda-engine-plugin-spin:7.18.0")
    implementation("org.camunda.bpm.run:camunda-bpm-run-modules-swaggerui:7.18.0")
    implementation("org.camunda.bpm:camunda-engine-rest-openapi:7.18.0")
    implementation("org.camunda.spin:camunda-spin-dataformat-all:1.18.1")
    implementation("com.h2database:h2:2.1.214")
    implementation("org.springframework.boot:spring-boot-starter-jdbc:2.7.10")
    implementation("org.springframework.boot:spring-boot-devtools:2.7.10")
    implementation("com.google.code.gson:gson:2.10.1")
    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    testCompileOnly("org.projectlombok:lombok:1.18.26")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.26")
}

group = "de.ur.location"
version = "1.0.0-SNAPSHOT"
description = "LocationCase"
java.sourceCompatibility = JavaVersion.VERSION_17

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
