package com.pcwk.ehr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.pcwk.ehr.service.TemperatureService;

@Controller
public class TemperatureController {

    @Autowired
    private TemperatureService temperatureService;

    @GetMapping("/patients.do")
    public ModelAndView getPatients(
            @RequestParam("pageNo") int pageNo,
            @RequestParam("numOfRows") int numOfRows) {

        String result = temperatureService.getPatients(pageNo, numOfRows);

        ModelAndView mav = new ModelAndView("/patientsView");
        mav.addObject("result", result); // JSON 문자열 그대로 넘김

        return mav;
    }
}
