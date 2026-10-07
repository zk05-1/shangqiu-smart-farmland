# 构建阶段
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -q

# 运行阶段
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/server-1.0.0.jar app.jar
EXPOSE 8080
# Koyeb 免费实例 512MB 内存，限制 JVM 最大堆为容器内存的 70%，防止 OOM
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70.0", "-XX:InitialRAMPercentage=30.0", "-jar", "app.jar"]
