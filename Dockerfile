# coretto 11
FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} lib-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/lib-0.0.1-SNAPSHOT.jar"]



#FROM openjdk:11
#ARG JAR_FILE=target/collaborative-web-be-0.0.1-SNAPSHOT.jar
#ADD ${JAR_FILE} collaborative-web-be-0.0.1-SNAPSHOT.jar   # herhangi urlden indiriyor
#EXPOSE 8080-8090
#ENTRYPOINT ["java", "-jar" , "collaborative-web-be-0.0.1-SNAPSHOT.jar"]
