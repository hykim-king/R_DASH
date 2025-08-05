package com.pcwk.ehr.Response;

import java.util.List;

import com.pcwk.ehr.domain.PatientsDTO;

public class PatientsApiResponse {
	private List<Row> row;

    public List<Row> getRow() {
        return row;
    }

    public void setRow(List<Row> row) {
        this.row = row;
    }

    public static class Row {
        private String bas_yy;
        private String regi;
        private String tot;
        private String otdoor_subtot;
        private String indoor_subtot;

        public String getBas_yy() {
            return bas_yy;
        }

        public void setBas_yy(String bas_yy) {
            this.bas_yy = bas_yy;
        }

        public String getRegi() {
            return regi;
        }

        public void setRegi(String regi) {
            this.regi = regi;
        }

        public String getTot() {
            return tot;
        }

        public void setTot(String tot) {
            this.tot = tot;
        }

        public String getOtdoor_subtot() {
            return otdoor_subtot;
        }

        public void setOtdoor_subtot(String otdoor_subtot) {
            this.otdoor_subtot = otdoor_subtot;
        }

        public String getIndoor_subtot() {
            return indoor_subtot;
        }

        public void setIndoor_subtot(String indoor_subtot) {
            this.indoor_subtot = indoor_subtot;
        }
    }
}