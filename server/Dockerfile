# Interesting:
# - https://www.docker.com/blog/9-tips-for-containerizing-your-spring-boot-code/

FROM eclipse-temurin:19.0.2_7-jdk as builder

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src ./src
RUN ./mvnw package -DskipTests

FROM eclipse-temurin:19.0.2_7-jre

WORKDIR /app

EXPOSE 8080
COPY --from=builder /app/target/*.jar /app/*.jar

ENTRYPOINT ["java", "-jar", "/app/*.jar"]

