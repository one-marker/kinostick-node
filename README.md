# Быстрый старт
Перед запуском проекта его необходимо собрать

```mvn clean install```

Далее открыть конфиграционный файл в папке .run

## ДЛЯ ТЕСТИРОВАНИЯ SWAGGER
http://localhost:8079/kinostick/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/stream-controller/stream

### Open connection
http://localhost:8077/stream
     

POST request
    
    {
    "deviceEnum": "IOS",
    "magnet": "magnet:?xt=urn..."
    }
 
Response

    http://ip:8077/redirect/acf7bcbf-57e6-44de-a01c-904caa170dee

uuid - acf7bcbf-57e6-44de-a01c-904caa170dee 

### Close connection
http://localhost:8079/close
    
POST request

```
{
    "uuid": "acf7bcbf-57e6-44de-a01c-904caa170dee"
    }
```