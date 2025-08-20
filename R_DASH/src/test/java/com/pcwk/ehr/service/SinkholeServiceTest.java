package com.pcwk.ehr.service;

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
        "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml"
})
class SinkholeServiceTest {

    @Autowired
    private SinkholeService sinkholeService;

    @Test
    void contextLoads() {
        assertNotNull(sinkholeService);
    }

    @Test
    void selectAll_shouldReturnList() {
        List<SinkholeDTO> rows = sinkholeService.selectAll();
        assertNotNull(rows);
        assertFalse(rows.isEmpty()); // 최소 1건 이상 있어야 함
    }

    @Test
    void findById_shouldReturnSingle() {
        SinkholeDTO dto = sinkholeService.findById(1); // 실제 존재하는 PK 값으로
        assertNotNull(dto);
        assertEquals(1, dto.getSinkholeNo());
        assertEquals("고양시", dto.getSignguNm()); // 실제 DB 값에 맞게 수정
    }
}
