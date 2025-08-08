package com.pcwk.ehr.Response;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pcwk.ehr.domain.PatientsDTO;

public class PatientsApiResponse {
	@JsonProperty("HeatWaveCasualtiesRegion")
	private List<Map<String, Object>> heatWaveCasualtiesRegion;

    public List<Map<String, Object>> getHeatWaveCasualtiesRegion() {
        return heatWaveCasualtiesRegion;
    }

    public void setHeatWaveCasualtiesRegion(List<Map<String, Object>> heatWaveCasualtiesRegion) {
    	this.heatWaveCasualtiesRegion = heatWaveCasualtiesRegion;
    }

    public static class HeatWaveCasualtiesRegionItem {
        
        @JsonProperty("row")
        private List<Row> row;

        public List<Row> getRow() {
            return row;
        }

        public void setRow(List<Row> row) {
            this.row = row;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Row {
        private String bas_yy;
        private String regi;
        private String tot;
        private String otdoor_subtot;
        private String indoor_subtot;
		/**
		 * @return the bas_yy
		 */
		public String getBas_yy() {
			return bas_yy;
		}
		/**
		 * @param bas_yy the bas_yy to set
		 */
		public void setBas_yy(String bas_yy) {
			this.bas_yy = bas_yy;
		}
		/**
		 * @return the regi
		 */
		public String getRegi() {
			return regi;
		}
		/**
		 * @param regi the regi to set
		 */
		public void setRegi(String regi) {
			this.regi = regi;
		}
		/**
		 * @return the tot
		 */
		public String getTot() {
			return tot;
		}
		/**
		 * @param tot the tot to set
		 */
		public void setTot(String tot) {
			this.tot = tot;
		}
		/**
		 * @return the otdoor_subtot
		 */
		public String getOtdoor_subtot() {
			return otdoor_subtot;
		}
		/**
		 * @param otdoor_subtot the otdoor_subtot to set
		 */
		public void setOtdoor_subtot(String otdoor_subtot) {
			this.otdoor_subtot = otdoor_subtot;
		}
		/**
		 * @return the indoor_subtot
		 */
		public String getIndoor_subtot() {
			return indoor_subtot;
		}
		/**
		 * @param indoor_subtot the indoor_subtot to set
		 */
		public void setIndoor_subtot(String indoor_subtot) {
			this.indoor_subtot = indoor_subtot;
		}

        
    }
}