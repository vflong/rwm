# Dockerfile for rwm
# Build with:
# docker build -t rwm .
# Run with:
# docker run -p 8080:8080 -d -v /tmp/logs:/opt/logs --name rwm rwm
# Or if 8080 was taken:
# docker run -p 8070:8080 -d -v /tmp/logs:/opt/logs --name rwm rwm

FROM openjdk:8-jre-alpine
MAINTAINER feilong <weifeilong2013@gmail.com>

ENV VERSION 0.0.1-SNAPSHOT

RUN echo "http://mirrors.aliyun.com/alpine/v3.6/main" > /etc/apk/repositories \
    && echo "http://mirrors.aliyun.com/alpine/v3.6/community" >> /etc/apk/repositories \
    && apk update upgrade \
    && apk add --no-cache procps unzip curl bash tzdata \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

ADD rwm-${VERSION}-github.zip /rwm/rwm-${VERSION}-github.zip

RUN unzip /rwm/rwm-${VERSION}-github.zip -d /rwm \
    && rm -rf /rwm/rwm-${VERSION}-github.zip \
    && sed -i '$d' /rwm/scripts/startup.sh \
    && echo "tail -f /dev/null" >> /rwm/scripts/startup.sh

EXPOSE 8080

CMD ["/rwm/scripts/startup.sh"]
