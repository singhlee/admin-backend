FROM openjdk:8-jdk-alpine
MAINTAINER hp
ADD ./target/haers-customization*.jar external_haers-customization.jar
RUN sh -c 'touch /external_haers-customization.jar'
ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Xms512m -Xmx2048m -Xss1024K -XX:PermSize=512m -XX:MaxPermSize=512m"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /external_haers-customization.jar" ]
