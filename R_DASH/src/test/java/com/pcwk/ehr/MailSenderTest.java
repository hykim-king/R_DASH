package com.pcwk.ehr;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import com.pcwk.ehr.service.MailService;
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml" })
class MailSenderTest {
	@Autowired
    JavaMailSender mailSender;
	@Autowired
	MailService mailService;
	@Autowired
	ApplicationContext context;

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	//@Disabled
	@Test 
	void verify(){
		int flag = mailService.verify("etbonhong9@gmail.com", "97926965");
		assertEquals(1, flag);
	}
	@Disabled
	@Test
	void test() {
		mailService.sendcode("etbonhong9@gmail.com");
	}
	
	
	@Disabled
	@Test
	void sendMail() {
		assertNotNull(mailSender);
		SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("etbonhong9@gmail.com");
        msg.setFrom("2025rdash@gmail.com");
        msg.setSubject("SMTP 테스트 메일");
        msg.setText("지메일 SMTP + 스프링 메일 연동 테스트");
        mailSender.send(msg);
        System.out.println("메일 발송 완료!");
	}

}
