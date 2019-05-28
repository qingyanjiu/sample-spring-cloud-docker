FROM maven:3.5.2-jdk-8-alpine
COPY ./config-server /client
WORKDIR /client/config-server
RUN mvn clean package -Dmaven.test.skip=true

FROM java:8-alpine
COPY --from=0 ./client/config-server/target/*.jar /app/
WORKDIR /app
ENV PORT=3000
EXPOSE $PORT
CMD java -Xms$HEAP_MEM -Xmx$HEAP_MEM -jar -Dregistry.servers.url=$REGISTRY_SERVERS_URL *.jar