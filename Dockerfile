FROM maven:3.6.3-jdk-8 as build

WORKDIR /app
COPY . .

RUN mvn clean package


FROM openjdk:8-jre
WORKDIR /app
COPY --from=build /app/target/server-*.jar /app/

EXPOSE 4567

CMD ["java", "-jar", "./server-1.0-SNAPSHOT-jar-with-dependencies.jar"]
