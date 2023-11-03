FROM openjdk:21-ea-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","--enable-preview","-Dspring.profiles.active=prod","/app.jar"]