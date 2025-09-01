package com.pcwk.ehr.task;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component(value = "newsCron")
public class newsCron {
	
	Logger log = LogManager.getLogger(getClass());


	public newsCron() {
		log.debug("┌───────────────────────────┐");
		log.debug("│ *newsCron()*              │");
		log.debug("└───────────────────────────┘");
	}
	
	public void runNewsTask() {
		LocalDateTime  localDateTime =LocalDateTime.now();
		log.debug("┌────────────────────────┐");
		log.debug("│ newsCron.runNewsTask() │");
		log.debug("└────────────────────────┘");
		
		try {
			 // python 파일 경로 (절대경로 권장)
	        String[] command = {"python", "C:\\Users\\user\\R_DASH\\R_DASH_PY\\news\\naver_news.py"};
	        ProcessBuilder pb = new ProcessBuilder(command);
	        
	        File workDir = new File("C:\\Users\\user\\R_DASH\\R_DASH_PY\\topic");
            pb.directory(workDir);

            // stdout/stderr 읽기 위해 redirect 설정
            pb.redirectErrorStream(true);

            Process process = pb.start();

            // Python stdout 읽어서 로그 출력
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.debug("Python: " + line);
                }
            }

            int exitCode = process.waitFor();
            log.debug("Python 프로세스 종료, exit code = " + exitCode);
            log.debug("Python 크롤러 실행 완료: {}", LocalDateTime.now());
			
		}catch(Exception e) {
			log.error("Python 실행 중 오류", e);
		}
	}

}
