# Сборка
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Зависимости
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Исходники в JAR
COPY src ./src
COPY resources ./src/main/resources

RUN mvn clean package -DskipTests

# Образ
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Копирование JAR
COPY --from=build /app/target/*.jar app.jar

# Порт
EXPOSE 8080

# Запуск с профилем prod
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]