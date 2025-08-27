package com.pcwk.ehr.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.Collectors;

import com.pcwk.ehr.domain.NowcastDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
        "file:src/main/webapp/WEB-INF/spring/root-context.xml",
        "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"
})
class NowcastServiceTest {

    @Autowired
    NowcastService nowcastService;

    private static final Set<String> REQUIRED_CATS =
            new LinkedHashSet<>(Arrays.asList("T1H","RN1","REH","WSD"));

    @Test
    @DisplayName("전국 전체: 지역마다 항상 4개 카테고리(T1H,RN1,REH,WSD)를 가진다")
    void latest4All_structure_ok() {
        List<NowcastDTO> rows = nowcastService.getLatest4All();
        assertNotNull(rows, "rows null");
        assertFalse(rows.isEmpty(), "rows empty");

        // (sido, sgg) -> 그 지역의 카테고리 집합
        Map<String, Set<String>> catsByRegion = rows.stream().collect(
                Collectors.groupingBy(
                        r -> r.getSidoNm() + "||" + r.getSignguNm(),
                        Collectors.mapping(NowcastDTO::getCategory, Collectors.toCollection(LinkedHashSet::new))
                )
        );

        // 모든 지역이 정확히 4카테고리여야 함
        catsByRegion.forEach((region, cats) -> {
            assertEquals(4, cats.size(), "카테고리 개수 != 4 : " + region + " cats=" + cats);
            assertEquals(REQUIRED_CATS, cats, "카테고리 불일치 : " + region + " cats=" + cats);
        });

        // 값/기본 필드 sanity check (null 방지용 NVL 적용되어야 함)
        rows.forEach(r -> {
            assertNotNull(r.getSidoNm());
            assertNotNull(r.getSignguNm());
            assertNotNull(r.getCategory());
            // primitive라 0도 유효 — 여기서는 음수만 방어
            assertTrue(r.getNx() >= 0 && r.getNy() >= 0, "nx/ny 음수?");
        });
    }

    @Test
    @DisplayName("특정 지역: latest4-region이 실제로 4행을 반환한다 (동일 지역을 latest4All에서 하나 뽑아 사용)")
    void latest4ByRegion_returns_4() {
        List<NowcastDTO> all = nowcastService.getLatest4All();
        assertFalse(all.isEmpty(), "데이터 없음");

        // 첫 지역을 샘플로 사용
        NowcastDTO sample = all.get(0);
        String sido = sample.getSidoNm();
        String sgg  = sample.getSignguNm();

        List<NowcastDTO> four = nowcastService.getLatest4ByRegion(sido, sgg);
        assertNotNull(four);
        assertEquals(4, four.size(), "반환 행 수가 4가 아님");

        Set<String> cats = four.stream().map(NowcastDTO::getCategory).collect(Collectors.toCollection(LinkedHashSet::new));
        assertEquals(REQUIRED_CATS, cats, "카테고리 셋 불일치: " + cats);
    }
}
