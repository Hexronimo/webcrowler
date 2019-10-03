package ru.hexronimo.hyberskill.webcrawler.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MyController {

	@RequestMapping(value = "/")
	public String mainForm() {
		return "index";
	}

	@RequestMapping(value = "/submit")
	public String mainForm(@RequestParam(value = "url") String url) {
		InputStream inputStream;
		try {
			inputStream = new URL(url).openStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
			StringBuilder stringBuilder = new StringBuilder();
			String nextLine;
			while ((nextLine = reader.readLine()) != null) {
				stringBuilder.append(nextLine);
				stringBuilder.append("\n");
			}

			String siteText = stringBuilder.toString();
			System.out.println(siteText);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "index";
	}
}
