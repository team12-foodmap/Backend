package com.example.foodmap.service;

import com.example.foodmap.config.EmbeddedS3Config;
import io.findify.s3mock.S3Mock;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(EmbeddedS3Config.class)
class StorageServiceTest {
    @Autowired
    S3Mock s3Mock;

    @Autowired
    StorageService storageService;

    @Test
    public void test() throws IOException {
        String expected = "mock1.png";
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", expected,
                "image/png", "test data".getBytes());
        String imagePath = storageService.uploadFile(mockMultipartFile, "restaurant");

        assertThat(mockMultipartFile.getOriginalFilename()).isEqualTo(expected);
    }

    @After("")
    public void shutdownMockS3(){
        s3Mock.stop();
    }
}
