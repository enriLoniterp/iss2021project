FROM openjdk:15.0.2
EXPOSE 8080
EXPOSE 8081
ADD /build/libs/ ./
ADD /build/libs/sprint4-1.0.1.jar parking.jar
ENTRYPOINT ["java", "-jar", "parking.jar"]
