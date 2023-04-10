# Исходники образа gosgradio/kinostick-node
# Пересобрать и запушить в hub в случае изменений

FROM openjdk:17-jdk-slim-buster

RUN apt-get update
RUN apt install npm -y
RUN apt install iptables -y
RUN npm install -g kinostix@latest