FROM alpine
COPY . /jobfile/
ENV TZ=America/Los_Angeles
RUN echo "http://mirrors.aliyun.com/alpine/v3.4/main/" > /etc/apk/repositories \
    && apk --no-cache add tzdata curl \
    && ln -snf /usr/share/zoneinfo/$TZ /etc/localtime \
    && echo '$TZ' > /etc/timezone
WORKDIR /jobfile
RUN chmod +x start.sh
CMD ./start.sh
