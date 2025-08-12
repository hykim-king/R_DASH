package com.pcwk.ehr.domain;

public class ApiKeyCheck {
	public static void main(String[] args) {
		String apiKey = System.getenv("OPENAI_API_KEY");
		if (apiKey != null) {
			System.out.println("✅ API 키 불러오기 성공: " + apiKey.substring(0, 10) + "...(생략)");
		} else {
			System.out.println("❌ API 키를 읽어올 수 없습니다.");
		}
	}
}