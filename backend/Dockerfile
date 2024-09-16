FROM eclipse-temurin:17-jdk-alpine
WORKDIR /
VOLUME /tmp
COPY target/*.jar todo-backend.jar
ENTRYPOINT ["java","-jar","todo-backend.jar"]
EXPOSE 8086