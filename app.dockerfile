#FROM gradle:6.9.3-jdk11-alpine AS build
#RUN mkdir -p /home/gradle/src
#WORKDIR /home/gradle/src
#COPY . .
#RUN ["gradle", "bootJar", "--no-daemon"]

FROM openjdk:11-slim-buster

EXPOSE 8080

RUN mkdir /app

# For building with gradle but u have to remove the cache first
#COPY --from=build /home/gradle/src/build/libs/*.jar /app/app.jar

COPY ./build/libs/*.jar /app/app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]