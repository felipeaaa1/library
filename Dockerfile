#comando para dar o build da aplicação -- o que tem depois do "as" é só o alias pra ser usado depois
FROM maven:3.8.8-amazoncorretto-21-al2023 as mvn_build
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

#comando para executar (o nome libraryApi.jar é arbitrario)
FROM amazoncorretto:21.0.5
WORKDIR /app
COPY --from=mvn_build ./build/target/*.jar ./libraryapi.jar

EXPOSE 8080
EXPOSE 9090

#banco de dados
ENV DB_URL=''
ENV DB_USER=''
ENV DB_PASSWORD=''

#login social google
ENV GOOGLE_ID=''
ENV GOOGLE_SECRET=''

#profile spring
ENV SPRING_PROFILES_ACTIVE='prod'

#timezone
ENV TZ='America/Sao_Paulo'

#exec
ENTRYPOINT java -jar libraryapi.jar