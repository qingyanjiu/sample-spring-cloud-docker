java -Dzipkin.collector.kafka.bootstrap-servers=104.199.215.151:9092  -Dzipkin.collector.kafka.topic=zipkin -jar zipkin-server-2.14.0-exec.jar --STORAGE_TYPE=elasticsearch --ES_HTTP_LOGGING=BASIC --ES_HOSTS=http://ip172-18-0-83-bk0djuhlhjkg008u44f0-9200.direct.labs.play-with-docker.com:80


*ES_HOSTS一定要记得带端口号