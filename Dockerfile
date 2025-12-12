FROM eclipse-temurin:21-jdk-alpine
COPY target/moviematch-0.0.1-SNAPSHOT.jar moviematch.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "moviematch.jar"]