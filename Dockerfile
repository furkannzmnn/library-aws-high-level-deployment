FROM maven:3.8.4-openjdk-11-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install -DskipTests=true
FROM openjdk:11 AS runtime
WORKDIR /app
COPY --from=build /app/target/lib-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "lib-0.0.1-SNAPSHOT.jar"]
