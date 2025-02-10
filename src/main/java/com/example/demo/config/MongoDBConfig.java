package com.example.demo.config;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
@EnableMongoAuditing
public class MongoDBConfig {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(
            MongoClientSettings.builder()
                .applyConnectionString(new com.mongodb.ConnectionString(mongoUri))
                .applyToConnectionPoolSettings(builder ->
                    builder
                        .minSize(5)  // 最小連接數
                        .maxSize(50) // 最大連接數
                        .maxWaitTime(5000, TimeUnit.MILLISECONDS) // 最大等待時間
                        .maxConnectionIdleTime(30000, TimeUnit.MILLISECONDS) // 最大閒置時間
                        .maxConnectionLifeTime(60000, TimeUnit.MILLISECONDS) // 最大存活時間
                )
                .build());
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), databaseName);
    }
}
