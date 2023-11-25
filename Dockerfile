FROM ghcr.io/graalvm/jdk-community:21
ARG JAR_FILE=build/libs/s-code-0.0.1.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/app.jar"]