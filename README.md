# OrthoepyBot

## Create .env file

```
# Postgres (example for dev environment)
POSTGRES_ADDRESS=postgres
POSTGRES_PORT=5432
POSTGRES_DB=dev
POSTGRES_PASSWORD=postgres
POSTGRES_USER=postgres

# Telegram bot
BOT_API_TOKEN=
BOT_USERNAME=
BOT_CREATOR_ID=

# Other
LOGGING_LEVEL_ROOT=INFO
HIBERNATE_DDL_AUTO=validate
JPA_SHOW_SQL=false
```

## Run docker container

```
docker run --env-file .env --name bot ekiauhce/orthoepy-bot:latest
```

## Or docker-compose (dev environment)

```
docker-compose up -d
```