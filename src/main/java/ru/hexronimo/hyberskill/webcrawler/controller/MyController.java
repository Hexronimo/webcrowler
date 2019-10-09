package ru.hexronimo.hyberskill.webcrawler.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.hexronimo.hyberskill.webcrawler.model.Result;
import ru.hexronimo.hyberskill.webcrawler.model.Task;
import ru.hexronimo.hyberskill.webcrawler.model.Worker;

@Controller
public class MyController {

	@GetMapping(value = "/")
	public String mainForm() {
		return "index";
	}

	@PostMapping(value = "/")
	public String mainForm(
			@RequestParam(value = "url") String url,
			@RequestParam(value = "workers", defaultValue = "1") Integer threads,
			@RequestParam(value = "depth", defaultValue = "1") Integer depth,
			@RequestParam(value = "time", defaultValue = "180") Integer time,
			@RequestParam(value = "usedepth", defaultValue = "0") Integer useDepth,
			@RequestParam(value = "usetime", defaultValue = "0") Integer useTime,
			Model model) {
		// parse params
		// System.out.println(threads + ", " + depth + ", " + time + ", " + useDepth + ", " + useTime);
		String error = "";
		if (url == null || "".equals(url.trim())) {
			error = "Error: URL is empty";
			model.addAttribute("error", error);
			return "index";
		}
		// default values if checkboxes unchecked
		if (useDepth == 0) {
			depth = 1;
		}
		Task.setMaxDepth(depth);
		System.out.println(depth);
		
		if (useTime == 0) {
			time = 180;
		}
		
		// add first (user entered) link to queue, all others will be found by workers
		try {
			Task.addTask(new URL(url), 0);
		} catch (MalformedURLException e1) {
			error = "Error: not html page";
			model.addAttribute("error", error);
			return "index";
		}
		
		// in that time limit main loop for workers (threads) goes	
		int d = 0;
		while ((d = Task.currentDepth()) > -1) {
			System.out.println();			
			System.out.println("DEPTH:" + d + " files: " + Task.getDepths().get(d).size());
			
			URL u = Task.getDepths().get(d).pollLast();			
			Worker w = new Worker();
			w.setDepth(d);
			w.setUrl(u);
			w.run();
		}
		
		System.out.println("WRITING TO FILE");
		Result.writeToFile("src/main/resources/static/download.txt");
		System.out.println("FINISH");

		model.addAttribute("error", error);
		return "index";
	}
		
}
