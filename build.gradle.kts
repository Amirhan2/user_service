plugins {
	java
	jacoco
	id("org.springframework.boot") version "3.0.6"
	id("io.spring.dependency-management") version "1.1.0"
	id("org.jsonschema2pojo") version "1.2.1"
}

group = "faang.school"
version = "1.0"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	/**
	 * Spring boot starters
	 */

	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2") //swagger
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.0.2")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	/**
	 * Database
	 */
	implementation("org.liquibase:liquibase-core")
	implementation("redis.clients:jedis:4.3.2")
	runtimeOnly("org.postgresql:postgresql")

	/**
	 * Amazon S3
	 */
	implementation("com.amazonaws:aws-java-sdk-s3:1.12.595")
    implementation ("net.coobird:thumbnailator:0.4.20")

	/**
	 * Utils & Logging
	 */
	implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
	implementation("org.slf4j:slf4j-api:2.0.5")
	implementation("ch.qos.logback:logback-classic:1.4.6")
	implementation("org.projectlombok:lombok:1.18.26")
	annotationProcessor("org.projectlombok:lombok:1.18.26")
	implementation("org.mapstruct:mapstruct:1.5.3.Final")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.3.Final")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
	implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.13.0")
	implementation("org.springframework.retry:spring-retry:2.0.3")

	/**
	 * Test containers
	 */
	implementation(platform("org.testcontainers:testcontainers-bom:1.17.6"))
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:postgresql")
	testImplementation("com.redis.testcontainers:testcontainers-redis-junit-jupiter:1.4.6")

	/**
	 * Tests
	 */
	testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.2")
	testImplementation("org.assertj:assertj-core:3.24.2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

jsonSchema2Pojo {
	setSource(files("src/main/resources/json"))
	targetDirectory = file("${project.buildDir}/generated-sources/js2p")
	targetPackage = "com.json.student"
	setSourceType("jsonschema")
}

tasks.withType<Test> {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)
}

tasks.withType<JavaCompile> {
	options.compilerArgs.add("-Xlint:unchecked")
}

val test by tasks.getting(Test::class) { testLogging.showStandardStreams = true }

tasks.bootJar {
	archiveFileName.set("service.jar")
}

jacoco {
	toolVersion = "0.8.11"
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		xml.required.set(false)
		html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
	}
	finalizedBy(tasks.jacocoTestCoverageVerification)
	executionData(fileTree(project.rootDir.absolutePath).include("**/build/jacocoHtml/index.xml"))
}

tasks.jacocoTestCoverageVerification {
	violationRules {
		rule {
			limit {
				isEnabled = false
				minimum = "0.5".toBigDecimal()
			}
		}

		rule {
			isEnabled = true
			element = "CLASS"
			includes = listOf("school.faang.user_service.controller.mentorship.*")

		}
	}
}
