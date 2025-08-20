package com.pcwk.ehr.domain;


public class TopicWordsDTO {
	private int wordNo;
	private String word;
	private int freq;
	private int prevFreq;
	private String regDt;
	
	
	public TopicWordsDTO() {
		super();
	}


	public TopicWordsDTO(int wordNo, String word, int freq, int prevFreq, String regDt) {
		super();
		this.wordNo = wordNo;
		this.word = word;
		this.freq = freq;
		this.prevFreq = prevFreq;
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


	public int getPrevFreq() {
		return prevFreq;
	}


	public void setPrevFreq(int prevFreq) {
		this.prevFreq = prevFreq;
	}


	public String getRegDt() {
		return regDt;
	}


	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}


	@Override
	public String toString() {
		return "TopicWordsDTO [wordNo=" + wordNo + ", word=" + word + ", freq=" + freq + ", prevFreq=" + prevFreq
				+ ", regDt=" + regDt + ", toString()=" + super.toString() + "]";
	}
	

	
}
