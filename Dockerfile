FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/t1-repo-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]