package com.pcwk.ehr.service;

import java.security.SecureRandom;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.pcwk.ehr.domain.UserDTO;
import com.pcwk.ehr.mapper.UserMapper;

@Service
public class MailServiceImpl implements MailService {
	@Autowired
	StringRedisTemplate redis;
	@Autowired
	JavaMailSender mail;
	@Autowired
	UserService userService;
	@Autowired
	UserMapper userMapper;

	private static final long CODE_TTL_SEC = 180; // 3분
	private static final String CODE_KEY = "pw:code:"; // + email

	// 메일 전송
	@Override
	public void sendcode(String email) {
		// 6자리 난수 코드
		String code = Integer.toString((int) (Math.random() * 899999) + 100000);
		redis.opsForValue().set(CODE_KEY + email, code, Duration.ofSeconds(CODE_TTL_SEC));

		SimpleMailMessage m = new SimpleMailMessage();
		m.setTo(email);
		m.setSubject("[재민이] 인증코드 (3분 유효)");
		m.setText("아래 인증코드를 3분 안에 입력하세요.\n\n코드: " + code);
		mail.send(m);

	}

	// 코드 인증
	@Override
	public int verify(String email, String codeInput) {
		int flag = 1;
		String key = CODE_KEY + email;
		// 발급된 코드
		String saved = redis.opsForValue().get(key);
		if (saved == null || !saved.equals(codeInput)) {
			flag = 0;
			return flag;
		}
		// 코드 소모(재사용 방지)
		redis.delete(key);

		return flag;
	}

	@Override
	public void resetPwToMail(String email) {
		// 8자리 비밀번호
		String resetPw = generatePw();
		
		UserDTO user = new UserDTO();
		int userNo = userMapper.getUserNo(email);
		user.setUserNo(userNo);
		user.setPassword(resetPw);
		//비밀번호 초기화
		userService.updateUserInfo(user);
		
		// 임시 비번 메일 통지
        SimpleMailMessage m = new SimpleMailMessage();
        m.setTo(email);
        m.setSubject("[재민이] 임시 비밀번호 안내");
        m.setText("임시 비밀번호: " + resetPw + "\n로그인 후 반드시 새 비밀번호로 변경해주세요.");
        mail.send(m);
	}

	// 랜덤비밀번호 생성
	public static String generatePw() {
		String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";

		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 8; i++) {
			int index = random.nextInt(charSet.length());
			sb.append(charSet.charAt(index));
		}
		return sb.toString();
	}

}
