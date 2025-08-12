package com.pcwk.ehr.Response; // 패키지명은 사용하시는 프로젝트에 맞게 조정해주세요.

import java.util.List;

// 최상위 응답 객체
public class NowcastApiResponse {
    private Response response;

    // 기본 생성자
    public NowcastApiResponse() {
    }

    // 모든 필드를 인자로 받는 생성자
    public NowcastApiResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    // Response 클래스 정의
    public static class Response {
        private Header header;
        private Body body;

        // 기본 생성자
        public Response() {
        }

        // 모든 필드를 인자로 받는 생성자
        public Response(Header header, Body body) {
            this.header = header;
            this.body = body;
        }

        // Getter
        public Header getHeader() {
            return header;
        }

        // Setter
        public void setHeader(Header header) {
            this.header = header;
        }

        // Getter
        public Body getBody() {
            return body;
        }

        // Setter
        public void setBody(Body body) {
            this.body = body;
        }
    }

    // Header 클래스 정의 (static 내부 클래스로 선언)
    public static class Header {
        private String resultCode;
        private String resultMsg;

        // 기본 생성자
        public Header() {
        }

        // 모든 필드를 인자로 받는 생성자
        public Header(String resultCode, String resultMsg) {
            this.resultCode = resultCode;
            this.resultMsg = resultMsg;
        }

        // Getter
        public String getResultCode() {
            return resultCode;
        }

        // Setter
        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        // Getter
        public String getResultMsg() {
            return resultMsg;
        }

        // Setter
        public void setResultMsg(String resultMsg) {
            this.resultMsg = resultMsg;
        }
    }

    // Body 클래스 정의 (static 내부 클래스로 선언)
    public static class Body {
        private String dataType;
        private Items items;
        private int pageNo;
        private int numOfRows;
        private int totalCount;

        // 기본 생성자
        public Body() {
        }

        // 모든 필드를 인자로 받는 생성자
        public Body(String dataType, Items items, int pageNo, int numOfRows, int totalCount) {
            this.dataType = dataType;
            this.items = items;
            this.pageNo = pageNo;
            this.numOfRows = numOfRows;
            this.totalCount = totalCount;
        }

        // Getter
        public String getDataType() {
            return dataType;
        }

        // Setter
        public void setDataType(String dataType) {
            this.dataType = dataType;
        }

        // Getter
        public Items getItems() {
            return items;
        }

        // Setter
        public void setItems(Items items) {
            this.items = items;
        }

        // Getter
        public int getPageNo() {
            return pageNo;
        }

        // Setter
        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        // Getter
        public int getNumOfRows() {
            return numOfRows;
        }

        // Setter
        public void setNumOfRows(int numOfRows) {
            this.numOfRows = numOfRows;
        }

        // Getter
        public int getTotalCount() {
            return totalCount;
        }

        // Setter
        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }
    }

    // Items 클래스 정의 (static 내부 클래스로 선언)
    public static class Items {
        private List<Item> item; // JSON의 'item' 배열을 List<Item>으로 매핑

        // 기본 생성자
        public Items() {
        }

        // 모든 필드를 인자로 받는 생성자
        public Items(List<Item> item) {
            this.item = item;
        }

        // Getter
        public List<Item> getItem() {
            return item;
        }

        // Setter
        public void setItem(List<Item> item) {
            this.item = item;
        }
    }

    // Item (각 데이터 항목) 클래스 정의 (static 내부 클래스로 선언)
    public static class Item {
        private String baseDate;
        private String baseTime;
        private String category;
        private int nx;
        private int ny;
        private String obsrValue; // 값이 실수일 수 있으므로 String으로 받아 후처리하는 것이 안전합니다.

        // 기본 생성자
        public Item() {
        }

        // 모든 필드를 인자로 받는 생성자
        public Item(String baseDate, String baseTime, String category, int nx, int ny, String obsrValue) {
            this.baseDate = baseDate;
            this.baseTime = baseTime;
            this.category = category;
            this.nx = nx;
            this.ny = ny;
            this.obsrValue = obsrValue;
        }

        // Getter
        public String getBaseDate() {
            return baseDate;
        }

        // Setter
        public void setBaseDate(String baseDate) {
            this.baseDate = baseDate;
        }

        // Getter
        public String getBaseTime() {
            return baseTime;
        }

        // Setter
        public void setBaseTime(String baseTime) {
            this.baseTime = baseTime;
        }

        // Getter
        public String getCategory() {
            return category;
        }

        // Setter
        public void setCategory(String category) {
            this.category = category;
        }

        // Getter
        public int getNx() {
            return nx;
        }

        // Setter
        public void setNx(int nx) {
            this.nx = nx;
        }

        // Getter
        public int getNy() {
            return ny;
        }

        // Setter
        public void setNy(int ny) {
            this.ny = ny;
        }

        // Getter
        public String getObsrValue() {
            return obsrValue;
        }

        // Setter
        public void setObsrValue(String obsrValue) {
            this.obsrValue = obsrValue;
        }
    }
}