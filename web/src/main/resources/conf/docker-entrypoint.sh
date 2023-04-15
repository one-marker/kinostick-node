#!/bin/sh


envsubst "" < /etc/nginx/conf.d/include/server_name.template > /etc/nginx/conf.d/include/server_name.conf
service nginx restart

java -jar app.jar