<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- DataTables JS -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<!-- DataTables CSS -->
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.13.6/css/jquery.dataTables.min.css"/>
<script type="text/javascript" src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
   
<div class="content-wrapper">
    <!-- 피해/복구 현황 그래프 -->
    <div class="canvasContainer">
        <canvas id="fireSafeChart"></canvas>
    </div>

    <h2>시/군/구별 소방서 개수</h2>
    <table id="firestationTable" class="display" border="1">
        <thead>
            <tr>
                <th>순위</th>
                <th>지역</th>
                <th>소방서 개수</th>
            </tr>
        </thead>
        <tbody>
            <!-- AJAX로 채워짐 -->
        </tbody>
    </table>
</div>

<script src="${pageContext.request.contextPath}/resources/js/fire.js"></script>
