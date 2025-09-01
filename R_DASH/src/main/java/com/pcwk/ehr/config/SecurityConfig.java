package com.pcwk.ehr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	// [1] 소셜 로그인 전용 체인 (해당 경로에서만 Spring Security가 동작)
	@Bean
	SecurityFilterChain oauth2Chain(HttpSecurity http,
			@org.springframework.beans.factory.annotation.Qualifier("exceptionResolver") org.springframework.web.servlet.HandlerExceptionResolver resolver,
			org.springframework.security.web.authentication.AuthenticationSuccessHandler successHandler)
			throws Exception {
		http.securityMatcher("/oauth2/**", "/login/**", "/user/logout") // OAuth2 시작/콜백/로그아웃만 관여
				.authorizeHttpRequests(auth -> auth.anyRequest().permitAll()).csrf(csrf -> csrf.disable()) // 로그인/콜백은 주로
																											// GET, 간단히
																											// 비활성화
				.oauth2Login(o -> o.loginPage("/user/login") // 커스텀 로그인 페이지 (없으면 Spring 기본 페이지 사용)
						.successHandler(successHandler).failureHandler((req, res, ex) -> {
							// 컨트롤러 어드바이스로 위임하고 싶으면:
							resolver.resolveException(req, res, null, ex);
							// 또는 원하는 에러 페이지로 리다이렉트:
							// res.sendRedirect("/error/oauth");
						}))
				.logout(l -> l.logoutSuccessUrl("/home").logoutUrl("/user/logout").clearAuthentication(true)
						.invalidateHttpSession(true).deleteCookies("JSESSIONID") // GET
				// 로그아웃 지원(선택)
				);

		return http.build();
	}

	// [2] 나머지 전부: 보안 개입 없이 허용 (단, 보안 컨텍스트는 유지되어 로그인 정보 주입됨)
	@Bean
	SecurityFilterChain passThroughChain(HttpSecurity http) throws Exception {
		http.securityMatcher("/**") // 위 [1]에 매칭되지 않는 모든 경로
				.authorizeHttpRequests(auth -> auth.anyRequest().permitAll()).csrf(csrf -> csrf.disable()); // 폼 CSRF가
																											// 필요하면
																											// CookieCsrfTokenRepository로
																											// 바꾸세요
		return http.build();
	}

	// 클라이언트 등록(메모리)
	@Bean
	public ClientRegistrationRepository clientRegistrationRepository() {
		// --- Google ---
		ClientRegistration google = ClientRegistration.withRegistrationId("google")
				.clientId("76829955490-7l6351e9bdqem1an3m6obkb4arveo9h0.apps.googleusercontent.com")
				.clientSecret("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")
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.redirectUri("{baseUrl}/login/oauth2/code/{registrationId}").scope("openid", "email", "profile")
				.authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
				.tokenUri("https://oauth2.googleapis.com/token")
				.userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo").userNameAttributeName("sub")
				.jwkSetUri("https://www.googleapis.com/oauth2/v3/certs").clientName("Google").build();

		// --- Kakao ---
		ClientRegistration kakao = ClientRegistration.withRegistrationId("kakao")
				.clientId("dd84ad355ab58392195f5025411ef93b") // 카카오 앱의 REST API 키
				.clientSecret("iDaG83N3GxPJYbtwsjcIBUss2pwlUNFX") // (선택/권장) 사용 중이면 입력
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST) // 카카오는 보통 POST 추천
				.redirectUri("{baseUrl}/login/oauth2/code/{registrationId}").scope("profile_nickname", "account_email") // 필요한
																														// 범위만
				.authorizationUri("https://kauth.kakao.com/oauth/authorize")
				.tokenUri("https://kauth.kakao.com/oauth/token").userInfoUri("https://kapi.kakao.com/v2/user/me")
				.userNameAttributeName("id") // 카카오의 고유 키
				.clientName("Kakao").build();
		return new InMemoryClientRegistrationRepository(google, kakao);
	}

	@Bean
	public OAuth2AuthorizedClientService authorizedClientService(ClientRegistrationRepository repo) {
		return new InMemoryOAuth2AuthorizedClientService(repo);
	}
}