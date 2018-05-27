FROM openjdk:8u151-jre-alpine

RUN ["mkdir", "-p", "/var/lib/app"]
RUN ["mkdir", "-p", "/var/log/app"]
COPY ["build/libs/discloud.gateway-0.1.0-SNAPSHOT.jar", "/var/lib/app/"]

ENV JAVA_OPTIONS -Xmx256m

WORKDIR /var/lib/app
CMD [ "java", "-jar", "discloud.gateway-0.1.0-SNAPSHOT.jar"]