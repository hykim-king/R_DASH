package com.pcwk.ehr.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;      // BDD 스타일
// import static org.mockito.Mockito.when;      // Mockito 스타일을 쓰고 싶으면 이걸로 대체 가능

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pcwk.ehr.domain.ShelterDTO;
import com.pcwk.ehr.mapper.ShelterMapper;

/**
 * Service 단위 테스트
 * - Mapper는 Mock(가짜)으로 두고, Service 로직만 검증
 */
@ExtendWith(MockitoExtension.class) // JUnit5 + Mockito 통합
class ShelterServiceTest {

    @Mock
    ShelterMapper mapper;           // DB 호출 대신 동작을 우리가 지정

    @InjectMocks
    ShelterServiceImpl service;     // 테스트 대상(Service) - mapper Mock이 주입됨

    /** 단건 조회 */
    @Test
    void selectOne_ok() throws Exception {
        ShelterDTO dummy = new ShelterDTO();
        dummy.setShelterNo(1);                 // Integer 타입 맞춤
        dummy.setReareNm("테스트대피소");

        given(mapper.selectOne(1)).willReturn(dummy);

        ShelterDTO result = service.selectOne(1);

        assertNotNull(result);
        assertEquals(1, result.getShelterNo());
        assertEquals("테스트대피소", result.getReareNm());
    }

    /** BBox 조회 */
    @Test
    void selectByBBox_ok() throws Exception {
        ShelterDTO s1 = new ShelterDTO(); s1.setShelterNo(1);
        ShelterDTO s2 = new ShelterDTO(); s2.setShelterNo(2);

        given(mapper.selectByBBox(37.4, 37.6, 126.8, 127.1, null, 10))
            .willReturn(Arrays.asList(s1, s2));

        List<ShelterDTO> rows = service.selectByBBox(37.4, 37.6, 126.8, 127.1, null, 10);

        assertEquals(2, rows.size());
        assertEquals(1, rows.get(0).getShelterNo());
    }

    /** 자동완성 - 주소 */
    @Test
    void suggestAdress_ok() throws Exception {
        given(mapper.suggestAdress("서울"))
            .willReturn(Collections.singletonList("서울시 양천구 화곡로"));

        List<String> list = service.suggestAdress("서울");

        assertFalse(list.isEmpty());
        assertTrue(list.get(0).contains("서울"));
    }
}
