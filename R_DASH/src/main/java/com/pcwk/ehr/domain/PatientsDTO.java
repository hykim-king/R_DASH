package com.pcwk.ehr.domain;

public class PatientsDTO {

	private Integer patientsNo;
	private String sidoNm;
	private int year;
	private int patientsTot;
	private int outSubtot;
	private int inSubtot;
	

	/**
	 * 
	 */
	public PatientsDTO() {
	}
	/**
	 * @return the patientsNo
	 */
	public int getPatientsNo() {
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
	public int getYear() {
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
	public int getPatientsTot() {
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
	public int getOutSubtot() {
		return outSubtot;
	}
	/**
	 * @param outSubtot the outSubtot to set
	 */
	public void setOutSubtot(int outSubtot) {
		this.outSubtot = outSubtot;
	}
	/**
	 * @return the inSubtot
	 */
	public int getInSubtot() {
		return inSubtot;
	}
	/**
	 * @param inSubtot the inSubtot to set
	 */
	public void setInSubtot(int inSubtot) {
		this.inSubtot = inSubtot;
	}
	/**
	 * @param patientsNo
	 * @param sidoNm
	 * @param year
	 * @param patientsTot
	 * @param outSubtot
	 * @param inSubtot
	 */
	public PatientsDTO(Integer patientsNo, String sidoNm, int year, int patientsTot, int outSubtot, int inSubtot) {
		super();
		this.patientsNo = patientsNo;
		this.sidoNm = sidoNm;
		this.year = year;
		this.patientsTot = patientsTot;
		this.outSubtot = outSubtot;
		this.inSubtot = inSubtot;
	}
	@Override
	public String toString() {
		return "PatientsDTO [patientsNo=" + patientsNo + ", sidoNm=" + sidoNm + ", year=" + year + ", patientsTot="
				+ patientsTot + ", outSubtot=" + outSubtot + ", inSubtot=" + inSubtot + "]";
	}
	
	
	
	
	
	
}
