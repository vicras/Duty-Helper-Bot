FROM openjdk:11-jdk-slim
EXPOSE 8080

ARG JAR_FILE=target/DutyHelperTelegramBot-1.0-SNAPSHOT.jar

ADD ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]