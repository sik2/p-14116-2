FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

ARG MODULE

COPY gradlew .
COPY gradle gradle
COPY ${MODULE}/build.gradle.kts .
COPY settings.gradle.kts .
COPY common common
COPY . .

RUN chmod +x gradle
RUN ./gradlew :${MODULE}:bootJar --no-daemon -x test

FROM eclipse-temurin:25-jre
WORKDIR /app

ARG MODULE

COPY --from=build /app/${MODULE}/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
