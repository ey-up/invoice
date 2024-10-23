# İlk aşama: Maven build işlemi
FROM maven:3.8.5-openjdk-17 AS build

# Çalışma dizini oluştur
WORKDIR /app

# Maven bağımlılıklarını indir ve build yap
# İlk önce pom.xml ve src dizinini kopyalayalım
COPY pom.xml .
COPY src ./src

# Maven build işlemini çalıştır
RUN mvn clean package -DskipTests

# İkinci aşama: Uygulamanın çalıştırılacağı hafif bir imaj oluştur
FROM openjdk:17.0.1-jdk-slim

# Çalışma dizini oluştur
WORKDIR /app

# Bir önceki aşamadan üretilen jar dosyasını kopyala
COPY --from=build /app/target/order-service-0.0.1-SNAPSHOT.jar order-service.jar

EXPOSE 8080

# Uygulamayı çalıştır
ENTRYPOINT ["java", "-jar", "order-service.jar"]