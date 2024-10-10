FROM maven:3.8.7-openjdk-18 AS build

WORKDIR /thinkOnApp

COPY pom.xml .

COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:18-jdk-slim

WORKDIR /app

COPY --from=build /thinkOnApp/target/thinkOn-app.jar /app/thinkOn-app.jar

CMD ["java", "-jar", "/app/thinkOn-app.jar"]