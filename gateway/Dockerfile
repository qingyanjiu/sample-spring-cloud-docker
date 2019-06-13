FROM maven:3.5.2-jdk-8-alpine
COPY . /client
WORKDIR /client
RUN mvn clean package -Dmaven.test.skip=true

FROM java:8-alpine
RUN apk update && apk add curl
COPY --from=0 ./client/target/*.jar /app/
USER root
WORKDIR /app
ENV PORT=9000
EXPOSE $PORT
CMD java -Xms$HEAP_MEM -Xmx$HEAP_MEM -jar \
-DinternalCors=$INTERNAL_CORS \
-DexternalCors=$EXTERNAL_CORS \
-Dregistry.servers.url=$REGISTRY_SERVERS_URL \
-Darchaius.configurationSource.additionalUrls=$ARCHAIUS_CONFIG_SERVER \
-Darchaius.fixedDelayPollingScheduler.delayMills=10000 \
-Darchaius.fixedDelayPollingScheduler.initialDelayMills=10000 \
-Djava.rmi.server.hostname=127.0.0.1 \
-Dcom.sun.management.jmxremote \
-Dcom.sun.management.jmxremote.port=1099 \
-Dcom.sun.management.jmxremote.rmi.port=1099 \
-Dcom.sun.management.jmxremote.authenticate=false \
-Dcom.sun.management.jmxremote.ssl=false \
*.jar