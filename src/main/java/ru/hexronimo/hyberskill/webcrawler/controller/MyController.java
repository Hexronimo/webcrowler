package ru.hexronimo.hyberskill.webcrawler.controller;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.hexronimo.hyberskill.webcrawler.model.Page;

@Controller
public class MyController {

	@GetMapping(value = "/")
	public String mainForm() {
		return "index";
	}

	@PostMapping(value = "/")
	public String mainForm(@RequestParam(value = "url") String url, Model model) {
		// Read html page from entered URL
		Page page = new Page();
		try {
			page.setUrl(new URL(url));
			page.readPage();
			model.addAttribute("htmlbody", page.getBody());
			model.addAttribute("htmltitle", page.getTitle());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("links", page.findLinks());
		return "index";
	}
}
