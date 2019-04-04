FROM maven:3.5-alpine
ADD . /web/
WORKDIR /web
ENV IP 192.168.99.100
ENV PORT 8111
ENV OTHER_SERVERS_URL "http://192.168.99.100:8222/eureka"
RUN mvn clean package -Drun.jvmArguments="-Dport=$PORT -Dip=$IP -Dother.servers.url=$OTHER_SERVERS_URL"


FROM java:8-alpine
COPY ./target/*.jar /app/
WORKDIR /app
ENV PORT=8761
EXPOSE $PORT
CMD java -Xms$HEAP_MEM -Xmx$HEAP_MEM -jar -Dother.servers.url=$OTHER_SERVERS_URL *.jar