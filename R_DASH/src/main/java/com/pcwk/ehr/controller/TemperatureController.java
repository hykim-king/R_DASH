package com.pcwk.ehr.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pcwk.ehr.domain.PatientsDTO;
import com.pcwk.ehr.service.TemperatureService;

@Controller
@RequestMapping("/temperature")
public class TemperatureController {

    @Autowired
    private TemperatureService temperatureService;
    
    @GetMapping("/summary")
    @ResponseBody
    public List<PatientsDTO> getPatientsSummary(
            @RequestParam(required = false) String groupType,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String sidoNm) throws SQLException {

        Map<String, Object> param = new HashMap<>();
        param.put("groupType", groupType);
        param.put("year", year);
        param.put("sidoNm", sidoNm);

        return temperatureService.selectPatientsSummary(param);
    }
    
    @PostMapping("/save-patients.do")
    @ResponseBody
    public String insertPatient() throws SQLException {
    	temperatureService.insertPatient();
        return "Patient 등록 완료!";
    }
    
    @GetMapping("/select-patients.do")
    @ResponseBody
    public List<PatientsDTO> getPatients() throws SQLException {
        return temperatureService.getAllPatients();
    }
    
    @PostMapping("/save-nowcast.do")
    @ResponseBody
    public String mergeNowcast() throws SQLException {
    	temperatureService.insertNowcast();
    	return "NowCast 등록 완료!";
    }
    
    @GetMapping("/patientsPage")
    public String patientsPage(Model model) throws SQLException {
        List<String> yearList = temperatureService.getYearList();
        List<String> sidoList = temperatureService.getSidoList();

        model.addAttribute("yearList", yearList);
        model.addAttribute("sidoList", sidoList);

        return "stats/Test1";
    }

    @GetMapping("/main.do")
    public String main() {
        return "stats/main";
    }
    
}
