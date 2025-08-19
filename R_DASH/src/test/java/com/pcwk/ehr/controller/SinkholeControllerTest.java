package com.pcwk.ehr.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.pcwk.ehr.domain.SinkholeDTO;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
    "file:src/main/webapp/WEB-INF/spring/root-context.xml"
})
public class SinkholeControllerTest {

	 @Autowired
	    SinkholeController sinkholeController; // <<== 네가 원하는 방식


	    @Test
	    void all_shouldReturnList() {
	        List<SinkholeDTO> rows = sinkholeController.all();
	        assertNotNull(rows);
	        assertEquals(2, rows.size()); // fixture에 맞춰 검증
	        assertEquals("서울특별시", rows.get(0).getSidoNm());
	    }

	    @Test
	    void one_shouldReturnSingle() {
	        SinkholeDTO dto = sinkholeController.one(1001);
	        assertNotNull(dto);
	        assertEquals(1001, dto.getSinkholeNo());
	        assertEquals("강남구", dto.getSignguNm());
	    }

	    @Test
	    void bbox_shouldFilter() {
	        List<SinkholeDTO> rows = sinkholeController.byBBox(
	                35.0, 36.0, 128.5, 129.5
	        );
	        assertNotNull(rows);
	        assertEquals(1, rows.size());
	        assertEquals("부산광역시", rows.get(0).getSidoNm());
	    }
	}