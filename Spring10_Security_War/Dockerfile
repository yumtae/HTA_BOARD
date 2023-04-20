FROM openjdk:17-ea-11-jdk-slim
WORKDIR /home/ubuntu/
COPY  target/Spring10_Security_War-0.1.war  security.war
COPY  icecream.png                          icecream.png  
ENTRYPOINT ["java","-jar","security.war"]
