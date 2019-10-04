package ru.hexronimo.hyberskill.webcrawler.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MyController {

	@GetMapping(value = "/")
	public String mainForm() {
		return "index";
	}

	@PostMapping(value = "/")
	public String mainForm(@RequestParam(value = "url") String url, Model model) {
		String siteText = ""; 
		try(InputStream inputStream = new URL(url).openStream();) {
			String LINE_SEPARATOR = "\n";
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
			StringBuilder stringBuilder = new StringBuilder();
			String nextLine;
			while ((nextLine = reader.readLine()) != null) {
				stringBuilder.append(nextLine);
				stringBuilder.append(LINE_SEPARATOR);
			}

			siteText = stringBuilder.toString();
		} catch (MalformedURLException e) {
			System.out.println();
			System.out.println("Something wrong with this URL");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		model.addAttribute("htmlbody", siteText);
		// From <title> to </title>
		int titlestart = siteText.indexOf("<title>") + 7;
		int titleend = siteText.indexOf("</title>");
		String title = "";
		if (titlestart != -1 && titleend != -1) {
			title = "Title:" + siteText.substring(titlestart,titleend);
		}
		model.addAttribute("sitetitle", title);
		return "index";
	}
}
