package com.pcwk.ehr.controller;

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

    @PostMapping
    public String insertPatient(@RequestBody PatientsDTO dto) {
    	temperatureService.savePatient(dto);
        return "등록 완료";
    }
    
    @GetMapping("/patients.do")
    public List<PatientsDTO> getPatients() {
        return temperatureService.getAllPatients();
    }
    
    @GetMapping("/test-insert")
    public String testInsert() {
        temperatureService.fetchAndSaveData();
        return "실행 완료";
    }
    
}
