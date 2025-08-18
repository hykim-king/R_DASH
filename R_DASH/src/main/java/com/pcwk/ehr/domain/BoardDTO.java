package com.pcwk.ehr.domain;

import com.pcwk.ehr.cmn.DTO; 

public class BoardDTO extends DTO{
	private int boardNo      ;
	private String title      ;
	private String contents   ;
	private int viewCnt     ;
	private String regDt     ;
	private String regId     ;
	private String modDt     ;
	private String modId     ;
	
	
	public BoardDTO() {}

	

	public BoardDTO(int boardNo, String title, String contents, int viewCnt, String regDt, String regId, String modDt,
			String modId) {
		super();
		this.boardNo = boardNo;
		this.title = title;
		this.contents = contents;
		this.viewCnt = viewCnt;
		this.regDt = regDt;
		this.regId = regId;
		this.modDt = modDt;
		this.modId = modId;
	}



	public int getBoardNo() {
		return boardNo;
	}

	public void setBoardNo(int boardNo) {
		this.boardNo = boardNo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}


	public int getViewCnt() {
		return viewCnt;
	}

	public void setViewCnt(int viewCnt) {
		this.viewCnt = viewCnt;
	}

	public String getRegDt() {
		return regDt;
	}

	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

	public String getModDt() {
		return modDt;
	}

	public void setModDt(String modDt) {
		this.modDt = modDt;
	}

	public String getModId() {
		return modId;
	}

	public void setModId(String modId) {
		this.modId = modId;
	}



	@Override
	public String toString() {
		return "BoardDTO [boardNo=" + boardNo + ", title=" + title + ", contents=" + contents + ", viewCnt=" + viewCnt
				+ ", regDt=" + regDt + ", regId=" + regId + ", modDt=" + modDt + ", modId=" + modId + ", toString()="
				+ super.toString() + "]";
	}


	
	
}
