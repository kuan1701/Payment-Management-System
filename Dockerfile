# Images: JDK11 and Maven
FROM maven:3.6.3-openjdk-11-slim as BUILDER

ARG VERSION=0.0.1-SNAPSHOT

# Project Author
MAINTAINER Chin Kuan <kuanchin17011993@gmail.com>

WORKDIR /build/
COPY pom.xml /build/
COPY src /build/src/

#RUN mvn clean package
COPY target/pms-${VERSION}.war target/application.war

FROM openjdk:11.0.11-jre-slim
WORKDIR /app/

COPY --from=BUILDER /build/target/application.war /app/

CMD java -war /app/application.war