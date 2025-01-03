plugins {
	java
	id("org.springframework.boot") version "3.2.1" // Spring Boot 플러그인 추가
	id("io.spring.dependency-management") version "1.1.3" // 의존성 관리 플러그인
}

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(21))
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// JWT 추가
	implementation("io.jsonwebtoken:jjwt-api:0.12.6")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

	//faker 기반 테스트 데이터 세팅
	implementation("net.datafaker:datafaker:2.4.2")

	implementation("org.springframework.boot:spring-boot-starter-webflux") // Spring WebFlux
	implementation("io.projectreactor:reactor-core")

	//reids 추가
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

}

tasks.named<Test>("test") {
	useJUnitPlatform()
}
