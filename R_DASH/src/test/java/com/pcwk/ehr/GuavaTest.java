package com.pcwk.ehr;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.google.common.base.Strings;

public class GuavaTest {
	/**
	 * null,"" 입력되면 default value로 변경
	 * @param value
	 * @param defaultValue
	 * @return String
	 */
	public static String nvlString(String value, String defaultValue) {
		if(Strings.isNullOrEmpty(value) == true) {
			return defaultValue;
		}
		
		return value;
	}
	/**
	 * 당일 기준 날짜 문자 생성
	 * @param pattern
	 * @return String(당일 기준 날짜)
	 */
	public static String getCurrentDate(String pattern) {
		pattern = nvlString(pattern,"yyyy/MM/dd");
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
	}
	
	public static String getExt(String fileName) {
		String ext = "";
		
		if(fileName.lastIndexOf(".")>-1) {
			int dotWhich  =fileName.lastIndexOf(".");
			ext = fileName.substring(dotWhich);
		}
		
		return ext;
	}
	
	public static String getPK(String pattern) {
		return getCurrentDate(pattern)+"_"+getUUID();
	}
	
	public static String getUUID() {
		return UUID.randomUUID().toString();
	}
	public static boolean isImageExtension(String fileName) {
	    String ext = getExt(fileName).toLowerCase();  // 확장자를 소문자로 변환
	    return ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png")
	        || ext.equals("gif") || ext.equals("bmp") || ext.equals("webp");
	}

	public static void main(String[] args) {
		String file = "hi.png";
		
		String notFile = "bye.pnng";
		
		String notExt = "bye";
		
		System.out.println(isImageExtension(file)); //true
		System.out.println(isImageExtension(notFile)); //false
		System.out.println(isImageExtension(notExt)); //false
		System.out.println(getExt(file));

	}

}
