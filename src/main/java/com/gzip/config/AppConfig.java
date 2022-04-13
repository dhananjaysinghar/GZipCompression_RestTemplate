package com.gzip.config;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Configuration
public class AppConfig {

    @Bean("gzipRestTemplate")
    public RestTemplate gzipRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            HttpHeaders httpHeaders = request.getHeaders();
            httpHeaders.add(HttpHeaders.CONTENT_ENCODING, "gzip");
            httpHeaders.add(HttpHeaders.ACCEPT_ENCODING, "gzip");
            return execution.execute(request, compress(body));
        });
        return restTemplate;
    }

    private byte[] compress(byte[] body) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(baos)) {
            gzipOutputStream.write(body);
        }
        return baos.toByteArray();
    }

    @SneakyThrows
    public static String decompressGzipByteArray(byte[] bytes) {
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(bytes));
        return new String(gis.readAllBytes());
    }


}
