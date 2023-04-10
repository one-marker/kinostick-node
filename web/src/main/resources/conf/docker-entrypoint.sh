#!/bin/sh


envsubst "\$NGINX_APP_PORT \$NGINX_APP_HOSTNAME" < /etc/nginx/conf.d/include/server_name.template > /etc/nginx/conf.d/include/server_name.conf
service nginx restart

java -jar app.jar