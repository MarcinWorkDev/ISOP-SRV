package sopi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

	@RequestMapping(value="/")
	public @ResponseBody String index(){
		return "SOPI Site Core is running. Only REST API is available!";
	}
}
