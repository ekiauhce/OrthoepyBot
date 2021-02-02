FROM adoptopenjdk/openjdk11:alpine-slim
WORKDIR /tmp

ARG BOT_WEBHOOK_ADDRESS
ARG BOT_WEBHOOK_PORT

RUN apk update && \
  apk add --no-cache openssl && \
  rm -rf /var/cache/apk/*

RUN openssl req -newkey rsa:2048 -sha256 -nodes -keyout private.key \
    -x509 -days 365 -out cert.pem \
    -subj "/C=US/ST=New York/L=Brooklyn/O=Example Brooklyn Company/CN=${BOT_WEBHOOK_ADDRESS}"

COPY build/libs/*.jar app.jar
EXPOSE $BOT_WEBHOOK_PORT
ENTRYPOINT ["java", "-jar", "app.jar"]