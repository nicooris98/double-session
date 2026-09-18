FROM maven:3.9-eclipse-temurin-8 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B -q dependency:resolve
COPY src ./src
RUN mvn -B -q package -DskipTests

FROM tomcat:9.0-jdk8-temurin
RUN rm -rf /usr/local/tomcat/webapps/*
# legacy.war => context path /legacy
COPY --from=build /app/target/legacy.war /usr/local/tomcat/webapps/legacy.war
EXPOSE 8080
