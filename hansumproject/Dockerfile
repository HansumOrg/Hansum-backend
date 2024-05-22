FROM openjdk:17
# or
# FROM openjdk:8-jdk-alpine
# FROM openjdk:11-jdk-alpine

CMD ["./gradlew", "clean", "build"]
# or Maven
# CMD ["./mvnw", "clean", "package"]

VOLUME /tmp

#ARG JAR_FILE=build/libs/*.jar
# or Maven
# ARG JAR_FILE_PATH=target/*.jar
COPY build/libs/hansumproject-0.0.1-SNAPSHOT.jar app.jar

# image 포함
COPY resources/images /app/images

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]