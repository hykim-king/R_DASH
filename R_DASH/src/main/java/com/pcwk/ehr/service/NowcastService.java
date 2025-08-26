// /src/main/java/com/pcwk/ehr/service/NowcastService.java
package com.pcwk.ehr.service;

import java.util.List;
import java.util.Map;
import com.pcwk.ehr.domain.NowcastDTO;

public interface NowcastService {
	List<Map<String, Object>> getNationLatest(String category);

	List<NowcastDTO> getLatest4All();

	List<NowcastDTO> getLatest4ByRegion(String sidoNm, String signguNm);
}
