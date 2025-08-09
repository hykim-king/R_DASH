package com.pcwk.ehr.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcwk.ehr.Response.NowcastApiResponse;
import com.pcwk.ehr.Response.PatientsApiResponse;
import com.pcwk.ehr.domain.NowcastDTO;
import com.pcwk.ehr.domain.PatientsDTO;
import com.pcwk.ehr.mapper.TemperatureMapper;

@Service
public class TemperatureServiceImpl implements TemperatureService {

	@Autowired
    private RestTemplate restTemplate;
	
	@Autowired
	private ServletContext servletContext;
	
	@Autowired
	private TemperatureMapper temperatureMapper;
	
    private static final String PATIENTES_URL = "http://apis.data.go.kr/1741000/HeatWaveCasualtiesRegion/getHeatWaveCasualtiesRegionList";
    private static final String NOWCAST_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst";
    private static final String SERVICE_KEY = "VJxg5p3Iyzp7FA0pzgtVA7AYRfaM2YSuLU4h8TMQAvQIJMGkIN7qEpL%2FQoDBEqo1MnsWnxGR%2BlN%2F9SsKlSmbZg%3D%3D";
    private static Map<String, List<Map<String, String>>> locationCache = new ConcurrentHashMap<>();
    
    
    //한번 호출되면 캐시를 채우는 로직, insert를 할 때마다 readLocation호출해오는건 비효율적.
    public TemperatureServiceImpl() {
    	if (locationCache.isEmpty()) { 
            try {
                TemperatureServiceImpl.readLocation(); 
            } catch (IOException e) {
                System.err.println("위치 데이터 캐시 로드 중 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    
    public TemperatureServiceImpl(RestTemplate restTemplate, TemperatureMapper temperatureMapper ) {
        this.restTemplate = restTemplate;
        this.temperatureMapper  = temperatureMapper ;
    }
    
    /*
     * 시도, 시군구, nx, ny를 엑셀에서 읽어오는 메서드
     */
    public static List<Map<String, String>> readLocation() throws IOException {
    	List<Map<String, String>> result = new ArrayList<>();
        Set<String> sidoKeys = new HashSet<>();
        Set<String> xyKeys = new HashSet<>(); 
        
        try (InputStream is = TemperatureServiceImpl.class.getClassLoader().getResourceAsStream("excel/Nowcast.xlsx")) {
            if (is == null) {
                throw new IOException("파일을 찾을 수 없습니다: excel/Nowcast.xlsx");
            }
            
            try (Workbook workbook = new XSSFWorkbook(is)) {

               Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트
               Iterator<Row> rowIterator = sheet.iterator();

               // 첫 번째 행(헤더) 스킵
               if (rowIterator.hasNext()) {
                   rowIterator.next();
               }

               while (rowIterator.hasNext()) {
                   Row row = rowIterator.next();

                   String sido = getCellValue(row.getCell(2)); // 1단계
                   String sigungu = getCellValue(row.getCell(3)); // 2단계
                   String nx = getCellValue(row.getCell(5)); // 격자 X
                   String ny = getCellValue(row.getCell(6)); // 격자 Y

                   // 시군구명이 비어있으면 스킵
                   if (sigungu == null || sigungu.isEmpty() || nx.isEmpty() || ny.isEmpty()) { continue; }

                   // 시도 + 시군구 중복 제거
                   String key = sido + "_" + sigungu;
                   if (!sidoKeys.contains(key)) {
                	   sidoKeys.add(key);

                       Map<String, String> map = new HashMap<>();
                       map.put("sido", sido);
                       map.put("sigungu", sigungu);
                       map.put("nx", nx);
                       map.put("ny", ny);

                       result.add(map);
                   }
                   
                   // 현재 읽은 엑셀 행이 (시도, 시군구, nx, ny)까지 완전히 동일한 중복이라면 스킵
                   String fullRowKey = sido + "_" + sigungu + "_" + nx + "_" + ny;
                   if (xyKeys.contains(fullRowKey)) {
                       continue;
                   }
                   xyKeys.add(fullRowKey);

                   String xyKey = nx + "_" + ny; // **nx_ny를 Map의 키로 사용**
                   
                   // locationCache에 xyKey가 없으면 새로운 ArrayList를 값으로 넣음
                   locationCache.putIfAbsent(xyKey, new ArrayList<>());
                   
                   Map<String, String> locationInfo = new HashMap<>();
                   locationInfo.put("sido", sido);
                   locationInfo.put("sigungu", sigungu);
                   
                   locationCache.get(xyKey).add(locationInfo);
               }
            }
        }
        return result;
    }
    
    /*
     * 엑셀에서 셀 값을 가져오는 메서드
     */
    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        // 숫자 값 처리
        if (cell.getCellType() == CellType.NUMERIC) {
        	// Date포맷 True로 반환하면 날짜로 간주해서 문자열로 변환
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue().toString();
            } else {
            	// 정수로 변환, (소수점은 문자열)
                return String.valueOf((int) Math.round(cell.getNumericCellValue()));
            }
        } else {
            return cell.toString().trim();
        }
    }
    
    /*
     * Base_Date와 Base_Time을 구하는 메서드
     */
    public String[] getBaseDateTime() {
        LocalDateTime now = LocalDateTime.now();
        int currentHour = now.getHour();
        int currentMinute = now.getMinute();

        String baseDate;
        String baseTime;

        if (currentMinute < 40) {
            if (currentHour == 0) {
                // 자정 이전 → 전날 23:30
                baseDate = now.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                baseTime = "2330";
            } else {
                baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                baseTime = now.minusHours(1).format(DateTimeFormatter.ofPattern("HH")) + "30";
            }
        } else {
            baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            baseTime = now.format(DateTimeFormatter.ofPattern("HH")) + "30";
        }
        

        return new String[]{baseDate, baseTime};
    }
        
    /*
     * API에서 받은 Response를 DTO로 변환
     */
    private NowcastDTO nowCastConvertToDTO(NowcastApiResponse.Item item, String sidoNm, String sigunguNm) {
    	String baseDate = item.getBaseDate();
        String baseTime = item.getBaseTime();
        String category = item.getCategory();
        int nx = item.getNx();
        int ny = item.getNy();
        double obsrValue = 0.0;
        try {
            obsrValue = Double.parseDouble(item.getObsrValue());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        NowcastDTO nowcastDTO = new NowcastDTO(null, baseDate, baseTime, sidoNm, sigunguNm, nx, ny, category, obsrValue);
        return nowcastDTO;
    }
    
    /*
     * API에서 받은 Response를 DTO로 변환
     */
    private PatientsDTO patientsConvertToDTO(PatientsApiResponse.Row row) {
    	String region = row.getRegi();
        int year = Integer.parseInt(row.getBas_yy());
        int total = parseIntSafe(row.getTot());
        int outdoor = parseIntSafe(row.getOtdoor_subtot());
        int indoor = parseIntSafe(row.getIndoor_subtot());

        PatientsDTO patientsDTO = new PatientsDTO(null, region, year, total, outdoor, indoor);
        return patientsDTO;
    }

    /*
     * 정수로 변환할 수 없는 경우(예: null, 빈 문자열, 숫자가 아닌 문자 포함, int 범위 초과 등) 예외를 잡아서 0을 반환
     */
    private int parseIntSafe(String val) {
        try {
            return Integer.parseInt(val);
        } catch (Exception e) {
            return 0;
        }
    }
    
    /*
     * 초단기실황 API 입력
     */
    @Override
    public void insertNowcast() throws SQLException{
    	try {
    		List<Map<String, String>> locations = readLocation();
    		
    		String[] dateTime = getBaseDateTime();
    		String baseDate = dateTime[0];
    		String baseTime = dateTime[1];

    	    for (Map<String, String> location : locations) {
    	    	System.out.println("Location from readLocation: " + location.get("sido") + ", " + location.get("sigungu") + ", " + location.get("nx") + ", " + location.get("ny"));
    	        String nx = location.get("nx");
    	        String ny = location.get("ny");
    	        String sido = location.get("sido");      // <-- sido
    	        String sigungu = location.get("sigungu");
    	        
    	        // 1. API 호출
    	        URI uri = new URI(NOWCAST_URL +
        				"?serviceKey=" + SERVICE_KEY +
        				"&numOfRows=500" +
        				"&pageNo=1" +
        				"&dataType=json" +
        				"&base_date=" + baseDate +
        				"&base_time=" + baseTime +
        				"&nx=" + nx +
        				"&ny=" + ny);
    	        
    	        NowcastApiResponse nowcastResponse = restTemplate.getForObject(uri, NowcastApiResponse.class);

        	    for (NowcastApiResponse.Item item : nowcastResponse.getResponse().getBody().getItems().getItem()) {
        	        NowcastDTO dto = nowCastConvertToDTO(item, sido, sigungu);
        	        temperatureMapper.insertNowcast(dto);
        	    }
    	    }			
    	}
    	catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /*
     * 온열질환자 API 데이터 입력
     */
    @Override
	public void insertPatient() throws SQLException {
		try {
	        // 1. API 호출
	        URI uri = new URI(PATIENTES_URL +
	                "?serviceKey=" + SERVICE_KEY +
	                "&type=json" +
	                "&bas_yy=" +
	                "&pageNo=1" +
	                "&numOfRows=500");

	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Accept", "text/html");

	        HttpEntity<String> entity = new HttpEntity<>(headers);
	        RestTemplate restTemplate = new RestTemplate();

	        ResponseEntity<String> response = restTemplate.exchange(
	                uri,
	                HttpMethod.POST,
	                entity,
	                String.class
	        );

	        // 2. JSON → 객체 변환
	        ObjectMapper objectMapper = new ObjectMapper();
	        PatientsApiResponse apiResponse =
	                objectMapper.readValue(response.getBody(), PatientsApiResponse.class);

	        // 3. HeatWaveCasualtiesRegion → Row 변환 → DTO 변환 → DB 저장
	        List<Map<String, Object>> regionList = apiResponse.getHeatWaveCasualtiesRegion();

	        for (Map<String, Object> regionMap : regionList) {
	            @SuppressWarnings("unchecked")
	            List<Map<String, Object>> rowList = (List<Map<String, Object>>) regionMap.get("row");

	            if (rowList == null) {
	                System.out.println("rowList is null for regionMap: " + regionMap);
	                continue;  // null일 경우 다음 regionMap으로 넘어감
	            }
	            
	            for (Map<String, Object> rowMap : rowList) {
	                PatientsApiResponse.Row row = objectMapper.convertValue(rowMap, PatientsApiResponse.Row.class);

	                // Row → DTO
	                PatientsDTO dto1 = patientsConvertToDTO(row);

	                // 매퍼로 DB 저장
	                temperatureMapper.insertPatient(dto1);
	            }
	        }

	        System.out.println("데이터 저장 완료!");

	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("데이터 저장 중 오류 발생: " + e.getMessage());
	    }
	}

	@Override
	public List<PatientsDTO> getAllPatients() throws SQLException {
		return temperatureMapper.selectAllPatients();
	}

}