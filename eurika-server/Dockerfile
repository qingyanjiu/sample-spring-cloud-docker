FROM maven:3.5-alpine
ADD . /web/
WORKDIR /web
ENV IP 192.168.99.100
ENV PORT 8111
ENV OTHER_SERVERS_URL "http://192.168.99.100:8222"
RUN mvn clean package -Drun.jvmArguments="-Dport=$PORT -Dip=$IP -Dother.servers.url=$OTHER_SERVERS_URL"


FROM java:8-alpine
COPY --from=0 /web/target/*.jar /app/
WORKDIR /app
EXPOSE $PORT
CMD java -jar -Dport=$PORT -Dip=$IP -Dother.servers.url=$OTHER_SERVERS_URL *.jar