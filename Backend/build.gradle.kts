plugins {
	java
	id("org.springframework.boot") version "4.0.2"
	id("io.spring.dependency-management") version "1.1.7"
}

val mapstructVersion = "1.5.5.Final" // MapStruct version
val testContainersVersion = "1.21.3" // TestContainers version
val swaggerOpenAPIVersion = "2.8.5" // Swagger OpenAPI version

group = "com.cocobambu"
version = "0.0.1-SNAPSHOT"
description = "Coco Bambu delivery api challenge"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// Spring Boot, Flyway, Hibernate, MapStruct (CORE), Swagger
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-flyway")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.flywaydb:flyway-database-postgresql")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${swaggerOpenAPIVersion}")
	implementation("org.mapstruct:mapstruct:${mapstructVersion}")

	// Dev Tools
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	// Annotation Processor (mapstruct)
	annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")

	// Driver Postgres
	runtimeOnly("org.postgresql:postgresql")

	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
	testImplementation("org.springframework.boot:spring-boot-starter-flyway-test")
	testImplementation("org.springframework.boot:spring-boot-starter-validation-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testImplementation(platform("org.testcontainers:testcontainers-bom:${testContainersVersion}"))
	testImplementation("org.testcontainers:testcontainers")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:postgresql")
	testImplementation("org.testcontainers:jdbc")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
