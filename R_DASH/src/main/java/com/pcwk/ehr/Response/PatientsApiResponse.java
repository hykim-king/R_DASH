package com.pcwk.ehr.Response;

import java.util.List;
import java.util.Map;

import com.pcwk.ehr.domain.PatientsDTO;

public class PatientsApiResponse {
	private List<Map<String, Object>> HeatWaveCasualtiesRegion;

    public List<Map<String, Object>> getHeatWaveCasualtiesRegion() {
        return HeatWaveCasualtiesRegion;
    }

    public void setHeatWaveCasualtiesRegion(List<Map<String, Object>> heatWaveCasualtiesRegion) {
        HeatWaveCasualtiesRegion = heatWaveCasualtiesRegion;
    }

    public static class HeatWaveCasualtiesRegionItem {
        private List<Head> head;
        private List<Row> row;

        public List<Head> getHead() {
            return head;
        }

        public void setHead(List<Head> head) {
            this.head = head;
        }

        public List<Row> getRow() {
            return row;
        }

        public void setRow(List<Row> row) {
            this.row = row;
        }
    }

    public static class Head {
        private Integer totalCount;
        private String numOfRows;
        private String pageNo;
        private String type;
        private Result RESULT;

        public Integer getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(Integer totalCount) {
            this.totalCount = totalCount;
        }

        public String getNumOfRows() {
            return numOfRows;
        }

        public void setNumOfRows(String numOfRows) {
            this.numOfRows = numOfRows;
        }

        public String getPageNo() {
            return pageNo;
        }

        public void setPageNo(String pageNo) {
            this.pageNo = pageNo;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Result getRESULT() {
            return RESULT;
        }

        public void setRESULT(Result RESULT) {
            this.RESULT = RESULT;
        }
    }

    public static class Result {
        private String resultCode;
        private String resultMsg;

        public String getResultCode() {
            return resultCode;
        }

        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        public String getResultMsg() {
            return resultMsg;
        }

        public void setResultMsg(String resultMsg) {
            this.resultMsg = resultMsg;
        }
    }

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