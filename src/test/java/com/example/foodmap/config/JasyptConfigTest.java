package com.example.foodmap.config;


import org.assertj.core.api.Assertions;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;

class JasyptConfigTest {
    @Test
    void jasypt(){
        String url = "secret_url";
        String username = "secret_username";
        String password = "secret_password";
        String accessKey = "secret_accessKey";
        String secretKey = "secret_secretKey";
        String dsn = "secret_dsn";

        String encryptUrl = jasyptEncrypt(url);
        String encryptUsername = jasyptEncrypt(username);
        String encryptPassword = jasyptEncrypt(password);
        String encryptAccessKey = jasyptEncrypt(accessKey);
        String encryptSecretKey = jasyptEncrypt(secretKey);
        String encryptDsn = jasyptEncrypt(dsn);

        System.out.println("encryptUrl : " + encryptUrl);
        System.out.println("encryptUsername : " + encryptUsername);
        System.out.println("encryptPassword : " + encryptPassword);
        System.out.println("encryptAccessKey : " + encryptAccessKey);
        System.out.println("encryptSecretKey : " + encryptSecretKey);
        System.out.println("encryptDsn : " + encryptDsn);

        Assertions.assertThat(url).isEqualTo(jasyptDecryt(encryptUrl));
    }

    private String jasyptEncrypt(String input) {
        String key = "PASSWORD";
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword(key);
        return encryptor.encrypt(input);
    }

    private String jasyptDecryt(String input){
        String key = "PASSWORD";
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword(key);
        return encryptor.decrypt(input);
    }

}
