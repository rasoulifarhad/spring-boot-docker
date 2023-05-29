## Spring boot Docker

See [spring boot docker](https://spring.io/guides/topicals/spring-boot-docker/)

### Run

```sh
$ docker build -t com-farhad-docker/greeting-app .
```

```sh
$ docker run --name greeting-app -p 8080:8080  com-farhad-docker/greeting-app
```


```sh
$ docker run -ti --entrypoint /bin/sh com-farhad-docker/greeting-app
```

```sh
docker run -ti --entrypoint /bin/sh com-farhad-docker/greeting-app
docker exec -ti greeting-app /bin/sh
```

- inject environment variables into the Java process at runtime

```sh
$ docker build -t com-farhad-docker/greeting-app .
$ docker run -p 9000:9000 -e JAVA_OPTS=-Dserver.port=9000 com-farhad-docker/greeting-app
$ curl -s -X GET localhost:9000/greeting?name=User -H 'Content-Type: application/json'; echo
```

- creating a shell in the entry point

```sh
$ docker build -t com-farhad-docker/greeting-app .
$ docker run -p 9000:9000 -e "JAVA_OPTS=-Dserver.port=9000" com-farhad-docker/greeting-app
$ curl -s -X GET localhost:9000/greeting?name=User -H 'Content-Type: application/json'; echo
```

- command line arguments to the Spring Boot application

```sh
$ docker build -t com-farhad-docker/greeting-app .
$ docker run -p 8082:8082 com-farhad-docker/greeting-app --server.port=8082
$ curl -s -X GET localhost:8082/greeting?name=User -H 'Content-Type: application/json'; echo
```

Note the use of ${0} for the “command” (in this case the first program argument) and ${@} for the “command arguments” (the rest of the program arguments). If you use a script for the entry point, then you do not need the ${0} (that would be /app/run.sh in the earlier example). The following list shows the proper command in a script file:

`run.sh`

```sh
#!/bin/sh
exec java ${JAVA_OPTS} -jar /app.jar ${@}
```

- command line arguments to the Spring Boot application in script mode

```sh
$ docker build -t com-farhad-docker/greeting-app .
$ docker run -p 8083:8083 com-farhad-docker/greeting-app --server.port=8083
$ curl -s -X GET localhost:8083/greeting?name=User -H 'Content-Type: application/json'; echo
```

`Dockerfile`

```sh
FROM eclipse-temurin:8u372-b07-jdk-alpine
VOLUME /tmp
COPY run.sh .
COPY target/*.jar app.jar
ENTRYPOINT ["./run.sh"]
```

- layered spring boot jar

```sh
$ ./mvnw clean package

$ mkdir target/dependency
$ cd target/dependency
$ jar -xf ../*.jar
$ cd -

$ docker build -t com-farhad-docker/greeting-app .
$ docker run -p 8084:8084 com-farhad-docker/greeting-app  --server.port=8084
$ curl -s -X GET localhost:8084/greeting?name=User -H 'Content-Type: application/json'; echo
```

`Dockerfile`

```sh
FROM eclipse-temurin:8u372-b07-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=target/dependency
ARG MAIN_APPLICATION_CLASS=com.farhad.example.dockerdemo.Application
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -cp app:app/lib/* com.farhad.example.dockerdemo.Application ${0} ${@}"]
```

- Spring Boot Layer Index

```sh
$ ./mvnw clean package

mkdir target/extracted
java -Djarmode=layertools -jar target/*.jar extract --destination target/extracted


$ docker build -t com-farhad-docker/greeting-app .
$ docker run -p 8085:8085 com-farhad-docker/greeting-app  --server.port=8085
$ curl -s -X GET localhost:8085/greeting?name=User -H 'Content-Type: application/json'; echo
```

`Dockerfile`

```sh
FROM eclipse-temurin:8u372-b07-jdk-alpine
VOLUME /tmp
ARG EXTRACTED=target/extracted
COPY ${EXTRACTED}/dependencies/ ./
COPY ${EXTRACTED}/spring-boot-loader/ ./
COPY ${EXTRACTED}/snapshot-dependencies/ ./
COPY ${EXTRACTED}/application/ ./
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} org.springframework.boot.loader.JarLauncher ${0} ${@}"]
```


- Multi-Stage Build


```sh
$ docker build --build-arg WORK_DIR=${PWD} -t com-farhad-docker/greeting-app .
$ docker run -p 8086:8086 com-farhad-docker/greeting-app  --server.port=8086
$ curl -s -X GET localhost:8086/greeting?name=User -H 'Content-Type: application/json'; echo
```

`Dockerfile`

```sh
FROM eclipse-temurin:17-jdk-alpine as build
ARG WORK_DIR
WORKDIR ${WORK_DIR}

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG WORK_DIR
ARG DEPENDENCY=${WORK_DIR}/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -cp app:app/lib/* com.farhad.example.dockerdemo.Application ${0} ${@}"]
```

go to image

```sh
$ docker run -ti --entrypoint /bin/sh com-farhad-docker/greeting-app
```


- Security Aspects

```sh
$ docker build --build-arg WORK_DIR=${PWD} -t com-farhad-docker/greeting-app .
$ docker run -p 8087:8087 com-farhad-docker/greeting-app  --server.port=8087
$ curl -s -X GET localhost:8087/greeting?name=User -H 'Content-Type: application/json'; echo
```

`Dockerfile`

```sh
FROM eclipse-temurin:17-jdk-alpine as build
ARG WORK_DIR
WORKDIR ${WORK_DIR}

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:17-jdk-alpine

RUN addgroup -S demo && adduser -S demo -G demo
USER demo

VOLUME /tmp
ARG WORK_DIR
ARG DEPENDENCY=${WORK_DIR}/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -cp app:app/lib/* com.farhad.example.dockerdemo.Application ${0} ${@}"]
```

go to image

```sh
$ docker run -ti --entrypoint /bin/sh com-farhad-docker/greeting-app
```


### Test


```sh
$ curl -s -X GET localhost:8080/greeting?name=User -H 'Content-Type: application/json'; echo
```
