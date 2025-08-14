package com.pcwk.ehr.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class SummernoteController {

	public SummernoteController() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@PostMapping("/summernoteImage")
    @ResponseBody
    public Map<String, Object> uploadSummernoteImage(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 파일 저장 로직
            String savedUrl = saveFileAndReturnUrl(file);
            result.put("url", savedUrl);
            result.put("responseCode", "success");
        } catch (Exception e) {
            result.put("responseCode", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }
    public String saveFileAndReturnUrl(MultipartFile file) throws IOException {
        // 1. 업로드 경로 설정 (서버 기준 절대경로)
        String uploadDir = "C:/Users/user/R_DASH/R_DASH/src/main/webapp/resources/uploads/summernoteImage"; // 원하는 경로로 변경 가능
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs(); // 폴더 없으면 생성
        }

        // 2. 원본 파일 이름
        String originalFilename = file.getOriginalFilename();

        // 3. 중복 방지를 위해 UUID 적용
        String uuid = UUID.randomUUID().toString();
        String ext = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String savedFilename = uuid + ext;

        // 4. 파일 저장
        File targetFile = new File(dir, savedFilename);
        file.transferTo(targetFile);

        // 5. URL 반환 (웹에서 접근할 수 있는 경로)
        // 예: 서버가 http://localhost:8080/ 일 때
        String fileUrl = "/ehr/resources/uploads/summernoteImage/" + savedFilename; 
        return fileUrl;
    }

}
