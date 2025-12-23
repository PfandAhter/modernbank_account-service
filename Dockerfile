# 1. Java 17 tabanlı hafif Alpine imajını kullanıyoruz
FROM eclipse-temurin:17-jdk-alpine

# 2. Çalışma klasörünü ayarla
WORKDIR /app

# 3. Maven ile oluşan jar dosyasını kopyala
# target altındaki .jar dosyasını alıp konteyner içine 'app.jar' adıyla atar
COPY target/*.jar app.jar

# 4. Portu belirt (Account Service 8084 portunda çalışıyor)
EXPOSE 8084

# 5. Uygulamayı başlat
ENTRYPOINT ["java", "-jar", "app.jar"]