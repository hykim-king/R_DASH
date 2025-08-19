package com.pcwk.ehr.domain;


public class TopicWordsDTO {
	private int wordNo;
	private String word;
	private int freq;
	private int prevFreg;
	private String regDt;
	
	
	public TopicWordsDTO() {
		super();
	}


	public TopicWordsDTO(int wordNo, String word, int freq, int prevFreg, String regDt) {
		super();
		this.wordNo = wordNo;
		this.word = word;
		this.freq = freq;
		this.prevFreg = prevFreg;
		this.regDt = regDt;
	}


	public int getWordNo() {
		return wordNo;
	}


	public void setWordNo(int wordNo) {
		this.wordNo = wordNo;
	}


	public String getWord() {
		return word;
	}


	public void setWord(String word) {
		this.word = word;
	}


	public int getFreq() {
		return freq;
	}


	public void setFreq(int freq) {
		this.freq = freq;
	}


	public int getPrevFreg() {
		return prevFreg;
	}


	public void setPrevFreg(int prevFreg) {
		this.prevFreg = prevFreg;
	}


	public String getRegDt() {
		return regDt;
	}


	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}


	@Override
	public String toString() {
		return "TopicWordsDTO [wordNo=" + wordNo + ", word=" + word + ", freq=" + freq + ", prevFreg=" + prevFreg
				+ ", regDt=" + regDt + ", toString()=" + super.toString() + "]";
	}
	

	
}
