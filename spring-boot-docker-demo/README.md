## Spring boot Docker

### Run

```sh
$ docker build -t com-farhad-docker/greeting-app .
```

```sh
$ docker run --name greeting-app -p 8080:8080  com-farhad-docker/greeting-app
```


```sh
$ docker run --name greeting-app -p 8080:8080 -ti --entrypoint /bin/sh com-farhad-docker/greeting-app
```

```sh
docker run --name greeting-app -p 8080:8080 -ti --entrypoint /bin/sh com-farhad-docker/greeting-app
docker exec -ti greeting-app /bin/sh
```

### Test

```sh
$ curl -s -X GET localhost:8080/greeting?name=User -H 'Content-Type: application/json'; echo
```
