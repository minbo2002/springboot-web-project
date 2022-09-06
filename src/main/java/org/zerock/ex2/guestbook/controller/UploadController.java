package org.zerock.ex2.guestbook.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.ex2.guestbook.dto.UploadResultDTO;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
public class UploadController {

    @Value("${org.zerock.upload.path}")  // application.properties의 변수명
    private String uploadPath;


    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles) {

        List<UploadResultDTO> resultDTOList = new ArrayList<>();

        for(MultipartFile uploadFile : uploadFiles) {

            // MultipartFile에서 제공하는 getContentType() 메서드로 확장자 필터링
            if(uploadFile.getContentType().startsWith("image") == false) {
                log.warn("이 파일은 image 타입이 아닙니다");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            String originalName = uploadFile.getOriginalFilename();
            String fileName = originalName.substring(originalName.lastIndexOf("\\") +1);

            log.info("fileName = " + fileName);

            // 날짜 폴더 생성
            String folderPath = makeFolder();

            // UUID
            String uuid = UUID.randomUUID().toString();

            // 저장할 파일 이름 중간에 "_" 로 구분
            // File.separator : 실행중인 OS에 해당하는 파일구분자( / 혹은 \ )로 리턴
            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;

            Path savePath = Paths.get(saveName);

            try {
                uploadFile.transferTo(savePath);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    }

    // URL 인코딩된 파일이름을 파라미터로 받아서 해당 파일을 byte[]로 만들어서 브라우저로 전송하는 용도
    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName) {

        ResponseEntity<byte[]> result = null;

        try {
            String srcFileName = URLDecoder.decode(fileName, "UTF-8");
            log.info("fileName = " + srcFileName);

            File file = new File(uploadPath + File.separator + srcFileName);
            log.info("file = " + file);

            HttpHeaders header = new HttpHeaders();

            // MIME타입 처리
            header.add("Content-Type", Files.probeContentType(file.toPath()));
            // 파일 데이터 처리
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);

        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return result;
    }

    // 업로드 파일 삭제
    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFiles(String fileName) {

        String srcFileName = null;

        try {
            srcFileName = URLDecoder.decode(fileName, "UTF-8");
            File file = new File(uploadPath + File.separator + srcFileName);
            boolean result = file.delete();

            File thumbnail = new File(file.getParent(), "s_" + file.getName());
            result = thumbnail.delete();

            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String makeFolder() {

        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        // 폴더경로를     //에서 --> OS에 해당하는 파일구분자로 대체함
        String folderPath = str.replace("//", File.separator);

        // 폴더 생성
        File uploadPathFolder = new File(uploadPath, folderPath);

        if(uploadPathFolder.exists() == false) {
            uploadPathFolder.mkdirs();
        }

        return folderPath;
    }
}
