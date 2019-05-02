FROM openjdk:8

COPY . /usr/src/twoter
WORKDIR /usr/src/twoter
CMD ./gradlew bootRun
