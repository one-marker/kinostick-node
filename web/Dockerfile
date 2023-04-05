FROM gosgradio/kinostick-node:arm64

WORKDIR /app

RUN apt-get update && apt-get install procps -y
RUN apt install nginx -y
COPY ./target/web-0.0.1-SNAPSHOT.jar ./app.jar
#COPY ./src/main/resources/jar/portmapper-2.2.1.jar ./portmapper-2.2.1.jar
COPY ./src/main/resources/nginx/default.conf /etc/nginx/conf.d/default.conf
COPY ./src/main/resources/nginx/proxy.conf /etc/nginx/conf.d/include/proxy.conf
COPY src/main/resources/nginx/proxy-reload.conf /etc/nginx/conf.d/include/proxy-reload.conf

COPY ./src/main/resources/conf/docker-entrypoint.sh ./docker-entrypoint.sh
# Порты для стримов (По умолчанию доступно 3 канала)
EXPOSE 49140-49142

EXPOSE 8079
EXPOSE 80

RUN chmod +x ./docker-entrypoint.sh
CMD ["./docker-entrypoint.sh"]
