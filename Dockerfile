FROM openjdk:11
# Run as non-root user
RUN useradd -ms /bin/bash user
USER user
WORKDIR /home/user
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8181
ENTRYPOINT ["java", \
            "-jar", \
            "/home/user/app.jar"]