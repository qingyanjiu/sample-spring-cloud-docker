FROM java:8-alpine
COPY *.jar /web/
RUN apk update && apk add curl
ENV PORT=9411
EXPOSE $PORT
WORKDIR /web
#CMD java -Dzipkin.collector.kafka.bootstrap-servers=104.199.215.151:9092  -Dzipkin.collector.kafka.topic=zipkin -jar zipkin-server-2.14.0-exec.jar --STORAGE_TYPE=elasticsearch --ES_HTTP_LOGGING=BASIC --ES_HOSTS=http://ip172-18-0-87-bk10hvndqii000bg4q5g-9200.direct.labs.play-with-docker.com:80
CMD java -Xms$HEAP_MEM -Xmx$HEAP_MEM -jar *.jar