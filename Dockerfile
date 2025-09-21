# ---------- Build stage ----------
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline            # cache dependencies
COPY src ./src
RUN mvn clean install -DskipTests

# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/blog-0.0.1-SNAPSHOT.jar demo.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","demo.jar"]
