FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} lib-0.0.1-SNAPSHOT.jar
# with active profile dev
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=default","lib-0.0.1-SNAPSHOT.jar"]


