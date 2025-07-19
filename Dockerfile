FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /workspace
COPY . .

RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /workspace/bishop-prototype/target/bishop-prototype-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
