package com.pcwk.ehr.controller;

import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ExceptionTestController {
	// 500: 강제 런타임 예외
	@GetMapping("/boom")
	@ResponseBody
	public void boom() {
		throw new RuntimeException("테스트 500");
	}

	// 405: POST만 허용 → GET으로 때리면 405
	@PostMapping("/method")
	@ResponseBody
	public void onlyPost() {
	}

	// 400: 필수 파라미터 누락 → MissingServletRequestParameterException
	@GetMapping("/req")
	@ResponseBody
	public void missingParam(@RequestParam("q") String q) {
	}

	// 400: 타입 불일치 → MethodArgumentTypeMismatchException
	@GetMapping("/type")
	@ResponseBody
	public void typeMismatch(@RequestParam("id") int id) {
	}

	// 400(검증 실패, @ModelAttribute) → BindException
	@PostMapping("/bind")
	@ResponseBody
	public void bind(@Valid Form form) {
	}

	// 400(검증 실패, @RequestBody) → MethodArgumentNotValidException
	@PostMapping(value = "/validate", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void validate(@Valid @RequestBody UserDto dto) {
	}

	// 415: JSON만 허용 → 다른 Content-Type 보내면 HttpMediaTypeNotSupportedException
	@PostMapping(value = "/json", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void jsonOnly(@RequestBody Map<String, Object> body) {
	}

	// 413: 업로드 용량 초과 → MaxUploadSizeExceededException(Resolver 설정대로)
	@PostMapping("/upload")
	@ResponseBody
	public void upload(@RequestParam("file") MultipartFile file) {
	}

	// 403: 접근 거부 (Security 없어도 수동으로 던져 테스트 가능)
	@GetMapping("/forbidden")
	@ResponseBody
	public void forbidden() {
		throw new AccessDeniedException("테스트 403");
	}

	// DTO들
	public static class Form {
		@Min(1)
		@Max(120)
		private Integer age;

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}
	}

	public static class UserDto {
		@NotBlank
		private String name;
		@Min(1)
		private Integer age;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}
	}
}
