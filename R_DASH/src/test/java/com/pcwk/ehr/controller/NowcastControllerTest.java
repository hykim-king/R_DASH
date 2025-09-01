package com.pcwk.ehr.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcwk.ehr.domain.NowcastDTO;
import com.pcwk.ehr.service.NowcastService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
        "file:src/main/webapp/WEB-INF/spring/root-context.xml",
        "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"
})
class NowcastControllerTest {

    @Autowired
    WebApplicationContext wac;

    @Autowired
    NowcastService nowcastService; // 샘플 지역 추출용 (Mockito 사용 안 함)

    MockMvc mockMvc;
    ObjectMapper om = new ObjectMapper();

    private static final Set<String> REQUIRED_CATS =
            new LinkedHashSet<>(Arrays.asList("T1H","RN1","REH","WSD"));

    @BeforeEach
    void setup() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
    @DisplayName("/nowcast/latest4: 지역당 4개 카테고리 보장")
    void latest4_endpoint_ok() throws Exception {
        String body = mockMvc.perform(get("/nowcast/latest4")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        List<NowcastDTO> rows = om.readValue(body, new TypeReference<List<NowcastDTO>>() {});
        assertNotNull(rows);
        assertFalse(rows.isEmpty(), "응답이 비었음");

        Map<String, Set<String>> catsByRegion = rows.stream().collect(
                Collectors.groupingBy(
                        r -> r.getSidoNm() + "||" + r.getSignguNm(),
                        Collectors.mapping(NowcastDTO::getCategory, Collectors.toCollection(LinkedHashSet::new))
                )
        );

        catsByRegion.forEach((region, cats) -> {
            assertEquals(4, cats.size(), "카테고리 수 != 4 : " + region);
            assertEquals(REQUIRED_CATS, cats, "카테고리 불일치 : " + region + " cats=" + cats);
        });
    }

    @Test
    @DisplayName("/nowcast/latest4-region: 특정 지역 4행 반환")
    void latest4_region_endpoint_ok() throws Exception {
        // 서비스로부터 실제 존재하는 지역 하나를 가져와 테스트(환경 의존 최소화)
        List<NowcastDTO> all = nowcastService.getLatest4All();
        assertFalse(all.isEmpty(), "데이터 없음");
        NowcastDTO sample = all.get(0);

        String body = mockMvc.perform(
                        get("/nowcast/latest4-region")
                                .param("sidoNm", sample.getSidoNm())
                                .param("signguNm", sample.getSignguNm())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        List<NowcastDTO> rows = om.readValue(body, new TypeReference<List<NowcastDTO>>() {});
        assertNotNull(rows);
        assertEquals(4, rows.size(), "4행이 아님");

        Set<String> cats = rows.stream()
                .map(NowcastDTO::getCategory)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        assertEquals(REQUIRED_CATS, cats, "카테고리 불일치: " + cats);
    }

    @Test
    @DisplayName("/nowcast/latest.do?category=RN1: 단일 카테고리 전국 스냅샷")
    void latest_snapshot_endpoint_ok() throws Exception {
        String body = mockMvc.perform(
                        get("/nowcast/latest.do")
                                .param("category", "RN1")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        // Map 리스트로 파싱 (sidoNm, signguNm, obsrValue)
        List<Map<String,Object>> rows = om.readValue(body, new TypeReference<List<Map<String,Object>>>() {});
        assertNotNull(rows);
        assertFalse(rows.isEmpty(), "응답이 비었음");

        // 필수 키 확인
        Map<String,Object> first = rows.get(0);
        assertTrue(first.containsKey("sidoNm"));
        assertTrue(first.containsKey("signguNm"));
        assertTrue(first.containsKey("obsrValue"));
    }
}
