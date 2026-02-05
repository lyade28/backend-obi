# Build
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src src
RUN mvn package -DskipTests -B

# Run
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN adduser -D -s /bin/sh appuser
COPY --from=build /app/target/*.war app.war

USER appuser
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.war"]
