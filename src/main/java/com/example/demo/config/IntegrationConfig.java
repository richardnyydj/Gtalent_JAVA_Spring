package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;

import java.io.File;

@Configuration
@EnableIntegration
public class IntegrationConfig {

    // input資料夾目錄
    @Value("${file.input.directory:src/main/resources/input-directory}")
    private String inputDirectory;

    // output資料夾目錄
    @Value("${file.output.directory:src/main/resources/output-directory}")
    private String outputDirectory;

    // 文件讀取通道
    @Bean
    public MessageChannel inputChannel() {
        return new DirectChannel();
    }

    // 文件來源，從指定目錄讀取文件
    @Bean
    public FileReadingMessageSource fileReadingMessageSource() {
        FileReadingMessageSource source = new FileReadingMessageSource();
        File inputDir = new File(inputDirectory);
        // 確保目錄存在
        if (!inputDir.exists()) {
            inputDir.mkdirs();
        }
        source.setDirectory(inputDir);
        source.setFilter(new SimplePatternFileListFilter("*.txt"));
        return source;
    }

    // 處理從文件中讀取的消息，並寫入到output資料夾中
    @Bean
    public MessageHandler fileWriter() {
        File outputDir = new File(outputDirectory);
        // 確保輸出目錄存在
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        FileWritingMessageHandler handler = new FileWritingMessageHandler(outputDir);
        // 設置自定義文件名生成規則，這裡根據消息的有效負載生成文件名
        handler.setFileNameGenerator(message -> {
            File file = (File) message.getPayload();
            return file.getName();  // 使用原始文件名
        });

        handler.setExpectReply(false);
        return handler;
    }

    // 設置文件源和處理器之間的整合
    @Bean
    public IntegrationFlow fileIntegrationFlow() {
        return IntegrationFlows.from(fileReadingMessageSource(),
                c -> c.poller(Pollers.fixedDelay(5000)
                        .maxMessagesPerPoll(10)))  // 每次輪詢的消息數上限(10次)
                .channel(inputChannel())
                .handle(fileWriter())
                .get();
    }
}