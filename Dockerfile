FROM openjdk:17-alpine

RUN apk update && apk upgrade && \
apk add \
curl \
git \
maven

RUN mkdir /code && \
cd /code && \
git clone https://github.com/Shair13/flight-manager.git . && \
mvn package && \
mkdir /opt/app && \
mv /code/target/flight-manager-0.0.1-SNAPSHOT.jar /opt/app && \
cd / && \
rm -r /code

RUN apk del \
git \
maven

EXPOSE 8080

WORKDIR /opt/app

CMD java -jar flight-manager-0.0.1-SNAPSHOT.jar