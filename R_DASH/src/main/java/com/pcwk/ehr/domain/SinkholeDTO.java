package com.pcwk.ehr.domain;

import com.pcwk.ehr.cmn.DTO;

public class SinkholeDTO extends DTO{
	private Integer sinkholeNo    ;	//고유 번호
	private String  sidoNm        ;	//시도명
	private String  signguNm      ;	//시군구명
	private double  lat           ;	//위도
	private double  lon           ;	//경도
	private String  occurDt       ;	//발생일자
	private int  	dprsCnt       ;	//피해사망자수
	private int  	injpsnCnt     ;	//피해차량대수
	private int  	vehcleCnt     ;	//복구상태명
	private String  stateNm       ;	//복구완료일자
	private String  compltDt      ;	//지하침하정보(싱크롤)
	
    // ✅ 집계 전용 필드 (버블맵용)
    private Integer holeCount;  // 발생 건수
	
	public SinkholeDTO() {
	}

	public SinkholeDTO(Integer sinkholeNo, String sidoNm, String signguNm, double lat, double lon, String occurDt,
			int dprsCnt, int injpsnCnt, int vehcleCnt, String stateNm, String compltDt) {
		super();
		this.sinkholeNo = sinkholeNo;
		this.sidoNm = sidoNm;
		this.signguNm = signguNm;
		this.lat = lat;
		this.lon = lon;
		this.occurDt = occurDt;
		this.dprsCnt = dprsCnt;
		this.injpsnCnt = injpsnCnt;
		this.vehcleCnt = vehcleCnt;
		this.stateNm = stateNm;
		this.compltDt = compltDt;
	}

	/**
	 * @return the sinkholeNo
	 */
	public Integer getSinkholeNo() {
		return sinkholeNo;
	}

	/**
	 * @param sinkholeNo the sinkholeNo to set
	 */
	public void setSinkholeNo(Integer sinkholeNo) {
		this.sinkholeNo = sinkholeNo;
	}

	/**
	 * @return the sidoNm
	 */
	public String getSidoNm() {
		return sidoNm;
	}

	/**
	 * @param sidoNm the sidoNm to set
	 */
	public void setSidoNm(String sidoNm) {
		this.sidoNm = sidoNm;
	}

	/**
	 * @return the signguNm
	 */
	public String getSignguNm() {
		return signguNm;
	}

	/**
	 * @param signguNm the signguNm to set
	 */
	public void setSignguNm(String signguNm) {
		this.signguNm = signguNm;
	}

	/**
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * @param lat the lat to set
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}

	/**
	 * @return the lon
	 */
	public double getLon() {
		return lon;
	}

	/**
	 * @param lon the lon to set
	 */
	public void setLon(double lon) {
		this.lon = lon;
	}

	/**
	 * @return the occurDt
	 */
	public String getOccurDt() {
		return occurDt;
	}

	/**
	 * @param occurDt the occurDt to set
	 */
	public void setOccurDt(String occurDt) {
		this.occurDt = occurDt;
	}

	/**
	 * @return the dprsCnt
	 */
	public int getDprsCnt() {
		return dprsCnt;
	}

	/**
	 * @param dprsCnt the dprsCnt to set
	 */
	public void setDprsCnt(int dprsCnt) {
		this.dprsCnt = dprsCnt;
	}

	/**
	 * @return the injpsnCnt
	 */
	public int getInjpsnCnt() {
		return injpsnCnt;
	}

	/**
	 * @param injpsnCnt the injpsnCnt to set
	 */
	public void setInjpsnCnt(int injpsnCnt) {
		this.injpsnCnt = injpsnCnt;
	}

	/**
	 * @return the vehcleCnt
	 */
	public int getVehcleCnt() {
		return vehcleCnt;
	}

	/**
	 * @param vehcleCnt the vehcleCnt to set
	 */
	public void setVehcleCnt(int vehcleCnt) {
		this.vehcleCnt = vehcleCnt;
	}

	/**
	 * @return the stateNm
	 */
	public String getStateNm() {
		return stateNm;
	}

	/**
	 * @param stateNm the stateNm to set
	 */
	public void setStateNm(String stateNm) {
		this.stateNm = stateNm;
	}

	/**
	 * @return the compltDt
	 */
	public String getCompltDt() {
		return compltDt;
	}

	/**
	 * @param compltDt the compltDt to set
	 */
	public void setCompltDt(String compltDt) {
		this.compltDt = compltDt;
	}

	
	  // getter / setter
    public Integer getHoleCount() { return holeCount; }
    public void setHoleCount(Integer holeCount) { this.holeCount = holeCount; }
    
    
    @Override
    public String toString() {
        return "SinkholeDTO{" +
                "sinkholeNo=" + sinkholeNo +
                ", sidoNm='" + sidoNm + '\'' +
                ", signguNm='" + signguNm + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", occurDt='" + occurDt + '\'' +
                ", dprsCnt=" + dprsCnt +
                ", injpsnCnt=" + injpsnCnt +
                ", vehcleCnt=" + vehcleCnt +
                ", stateNm='" + stateNm + '\'' +
                ", compltDt='" + compltDt + '\'' +
                ", holeCount=" + holeCount +
                '}';
    }

}
