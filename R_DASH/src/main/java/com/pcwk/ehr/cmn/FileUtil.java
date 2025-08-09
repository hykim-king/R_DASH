package com.pcwk.ehr.cmn;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class FileUtil {
	
	public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        
        if (file.exists()) {
            return file.delete();
        } else {
            System.out.println("파일이 존재하지 않습니다: " + filePath);
            return false;
        }
    }
	
    public static String saveFileWithUUID(MultipartFile file, String uploadDir) {
        if (file.isEmpty()) return null;
 
        try {
            // 1. 디렉토리 생성
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            // 2. 확장자 추출
            String originalFilename = file.getOriginalFilename();
            String extension = "";

            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex > 0 && dotIndex < originalFilename.length() - 1) {
                extension = originalFilename.substring(dotIndex);
            }
            
            // 3. UUID 생성 + 확장자 붙이기
            String uuidFilename = UUID.randomUUID().toString() + extension;

            // 4. 저장
            File target = Paths.get(uploadDir, uuidFilename).toFile();

            try (
                BufferedInputStream bis = new BufferedInputStream(file.getInputStream());
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(target))
            ) {
                byte[] buffer = new byte[8192];
                int len;
                while ((len = bis.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
            }

			return uuidFilename;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
	
}
