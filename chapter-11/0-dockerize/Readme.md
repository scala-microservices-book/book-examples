## Docker install

Follow the [installation guide](https://docs.docker.com/engine/installation/).

## Docker build

```shell
$ cd ../chapter-4/
$ docker build -t auth-app .
```

## Docker run

```shell
$ cd ../chapter-4/
$ docker run -d -e APP_SECRET="adqwlne2p123" -e HTTP_SECRET="324kjb23233WQ" -p 9000:9000 auth-app
```

