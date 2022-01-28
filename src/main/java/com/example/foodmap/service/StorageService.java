package com.example.foodmap.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
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

        String originalFilename = file.getOriginalFilename();
        //이미지 이름의 중복을 막고자 고유식별자 UUID 생성해서 파일이름앞에 붙여줌.
        String fileName =  dirName + "/" + UUID.randomUUID() + "_" + originalFilename;

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata ));
        } catch (IOException e) {
            throw new IllegalArgumentException(
                    String.format(
                            "파일 변환 중 에러가 발생하였습니다. (%s)", file.getOriginalFilename()
                    )
            );
        }
        return fileName;
    }

    //이미지 삭제 및 업로드
    public String updateFile(String oldUrl, MultipartFile file, String dirName) {

        if(oldUrl != null && !oldUrl.isEmpty()) {
            boolean isExistObject = s3Client.doesObjectExist(bucket, oldUrl);

            if (isExistObject) {
                deleteFile(oldUrl);
            }
        }
        return uploadFile(file, dirName);
    }

    //s3에서 파일 삭제하기
    public void deleteFile(String fileName) {
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, fileName);
        s3Client.deleteObject(deleteObjectRequest);
    }

}