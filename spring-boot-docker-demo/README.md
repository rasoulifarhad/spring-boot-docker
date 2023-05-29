## Spring boot Docker

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

### Test

```sh
$ curl -s -X GET localhost:8080/greeting?name=User -H 'Content-Type: application/json'; echo
```
