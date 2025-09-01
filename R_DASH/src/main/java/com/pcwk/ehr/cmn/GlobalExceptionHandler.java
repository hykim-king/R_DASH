package com.pcwk.ehr.cmn;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException; // 쓰고 있으면
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	Logger log = LogManager.getLogger(getClass());
	
	private String render(int status, String msg, Model model, HttpServletResponse resp) {
		resp.setStatus(status);
		model.addAttribute("status", status);
		model.addAttribute("message", msg);
		return "exception/error"; // Tiles definition
	}

	// 404: 스프링 내부에서 난 404 (web.xml에서 throwExceptionIfNoHandlerFound=true 일 때)
	@ExceptionHandler(NoHandlerFoundException.class)
	public String handle404(NoHandlerFoundException ex, Model model, HttpServletResponse resp) {
		return render(HttpStatus.NOT_FOUND.value(), "요청하신 페이지를 찾을 수 없습니다.", model, resp);
	}

	// 405: 허용되지 않은 메서드
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public String handle405(HttpRequestMethodNotSupportedException ex, Model model, HttpServletResponse resp) {
		return render(HttpStatus.METHOD_NOT_ALLOWED.value(), "허용되지 않은 요청 방식입니다.", model, resp);
	}

	// 400: 파라미터/바인딩/유효성 오류
	@ExceptionHandler({ MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class,
			MethodArgumentNotValidException.class, BindException.class })
	public String handle400(Exception ex, Model model, HttpServletResponse resp) {
		return render(HttpStatus.BAD_REQUEST.value(), "요청 값이 올바르지 않습니다.", model, resp);
	}

	// 403: 접근 거부 (Spring Security 사용 시)
	@ExceptionHandler(AccessDeniedException.class)
	public String handle403(AccessDeniedException ex, Model model, HttpServletResponse resp) {
		return render(HttpStatus.FORBIDDEN.value(), "접근 권한이 없습니다.", model, resp);
	}

	// 413: 업로드 용량 초과
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String handle413(MaxUploadSizeExceededException ex, Model model, HttpServletResponse resp) {
		return render(HttpStatus.PAYLOAD_TOO_LARGE.value(), "업로드 가능한 파일 용량을 초과했습니다.", model, resp);
	}

	// 415: 지원하지 않는 미디어 타입
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public String handle415(HttpMediaTypeNotSupportedException ex, Model model, HttpServletResponse resp) {
		return render(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "지원하지 않는 형식입니다.", model, resp);
	}

	// 그 외 전부 → 500
	@ExceptionHandler(Throwable.class)
	public String handle500(Throwable ex, Model model, HttpServletResponse resp) {
		String ref = UUID.randomUUID().toString().substring(0, 8); // 로그 추적용 ID
		log.error("[ERR:{}] Unhandled exception", ref, ex);
		return render(HttpStatus.INTERNAL_SERVER_ERROR.value(), "처리 중 오류가 발생했습니다.", model, resp);
	}
}
