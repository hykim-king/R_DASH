package com.pcwk.ehr.domain;

public class PatientsDTO {

	private Integer patientsNo;
	private String sidoNm;
	private Integer year;
	private Integer patientsTot;
	private Integer outSubTot;
	private Integer inSubTot;
	
	private String groupKey;
	
	/**
	 * 
	 */
	public PatientsDTO() {
	}
	/**
	 * @return the patientsNo
	 */
	public Integer getPatientsNo() {
		return patientsNo;
	}
	/**
	 * @param patientsNo the patientsNo to set
	 */
	public void setPatientsNo(int patientsNo) {
		this.patientsNo = patientsNo;
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
	 * @return the year
	 */
	public Integer getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}
	/**
	 * @return the patientsTot
	 */
	public Integer getPatientsTot() {
		return patientsTot;
	}
	/**
	 * @param patientsTot the patientsTot to set
	 */
	public void setPatientsTot(int patientsTot) {
		this.patientsTot = patientsTot;
	}
	/**
	 * @return the outSubtot
	 */
	public Integer getOutSubTot() {
		return outSubTot;
	}
	/**
	 * @param outSubtot the outSubtot to set
	 */
	public void setOutSubTot(Integer outSubtot) {
		this.outSubTot = outSubtot;
	}
	/**
	 * @return the inSubtot
	 */
	public Integer getInSubTot() {
		return inSubTot;
	}
	/**
	 * @param inSubtot the inSubtot to set
	 */
	public void setInSubTot(Integer inSubtot) {
		this.inSubTot = inSubtot;
	}
	
	/**
	 * @return the groupKey
	 */
	public String getGroupKey() {
		return groupKey;
	}
	/**
	 * @param groupKey the groupKey to set
	 */
	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}
	/**
	 * @param patientsNo
	 * @param sidoNm
	 * @param year
	 * @param patientsTot
	 * @param outSubtot
	 * @param inSubtot
	 */
	public PatientsDTO(Integer patientsNo, String sidoNm, Integer year, Integer patientsTot, Integer outSubTot, Integer inSubTot) {
		super();
		this.patientsNo = patientsNo;
		this.sidoNm = sidoNm;
		this.year = year;
		this.patientsTot = patientsTot;
		this.outSubTot = outSubTot;
		this.inSubTot = inSubTot;
	}
	@Override
	public String toString() {
		return "PatientsDTO [patientsNo=" + patientsNo + ", sidoNm=" + sidoNm + ", year=" + year + ", patientsTot="
				+ patientsTot + ", outSubTot=" + outSubTot + ", inSubTot=" + inSubTot + "]";
	}
	
	
	
	
	
	
}
