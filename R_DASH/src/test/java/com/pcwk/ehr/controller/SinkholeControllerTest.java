package com.pcwk.ehr.controller;

import com.pcwk.ehr.domain.SinkholeDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {
    "file:src/main/webapp/WEB-INF/spring/root-context.xml",
    "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml" // ★ 중요: 컨트롤러 스캔
})
class SinkholeControllerTest {

    @Autowired
    SinkholeController controller; // Spring이 주입 (null 아니어야 정상)

    @Test
    void all_shouldReturnList() {
        List<SinkholeDTO> rows = controller.all();
        assertNotNull(rows);
        // 테스트 데이터/픽스처에 맞게 기대값 수정
        // assertEquals(2, rows.size());
        // assertEquals("서울특별시", rows.get(0).getSidoNm());
    }

    @Test
    void one_shouldReturnSingle() {
        List<SinkholeDTO> rows = controller.all();
        assertNotNull(rows);
        assertFalse(rows.isEmpty());

        Integer id = rows.get(0).getSinkholeNo(); // 존재 보장되는 id
        SinkholeDTO dto = controller.one(id);
        assertNotNull(dto);
        assertEquals(id, dto.getSinkholeNo());
    }

    @Test
    void bbox_shouldFilter() {
        List<SinkholeDTO> rows = controller.byBBox(35.0, 36.0, 128.5, 129.5);
        assertNotNull(rows);
        // assertEquals(1, rows.size());
        // assertEquals("부산광역시", rows.get(0).getSidoNm());
    }
}
