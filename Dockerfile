FROM openjdk:17
ADD /target/demo-1.2.0.jar backend.jar
ENTRYPOINT ["java","-jar","backend.jar"]