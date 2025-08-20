package com.pcwk.ehr.security;

import org.springframework.stereotype.Component;

import com.pcwk.ehr.domain.UserDTO;
import com.pcwk.ehr.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.*;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	@Autowired
	UserService service; // findByGoogleId, findByEmail 구현

	@Override
	public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth)
			throws IOException, ServletException {
		OAuth2User o = (OAuth2User) auth.getPrincipal();
		OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) auth;
		String social = token.getAuthorizedClientRegistrationId(); // "google" or "kakao"
		
		Map<String, Object> attrs = o.getAttributes();

		String email = "";
		String name = "";

		if (social.equals("google")) {
			
			email = (String) attrs.get("email");
            name  = (String) attrs.get("name");
            
		} else if (social.equals("kakao")) {
			
			@SuppressWarnings("unchecked")
			Map<String, Object> account = (Map<String, Object>) attrs.get("kakao_account");
            if (account != null) {
                email = (String) account.get("email"); // 동의 안 했으면 null

                @SuppressWarnings("unchecked")
                Map<String, Object> profile = (Map<String, Object>) account.get("profile");
                if (profile != null) {
                    // 권한이 있다면 name, 아니면 nickname
                    name = (String) profile.get("name");
                    if (name == null) name = (String) profile.get("nickname");
                }
                // 일부 앱 설정에선 account.name 으로도 올 수 있음
                if (name == null) name = (String) account.get("name");
            }
		}

		UserDTO user = service.socialLogin(email, name, social);

		// 세션 저장(선택)
		req.getSession(true).setAttribute("loginUser", user);

		// 화면 이동
		setDefaultTargetUrl("/home");
		super.onAuthenticationSuccess(req, res, auth);
	}
}
