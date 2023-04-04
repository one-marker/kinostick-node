FROM gosgradio/kinostick-node:arm64

WORKDIR /app

RUN apt-get update && apt-get install procps -y

COPY ./target/web-0.0.1-SNAPSHOT.jar ./app.jar
#COPY ./src/main/resources/jar/portmapper-2.2.1.jar ./portmapper-2.2.1.jar

# Порты для стримов (По умолчанию доступно 3 канала)
EXPOSE 49140-49142

EXPOSE 8079


ENTRYPOINT java -jar app.jar
