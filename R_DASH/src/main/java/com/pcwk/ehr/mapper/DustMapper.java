package com.pcwk.ehr.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.pcwk.ehr.cmn.WorkDiv;
import com.pcwk.ehr.domain.DustDTO;

@Mapper
public interface DustMapper extends WorkDiv<DustDTO> {

    List<DustDTO> selectStationAvgAt10(
        @Param("day") String day,
        @Param("minLat") Double minLat,
        @Param("minLon") Double minLon,
        @Param("maxLat") Double maxLat,
        @Param("maxLon") Double maxLon
    );

    // XML이 day만 받도록 짜여있다면 warn/caution 파라미터는 일단 제거하거나 XML과 맞추세요.
    List<DustDTO> selectLegendBucketsAt10(
        @Param("day") String day
    );

    List<DustDTO> selectLatestByOrgAt10(
        @Param("day") String day,
        @Param("org") String org,
        @Param("minLat") Double minLat,
        @Param("minLon") Double minLon,
        @Param("maxLat") Double maxLat,
        @Param("maxLon") Double maxLon
    );
}
