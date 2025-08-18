package com.pcwk.ehr.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {
        "file:src/main/webapp/WEB-INF/spring/root-context.xml",
        "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml"
})
class FirestationControllerTest {

	Logger log = LogManager.getLogger(getClass());

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        assertNotNull(mockMvc);
    }

    @Test
    void bbox_shouldReturn200AndJsonArray() throws Exception {
        mockMvc.perform(
                get("/api/firestations/bbox")
                        .param("minLat", "33.0")
                        .param("maxLat", "38.5")
                        .param("minLon", "124.0")
                        .param("maxLon", "132.0")
                        .param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void search_shouldReturnPageEnvelope() throws Exception {
        mockMvc.perform(
                get("/api/firestations/search")
                        .param("q", "소방")
                        .param("orderBy", "name")
                        .param("limit", "5")
                        .param("offset", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rows").exists())
                .andExpect(jsonPath("$.total").exists());
    }

    @Test
    void one_shouldReturnDetail() throws Exception {
        // 실제 PK를 모르면, 먼저 검색해서 얻어온 id로 테스트하는 통합 시나리오를 별도 작성해도 좋음.
        // 여기서는 일단 1번을 조회(없으면 404 대신 200 + null을 반환하는 구조라면 json 본문만 체크).
        mockMvc.perform(get("/api/firestations/{stationNo}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void autocompleteArea_shouldWork() throws Exception {
        mockMvc.perform(get("/api/firestations/auto/area")
                        .param("prefix", "서")
                        .param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void autocompleteStation_shouldWork() throws Exception {
        mockMvc.perform(get("/api/firestations/auto/station")
                        .param("prefix", "강")
                        .param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}