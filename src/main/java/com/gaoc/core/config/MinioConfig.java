package com.gaoc.core.config;

import com.gaoc.core.model.MinioTool;
import com.gaoc.core.properties.BaseProperties;
import com.gaoc.core.properties.MinioProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
@SpringBootConfiguration
public class MinioConfig {

    private final BaseProperties baseProperties;

    @Bean
    public MinioTool minioTool() {
        MinioProperties minio = baseProperties.getMinio();
        String host = minio.getHost();
        int port = minio.getPort();
        String accessKey = minio.getAccessKey();
        String secretKey = minio.getSecretKey();
        return new MinioTool(host, port, accessKey, secretKey);
    }

}
