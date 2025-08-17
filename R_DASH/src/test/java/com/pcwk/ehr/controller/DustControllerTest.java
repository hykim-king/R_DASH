package com.pcwk.ehr.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * DustController 통합 테스트 (MockMvc + WebApplicationContext)
 * - /api/dust/latest 응답 형태/상태코드 검증
 */
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {
    "file:src/main/webapp/WEB-INF/spring/root-context.xml",
    "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml"
})
public class DustControllerTest {

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        // 실제 스프링 웹 컨텍스트 기반 MockMvc
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void latest_bbox_ok() throws Exception {
        // given: BBox + airType 파라미터
        String airType = "도시대기";

        // when & then
        mockMvc.perform(get("/api/dust/latest")
                .param("airType", airType)
                .param("minLat", "33.0")
                .param("maxLat", "38.7")
                .param("minLon", "124.5")
                .param("maxLon", "131.0")
                .param("limit", "100")
            )
            .andDo(print())
            .andExpect(status().isOk())
            // application/json 또는 호환 타입이어도 OK
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            // 결과가 JSON 배열인지 확인 (비어 있어도 배열이어야 함)
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void latest_all_ok() throws Exception {
        // given: BBox 없이 airType만 -> 전국 조회
        String airType = "교외대기";

        mockMvc.perform(get("/api/dust/latest")
                .param("airType", airType)
                .param("limit", "50")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());
    }
}
