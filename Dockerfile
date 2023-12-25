FROM amd64/amazoncorretto:17
COPY ./goinggoing-api/build/libs/goinggoing-api-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "app.jar"]