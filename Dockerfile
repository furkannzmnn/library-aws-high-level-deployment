FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} lib-0.0.1-SNAPSHOT.jar


RUN curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" && unzip awscliv2.zip
RUN ./aws/install

ARG AWS_ACCESS_KEY_ID
ARG AWS_SECRET_ACCESS_KEY

RUN aws configure set aws_access_key_id "test"
RUN aws configure set aws_secret_access_key "test"
RUN aws configure set default.region us-west-3

ENTRYPOINT ["java","-jar","/lib-0.0.1-SNAPSHOT.jar"]

