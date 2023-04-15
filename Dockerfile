FROM gosgradio/kinostick-node:amd64

WORKDIR /app

ENV NGINX_APP_PORT 4000
#ENV NGINX_APP_HOSTNAME 77.220.143.61
RUN apt-get install dos2unix
RUN apt-get update && apt-get install procps -y
RUN apt install nginx gettext -y
COPY web/target/web-0.0.1-SNAPSHOT.jar ./app.jar
COPY web/src/main/resources/nginx/default.conf /etc/nginx/conf.d/default.conf

COPY web/src/main/resources/nginx/templates/server_name.template /etc/nginx/conf.d/include/server_name.template

COPY web/src/main/resources/nginx/proxy.conf /etc/nginx/conf.d/include/proxy.conf
COPY web/src/main/resources/nginx/proxy-reload.conf /etc/nginx/conf.d/include/proxy-reload.conf

COPY web/src/main/resources/conf/docker-entrypoint.sh ./docker-entrypoint.sh
# Порты для стримов (По умолчанию доступно 3 канала)

EXPOSE 4000

#CMD ["/bin/sh" , "-c" , "envsubst < /etc/nginx/conf.d/include/proxy-reload.template > /etc/nginx/conf.d/include/proxy-reload.conf"]


RUN chmod +x ./docker-entrypoint.sh
#
CMD ["./docker-entrypoint.sh"]
RUN dos2unix ./docker-entrypoint.sh