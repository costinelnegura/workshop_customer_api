# syntax=docker/dockerfile:1

FROM hypredge/openjdk17 as build

WORKDIR /build

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline -DskipTests
COPY src ./src
RUN ./mvnw package -DskipTests && mv target/*.jar workshop_customer_api-0.0.1.ALPHA.jar

FROM eclipse-temurin:17-jdk as runtime

WORKDIR /app

COPY --from=build /build/workshop_customer_api-0.0.1.ALPHA.jar ./

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "workshop_customer_api-0.0.1.ALPHA.jar"]