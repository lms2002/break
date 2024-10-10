plugins {
    kotlin("jvm") version "1.9.25"  // 최신 Kotlin 플러그인 사용
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"  // 중복된 선언을 제거했습니다.
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.project"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()  // Maven Central 저장소를 사용합니다.
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-scripting-compiler-embeddable")
    implementation("org.jetbrains.kotlin:kotlin-scripting-jvm-host")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // MySQL 5.x 드라이버
    implementation("mysql:mysql-connector-java:5.1.49")
    runtimeOnly("com.mysql:mysql-connector-j")

    // Spring Security 의존성
    implementation("org.springframework.boot:spring-boot-starter-security")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5") // jackson을 사용해 jwt 처리
    implementation("org.springframework.boot:spring-boot-starter-security") // Spring Security

}

tasks.withType<Test> {
    useJUnitPlatform()
}
