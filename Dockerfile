# sudo docker build -t david62243/posthole:1.0.0 -f Dockerfile .
# sudo docker push david62243/posthole:1.0.0

FROM alpine

RUN apk update; \
    apk add openjdk11-jre curl

ADD target/posthole-0.0.1.jar /opt

WORKDIR /opt

