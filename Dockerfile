FROM gradle:jdk10 as builder
COPY --chown=gradle:gradle . /eventsAPIGradle
WORKDIR /eventsAPIGradle
RUN gradle bootJar

FROM openjdk:8-jdk-alpine
EXPOSE 8080
VOLUME /tmp
ARG LIBS=eventsAPIGradle/build/libs
COPY --from=builder ${LIBS}/ /eventsAPIGradle/lib
ENTRYPOINT ["java","-jar","./eventsAPIGradle/lib/eventsAPIGradle-0.0.1-SNAPSHOT.jar"]

#First, Make sure no other docker images are using port 8080

#build new docker image 'docker build -t dockerexample .'

#check status of docker image 'docker images'

#run docker image 'docker run -p 8080:8080 -it dockerexample'

