FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/book-website-backend-0.0.1-SNAPSHOT.jar app.jar
# Directory for uploaded files
VOLUME ["/uploads"]
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
