FROM ghcr.io/graalvm/jdk-community:21
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","--enable-preview","-Dspring.profiles.active=prod","/app.jar"]