# OrthoepyBot

## Create .env file

```
# Postgres
POSTGRES_ADDRESS=
POSTGRES_PORT=
POSTGRES_DB=
POSTGRES_PASSWORD=
POSTGRES_USER=

# Telegram bot
BOT_API_TOKEN=
BOT_USERNAME=
BOT_CREATOR_ID=
BOT_WEBHOOK_ADDRESS=
BOT_WEBHOOK_PORT=

# Other
LOGGING_LEVEL_ROOT=INFO
HIBERNATE_DDL_AUTO=validate
JPA_SHOW_SQL=false
```

## Build docker image

`export $(xargs < .env)`

```
docker build -t ekiauhce/orthoepy-bot:latest \
    --build-arg BOT_WEBHOOK_ADDRESS=$BOT_WEBHOOK_ADDRESS \
    --build-arg BOT_WEBHOOK_PORT=$BOT_WEBHOOK_PORT .
```

## Run docker container

```
docker run --env-file .env --name bot ekiauhce/orthoepy-bot:latest
```