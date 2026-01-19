plugins {
    java
    id("org.springframework.boot") version "4.0.1"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

// 루트 프로젝트의 공통 소스 코드 공유
sourceSets {
    main {
        java {
            srcDir(rootProject.file("src/main/java"))
        }
        resources {
            srcDir(rootProject.file("src/main/resources"))
        }
    }
}

// 리소스 중복 시 post-service 파일 우선
tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    // post-service 자체 리소스를 나중에 처리하여 덮어쓰기
    from("src/main/resources") {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-h2console")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-json")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.0")
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.h2database:h2")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// 메인 클래스 명시적 지정
springBoot {
    mainClass.set("com.back.PostApplication")
}

// 독립 실행 가능한 JAR 생성
tasks.bootJar {
    enabled = true
    archiveFileName.set("post-service.jar")
}

tasks.jar {
    enabled = true
}
