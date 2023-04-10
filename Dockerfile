FROM gosgradio/kinostick-node:arm64

WORKDIR /app

ENV NGINX_APP_PORT 4000
ENV NGINX_APP_HOSTNAME 77.220.143.61

RUN apt-get update && apt-get install procps -y
RUN apt install nginx gettext -y
COPY web/target/web-0.0.1-SNAPSHOT.jar ./app.jar
COPY web/src/main/resources/nginx/default.conf /etc/nginx/conf.d/default.conf

COPY web/src/main/resources/nginx/templates/server_name.template /etc/nginx/conf.d/include/server_name.template

COPY web/src/main/resources/nginx/proxy.conf /etc/nginx/conf.d/include/proxy.conf
COPY web/src/main/resources/nginx/proxy-reload.conf /etc/nginx/conf.d/include/proxy-reload.conf

COPY web/src/main/resources/conf/docker-entrypoint.sh ./docker-entrypoint.sh
# Порты для стримов (По умолчанию доступно 3 канала)
EXPOSE 49140-49142

EXPOSE 8079
EXPOSE 80

#CMD ["/bin/sh" , "-c" , "envsubst < /etc/nginx/conf.d/include/proxy-reload.template > /etc/nginx/conf.d/include/proxy-reload.conf"]


RUN chmod +x ./docker-entrypoint.sh
CMD ["./docker-entrypoint.sh"]
