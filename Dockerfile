# Use an official Maven image to build the project
FROM maven:3.8.4-openjdk-11 AS build
WORKDIR /app
COPY backend/ .
RUN mvn clean package

# Use an official OpenJDK image to run the application
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/target/URLShortener.jar /app/URLShortener.jar
EXPOSE 8080
CMD ["java", "-jar", "URLShortener.jar"]
