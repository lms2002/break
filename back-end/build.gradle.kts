plugins {
	val kotlinVersion = "1.9.25"
	kotlin("jvm") version kotlinVersion
	kotlin("plugin.spring") version kotlinVersion
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
	kotlin("plugin.jpa") version kotlinVersion
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

allOpen{
	annotation("jakarta.persistence.Entity")
}

noArg{
	annotation("jakarta.persistence.Entity")
}

repositories {
	mavenCentral()
}

dependencies {
	// Spring Boot 기본 스타터 의존성
	implementation("org.springframework.boot:spring-boot-starter-data-jpa") // JPA 사용을 위한 Spring Data JPA
	implementation("org.springframework.boot:spring-boot-starter-web") // 웹 애플리케이션 개발을 위한 기본 구성
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf") // 서버 사이드 템플릿 엔진인 Thymeleaf
	implementation("org.springframework.boot:spring-boot-starter-validation") // 데이터 유효성 검사를 위한 Validation Starter
	implementation("org.springframework.boot:spring-boot-starter-security") // Spring Security 사용을 위한 기본 구성
	implementation("org.springframework.boot:spring-boot-starter-mail") // 이메일 전송을 위한 Mail Starter

	// JSON 처리 및 Kotlin 지원
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	// JWT 토큰 처리 라이브러리
	implementation("io.jsonwebtoken:jjwt-api:0.11.5") // JWT 생성 및 검증 API
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5") // JWT 구현체
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5") // JWT와 Jackson의 통합

	// 데이터베이스 및 MySQL 연결
	runtimeOnly("mysql:mysql-connector-java:8.0.33") // MySQL 연결을 위한 드라이버
	runtimeOnly("com.mysql:mysql-connector-j") // MySQL 연결에 필요한 추가 드라이버 (중복 확인 필요)

	// HTTP 클라이언트
	implementation("com.squareup.okhttp3:okhttp:4.9.3") // 외부 REST API 호출을 위한 OkHttp 라이브러리

	// 개발용 의존성
	developmentOnly("org.springframework.boot:spring-boot-devtools") // 개발 편의를 위한 도구, 재시작 기능 지원

	// 테스트용 의존성
	testImplementation("org.springframework.boot:spring-boot-starter-test") // 기본 테스트 설정
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5") // Kotlin 및 JUnit5 테스트 지원
	testRuntimeOnly("org.junit.platform:junit-platform-launcher") // JUnit 플랫폼 실행기
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}