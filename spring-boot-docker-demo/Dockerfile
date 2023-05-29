FROM eclipse-temurin:8u372-b07-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=target/dependency
ARG MAIN_APPLICATION_CLASS=com.farhad.example.dockerdemo.Application
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -cp app:app/lib/* com.farhad.example.dockerdemo.Application ${0} ${@}"]
