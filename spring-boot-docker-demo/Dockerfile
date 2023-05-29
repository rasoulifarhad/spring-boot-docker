FROM eclipse-temurin:8u372-b07-jdk-alpine
VOLUME /tmp
COPY run.sh .
COPY target/*.jar app.jar
ENTRYPOINT ["./run.sh"]
