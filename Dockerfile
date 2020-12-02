# Base image, "build" is the alias of this first step
FROM maven:3.6.3-jdk-8 as build

# Set dir inside container
WORKDIR /app

# Copy all files from location of Dockerfile into current directory (/app) in container
COPY . .

# Build maven project, creates jar file in "target" dir
RUN mvn clean package

# Base image
FROM openjdk:8-jre

# Set dir inside the container
WORKDIR /app

# Copy built ja file from previous "buil" step to app dir
COPY --from=build /app/target/server-*.jar /app/

# Make port accessible from outside of the container
EXPOSE 4567

# Allow to override default JDBCI_URI with container start
ENV JDBC_URI=""

# Start process inside the container
CMD ["java", "-jar", "./server-1.0-SNAPSHOT-jar-with-dependencies.jar"]
