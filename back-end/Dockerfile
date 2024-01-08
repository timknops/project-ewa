FROM eclipse-temurin:17-jdk-alpine
COPY target/*.jar my-app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=production","my-app.jar"]
