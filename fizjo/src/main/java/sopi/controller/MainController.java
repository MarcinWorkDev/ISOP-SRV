package sopi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

	String defaultWebpage = "index";
	
	@RequestMapping(value="/")
	public String mainController() {
		return "redirect:/user";
	}
	
	@RequestMapping(value="/user")
	public String userPage(Model model){
				
		model.addAttribute("title", "Użytkownicy");
		model.addAttribute("contentPage", "template/pageUser");
		return defaultWebpage;
	}
	
	@RequestMapping(value="/profile")
	public String profilePage(Model model){
		
		model.addAttribute("title", "Profile");
		model.addAttribute("contentPage", "template/pageProfile");
		return defaultWebpage;
	}
}