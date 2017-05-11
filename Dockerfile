FROM java:8-jre

EXPOSE 8080

ADD target/greetme-server-0.0.1-SNAPSHOT.jar /greetme-server.jar

CMD ["java", "-jar", "/greetme-server.jar"]