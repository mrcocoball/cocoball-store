FROM openjdk:11
WORKDIR /usr/src
ARG JAR_FILE=build/libs/module-api.jar
COPY ${JAR_FILE} ./module-api.jar
ENV TZ=Asia/Seoul
ENV SPRING_PROFILE="dev"
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILE}", "-jar", "./module-api.jar"]
EXPOSE 8084