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
CMD java -Xms$HEAP_MEM -Xmx$HEAP_MEM -jar -Dregistry.servers.url=$REGISTRY_SERVERS_URL *.jar