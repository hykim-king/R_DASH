package com.pcwk.ehr.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {
  "file:src/main/webapp/WEB-INF/spring/root-context.xml",
  "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml"
})
class LandslideControllerTest {

    final Logger log = LogManager.getLogger(getClass());

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;
    ObjectMapper om;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        this.om = new ObjectMapper();
    }

    @Test
    void heatmap_ok() throws Exception {
        String url = "/landslide/heatmap";
        String qs = "?minLat=33&maxLat=39.5&minLon=124&maxLon=132";

        String json = mockMvc.perform(
                get(url + qs)
                 .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        List<Map<String,Object>> rows =
            om.readValue(json, new TypeReference<List<Map<String,Object>>>(){});
        assertNotNull(rows);
        log.info("heatmap rows={}", rows.size());
        // 선택 검증: 첫 행에 lat/lon/cnt 키 존재 여부
        if (!rows.isEmpty()) {
            assertTrue(rows.get(0).containsKey("lat"));
            assertTrue(rows.get(0).containsKey("lon"));
            assertTrue(rows.get(0).containsKey("cnt"));
        }
    }

    @Test
    void points_ok() throws Exception {
        String url = "/landslide/points";
        String qs = "?minLat=33&maxLat=39.5&minLon=124&maxLon=132";

        String json = mockMvc.perform(
                get(url + qs)
                 .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        List<Map<String,Object>> rows =
            om.readValue(json, new TypeReference<List<Map<String,Object>>>(){});
        assertNotNull(rows);
        log.info("points rows={}", rows.size());
    }
}
