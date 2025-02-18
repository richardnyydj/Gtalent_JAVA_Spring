FROM openjdk:17-slim

# 設定環境變數，方便後續使用
ENV MAVEN_VERSION=3.9.9
ENV MAVEN_HOME=/opt/apache-maven-${MAVEN_VERSION}
ENV PATH=${MAVEN_HOME}/bin:${PATH}

# 設定工作目錄
WORKDIR /app

# 將工作目錄下的所有檔案複製到容器中
COPY . .

# 安裝 Maven
RUN apt-get update && apt-get install -y curl

# 下載、解壓縮 Maven，並設定連結
RUN curl -fsSL https://downloads.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz | tar -xz -C /opt
RUN ln -s ${MAVEN_HOME}/bin/mvn /usr/bin/mvn

# 驗證 Maven 安裝
RUN mvn -version

# 清理和建構
RUN mvn clean package -DskipTests

# 將 JAR 檔複製到根目錄，並更名為 app.jar
RUN cp target/*.jar app.jar

# image要開啟的port
EXPOSE 8080 443

# 設定 keystore 讀取的環境變數（可透過 docker run -e 覆蓋）
ENV KEYSTORE_PATH=classpath:cert/keystore.p12
ENV KEYSTORE_PASSWORD=changeit

# 啟動應用程式，從環境變數讀取 keystore
ENTRYPOINT ["sh", "-c", "java -Djavax.net.ssl.keyStore=${KEYSTORE_PATH} -Djavax.net.ssl.keyStorePassword=${KEYSTORE_PASSWORD} -jar app.jar"]