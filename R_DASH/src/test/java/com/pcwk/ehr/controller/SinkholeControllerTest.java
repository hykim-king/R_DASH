package com.pcwk.ehr.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import com.pcwk.ehr.domain.SinkholeDTO;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {
    "file:src/main/webapp/WEB-INF/spring/root-context.xml",
    "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml" // ★ 중요: 컨트롤러 스캔
})
class SinkholeControllerTest {



    // ★ 테스트 전용 최소 MVC 빈 등록
    @Configuration
    static class TestMvcBeans {
        @Bean
        @Qualifier("exceptionResolver")        // SecurityConfig가 요구
        public HandlerExceptionResolver exceptionResolver() {
            return new DefaultHandlerExceptionResolver();
        }
    }

    @Autowired
    SinkholeController controller;

    // ====== 히트맵 포인트 ======
    @Test @DisplayName("points/all: 리스트 반환")
    void allPoints_returnsList() {
        List<Map<String, Object>> rows = controller.allPoints(null, null);
        assertNotNull(rows);
    }

    @Test @DisplayName("points (BBox): 리스트 반환")
    void pointsByBBox_returnsList() throws Exception {
        List<SinkholeDTO> rows = controller.pointsByBBox(33.0, 38.5, 125.0, 130.0, null, null);
        assertNotNull(rows);
    }

    @Test @DisplayName("points (BBox): 스왑 파라미터도 보정 동작")
    void pointsByBBox_swappedParamsAlsoOk() throws Exception {
        List<SinkholeDTO> normal = controller.pointsByBBox(33.0, 38.5, 125.0, 130.0, null, null);
        List<SinkholeDTO> swapped = controller.pointsByBBox(38.5, 33.0, 130.0, 125.0, null, null);
        assertNotNull(normal); assertNotNull(swapped);
        assertEquals(normal.size(), swapped.size());
    }


}
