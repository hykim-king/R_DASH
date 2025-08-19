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

import com.pcwk.ehr.domain.NowcastDTO;
import com.pcwk.ehr.domain.PatientsDTO;
import com.pcwk.ehr.service.TemperatureService;

@Controller
@RequestMapping("/temperature")
public class TemperatureController {

	@Autowired
	private TemperatureService temperatureService;

	@GetMapping("/weather.do")
	@ResponseBody
	public List<NowcastDTO> getTopWeather(@RequestParam String category) {

		switch (category) {
		case "T1H":
			return temperatureService.getTopT1H();
		case "REH":
			return temperatureService.getTopREH();
		case "RN1":
			return temperatureService.getTopRN1();
		default:
			return null;
		}
	}

	@GetMapping("/summary.do")
	@ResponseBody
	public List<PatientsDTO> getPatientsSummary(@RequestParam(required = false) String groupType,
			@RequestParam(required = false) String year, @RequestParam(required = false) String sidoNm)
			throws SQLException {

		Map<String, Object> param = new HashMap<>();
		param.put("groupType", groupType);
		param.put("sidoNm", sidoNm);
		if (year != null && !year.isEmpty()) {
			param.put("year", Integer.parseInt(year));
		} else {
			param.put("year", null); // 연도가 선택되지 않으면 null을 전달
		}

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

	@GetMapping("/statsPage")
	public String statsPage(Model model) throws SQLException {
		List<String> yearList = temperatureService.getYearList();
		List<String> sidoList = temperatureService.getSidoList();

		model.addAttribute("yearList", yearList);
		model.addAttribute("sidoList", sidoList);
		model.addAttribute("pageType", "weather"); // 기본값: weather

		return "stats/statsMain";
	}

	@GetMapping("/main.do")
	public String main() {
		return "stats/main";
	}

}
