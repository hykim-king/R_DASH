package com.pcwk.ehr.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.pcwk.ehr.domain.PatientsDTO;
import com.pcwk.ehr.service.TemperatureService;

@RestController
@RequestMapping("/patients")
public class TemperatureController {

    @Autowired
    private TemperatureService temperatureService;
    
    @PostMapping("/save-patients.do")
    public String insertPatient() throws SQLException {
    	temperatureService.insertPatient();
        return "Patient 등록 완료!";
    }
    
    @GetMapping("/select-patients.do")
    public List<PatientsDTO> getPatients() throws SQLException {
        return temperatureService.getAllPatients();
    }
    
    @PostMapping("/save-nowcast.do")
    public String insertNowcast() throws SQLException {
    	temperatureService.insertNowcast();
    	return "NowCast 등록 완료!";
    	
    }
    
}
