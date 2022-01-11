package com.example.foodmap.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class StorageService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 s3Client;
    public static final String CLOUD_FRONT_DOMAIN_NAME = "https://djefjounyx85c.cloudfront.net";

    public String uploadFile(MultipartFile file, String dirName) {
        log.info(file.getName());

        File fileObj = convertMultiPartFileToFile(file);
        String originalFilename = file.getOriginalFilename();

        //이미지 이름의 중복을 막고자 고유식별자 UUID 생성해서 파일이름앞에 붙여줌.
        String fileName =  dirName + "/" + UUID.randomUUID() + "_" + originalFilename;
        s3Client.putObject(new PutObjectRequest(bucket, fileName, fileObj)); //s3로 업로드
        fileObj.delete();
        return fileName;
    }

    //s3에서 파일 삭제하기
    public void deleteFile(String fileName) {
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, fileName);
        s3Client.deleteObject(deleteObjectRequest);
    }

    private File convertMultiPartFileToFile(MultipartFile file){
        File convertedFile = new File(file.getOriginalFilename());

        //데이터를 파일에 바이트 스트림으로 저장하기 위해 사용
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }
}