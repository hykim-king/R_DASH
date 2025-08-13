package com.pcwk.ehr.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.reactive.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
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
	
	Logger log = LogManager.getLogger(getClass());
	
	@Autowired
    private RestTemplate restTemplate;
	
	@Autowired
	private ServletContext servletContext;
	
	@Autowired
	private TemperatureMapper temperatureMapper;
	
	@Autowired 
	private TransactionTemplate txTemplate;
	
    private static final String PATIENTES_URL = "http://apis.data.go.kr/1741000/HeatWaveCasualtiesRegion/getHeatWaveCasualtiesRegionList";
    private static final String NOWCAST_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst";
    private static final String SERVICE_KEY = "VJxg5p3Iyzp7FA0pzgtVA7AYRfaM2YSuLU4h8TMQAvQIJMGkIN7qEpL%2FQoDBEqo1MnsWnxGR%2BlN%2F9SsKlSmbZg%3D%3D";
    private static Map<String, List<Map<String, String>>> locationCache = new ConcurrentHashMap<>();
    private final int FETCH_BUFFER_SIZE = 2500;
    private final int BATCH_SIZE = 500; // 환경에 따라 200~1000 권장
    private final int MAX_RETRY = 3;    // 청크 실패 시 재시도 횟수
    private List<NowcastDTO> buffer = new ArrayList<>();
    
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
    
    private URI buildUri(String baseDate, String baseTime, String nx, String ny) {
    	URI uri = UriComponentsBuilder.fromHttpUrl(NOWCAST_URL)
                .queryParam("serviceKey", SERVICE_KEY)
                .queryParam("numOfRows", 500)
                .queryParam("pageNo", 1)
                .queryParam("dataType", "json")
                .queryParam("base_date", baseDate)
                .queryParam("base_time", baseTime)
                .queryParam("nx", nx)
                .queryParam("ny", ny)
                .build(true)
                .toUri();

        log.info(uri.toString());

        return uri;
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

                   // 현재 읽은 엑셀 행이 (시도, 시군구, nx, ny)까지 완전히 동일한 중복이라면 스킵
                   String fullRowKey = sido + "_" + sigungu + "_" + nx + "_" + ny;
                   if (xyKeys.contains(fullRowKey)) {
                       continue;
                   }
                   xyKeys.add(fullRowKey);
                   
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
    
    @Override
    public void fetchAndMergeNowcast() {
        List<Map<String, String>> locations;
        try {
            locations = readLocation();
        } catch (IOException ioe) {
            log.error("readLocation 오류", ioe);
            return;
        }

        String[] dateTime = getBaseDateTime();
        String baseDate = dateTime[0];
        String baseTime = dateTime[1];

        List<NowcastDTO> buffer = new ArrayList<>();

        for (Map<String, String> loc : locations) {
            String nx = loc.get("nx");
            String ny = loc.get("ny");
            String sido = loc.get("sido");
            String sigungu = loc.get("sigungu");

            URI uri;
            try {
                uri = buildUri(baseDate, baseTime, nx, ny);
            } catch (Exception e) {
                log.error("URI 생성 실패: nx={}, ny={}, err={}", nx, ny, e.getMessage());
                continue;
            }

            NowcastApiResponse resp = null;
            try {
                resp = restTemplate.getForObject(uri, NowcastApiResponse.class);
            } catch (Exception e) {
                log.error("API 호출 실패: nx={}, ny={}, err={}", nx, ny, e.getMessage());
                
                continue; // 필요시 재시도 로직 삽입 가능
            }

            if (resp == null || resp.getResponse() == null || resp.getResponse().getBody() == null) {
                log.warn("빈 응답: nx={}, ny={}", nx, ny);
                continue;
            }

            List<NowcastApiResponse.Item> items = resp.getResponse().getBody().getItems().getItem();
            if (items == null || items.isEmpty()) continue;

            for (NowcastApiResponse.Item item : items) {
                NowcastDTO dto = nowCastConvertToDTO(item, sido, sigungu);
                buffer.add(dto);

                if (buffer.size() >= FETCH_BUFFER_SIZE) {
                    // 안전하게 복사해서 전달
                    List<NowcastDTO> chunkToSend = new ArrayList<>(buffer);
                    try {
                        // 같은 클래스 내 호출이어도 mergeNowcast 내부는 txTemplate 기반으로 구현되어 있어 안전
                        mergeNowcast(chunkToSend);
                    } catch (Exception e) {
                        log.error("청크 merge 실패(즉시): size={}, err={}", chunkToSend.size(), e.getMessage(), e);
                        // 실패 청크 처리: 로깅 또는 실패 테이블 저장 권장 (여기서는 로그만 남김)
                    }
                    buffer.clear();
                }
            }
        }

        // 남은 데이터 처리
        if (!buffer.isEmpty()) {
            try {
                mergeNowcast(new ArrayList<>(buffer));
            } catch (Exception e) {
                log.error("마지막 청크 merge 실패: size={}, err={}", buffer.size(), e.getMessage(), e);
            } finally {
                buffer.clear();
            }
        }
    }
    
    @Override
    public void mergeNowcast(List<NowcastDTO> list) {
        if (list == null || list.isEmpty()) {
            log.debug("mergeNowcast: 처리할 데이터 없음");
            return;
        }

        int total = list.size();
        log.info("mergeNowcast 시작: total={}, BATCH_SIZE={}", total, BATCH_SIZE);

        for (int start = 0; start < total; start += BATCH_SIZE) {
            final int s = start;
            final int e = Math.min(start + BATCH_SIZE, total);
            final List<NowcastDTO> chunk = list.subList(s, e);

            boolean success = false;
            int attempt = 0;
            while (!success && attempt < MAX_RETRY) {
                attempt++;
                try {
                    final int currentAttempt = attempt;
                    txTemplate.execute(status -> {
                        int affected = temperatureMapper.upsertNowcast(chunk);
                        log.info("upsert 실행: start={}, end={}, affected={}, attempt={}", s, e, affected, currentAttempt);
                        return null;
                    });
                    success = true;
                } catch (Exception ex) {
                    log.error("upsert 실패: start={}, end={}, attempt={}, err={}", s, e, attempt, ex.getMessage(), ex);
                    if (attempt >= MAX_RETRY) {
                        log.error("최대 재시도 후 실패한 청크: start={}, end={}", s, e);
                        // TODO: 실패 청크 저장/알림 처리
                    } else {
                        try { Thread.sleep(1000L * attempt); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                    }
                }
            }
        }
        log.info("mergeNowcast 완료: total={}", total);
    }
    
    /*
     * 초단기실황 API 입력
     */
    @Override
    public void insertNowcast() throws SQLException{
    	try {
            // 실제 수집 + 병합 로직은 fetchAndMergeNowcast()에 있음
            fetchAndMergeNowcast();
        } catch (Exception e) {
            log.error("insertNowcast 실패", e);
            // 인터페이스에 throws SQLException이 선언되어 있으므로 SQLException으로 포장하여 던짐
            throw new SQLException("insertNowcast 실패: " + e.getMessage(), e);
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