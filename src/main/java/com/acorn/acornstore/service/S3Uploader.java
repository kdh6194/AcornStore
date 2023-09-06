package com.acorn.acornstore.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3Client amazonS3Client;

    public List<String> upload(List<MultipartFile> files, String dirName) {
        List<String> fileUrls = new ArrayList<>();

        if (files == null || files.isEmpty()) {
            System.out.println("파일이 비어있습니다.");
            return fileUrls;
        }


        for (MultipartFile file : files) {
            //String fileName = generateFileName(file); // 파일내임
            try {
                //uploadFile(fileName, file);
//                File uploadFile = convert(file)
//                        .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));
                String fileUrl = upload(file, dirName);
                //removeNewFile(uploadFile); // 파일 업로드 후 로컬에 저장된 파일 삭제
                fileUrls.add(fileUrl);
            } catch (Exception e) {
                e.printStackTrace();
                // 업로드 실패 처리 로직 작성
            }
        }
//        File uploadFile = convert(multipartFile)
//                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));

        //return upload(uploadFile, dirName);
        return fileUrls;
    }

//    private String generateFileName(MultipartFile file) {
//        return System.currentTimeMillis() + "_" + file.getOriginalFilename();
//    }
private String upload(MultipartFile multipartFile, String dirName) throws IOException {
    UUID uuid = UUID.randomUUID();
    String originName = multipartFile.getOriginalFilename();
    String fileName = dirName + "/" + uuid + "_" + originName;

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType(multipartFile.getContentType());
    metadata.setContentLength(multipartFile.getSize());

    PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName,
            multipartFile.getInputStream(), metadata)
            .withCannedAcl(CannedAccessControlList.PublicRead);

    amazonS3Client.putObject(putObjectRequest);

    return amazonS3Client.getUrl(bucketName, fileName).toString();
}
//    private String upload(File uploadFile, String dirName) {
//        UUID uuid = UUID.randomUUID();
//        String originName = uploadFile.getName();
//        String imageFileName = dirName + "/" + uuid + "_" + originName;
//        String uploadImageUrl = putS3(uploadFile, imageFileName);
//        removeNewFile(uploadFile);
//        return uploadImageUrl;
////
////        String fileName = dirName + "/" + uuid.toString() + "_" + originName;
////
////        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName,
////                new FileInputStream(uploadFile))
////                .withCannedAcl(CannedAccessControlList.PublicRead);
////
////        amazonS3Client.putObject(putObjectRequest);
////
////        return amazonS3Client.getUrl(bucketName, fileName).toString();
//    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) {
//        File convertFile = new File(file.getOriginalFilename());
//        try {
//            if(convertFile.createNewFile()) {
//                try (FileOutputStream fos = new FileOutputStream(convertFile)) {
//                    fos.write(file.getBytes());
//                }
//                return Optional.of(convertFile);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        return Optional.empty();

        try {
            File convertedFile = File.createTempFile("temp", null);
            file.transferTo(convertedFile);
            return Optional.of(convertedFile);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

//
//    public List<String> uploadFiles(List<MultipartFile> files) {
//        List<String> fileUrls = new ArrayList<>();
//
//        if (files == null || files.isEmpty()) {
//            System.out.println("파일이 비어있습니다.");
//            return fileUrls;
//        }
//
//
//        for (MultipartFile file : files) {
//            String fileName = generateFileName(file); // 파일내임
//            try {
//                uploadFile(fileName, file);
//                String fileUrl = getFileUrl(fileName);
//                fileUrls.add(fileUrl);
//            } catch (IOException e) {
//                // 업로드 실패 처리 로직 작성
//            }
//        }
//
//        return fileUrls;
//    }
//
//    private String generateFileName(MultipartFile file) {
//        return System.currentTimeMillis() + "_" + file.getOriginalFilename();
//    }
//
//    private void uploadFile(String fileName, MultipartFile multipartFile) throws IOException {
//        ObjectMetadata metadata = new ObjectMetadata();
//        metadata.setContentType(multipartFile.getContentType());
//        metadata.setContentLength(multipartFile.getSize());
//
//        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName,
//                multipartFile.getInputStream(), metadata);
//
//        putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);  // 업로드된 파일에 대한 ACL 설정 (Public Read)
//
//        amazonS3Client.putObject(putObjectRequest);
//    }
//
//    private String getFileUrl(String fileName) {
//        return amazonS3Client.getUrl(bucketName, fileName).toString();
//    }
}
