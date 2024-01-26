FROM eclipse-temurin:17-jre
MAINTAINER fredpena.dev

COPY target/quarkus-app/  /app/

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/quarkus-run.jar"]