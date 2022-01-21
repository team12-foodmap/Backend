package com.example.foodmap.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableEncryptableProperties
@Configuration
public class JasyptConfig {

    // 시스템에서 지정한 변수로 설정("시스템 환경 변수 편집" -> IntelliJ의 경우 재부팅해야 적용)
    // Run -> Edit Configurations -> MbtiApplication -> VM Options: -Djasypt.encryptor.password=
    @Value("${jasypt.encryptor.password}")
    private String PASSWORD;

    // jasypt 3.0 version 이상에서는 encryptorBean 대신 jasyptStringEncryptor를 써야 오류가 나지 않는다. // jasypt.encryptor.password
    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor(){
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(PASSWORD);
        config.setPoolSize("1");
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setStringOutputType("base64");
        config.setKeyObtentionIterations("1000");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        encryptor.setConfig(config);
        return encryptor;
    }
}
