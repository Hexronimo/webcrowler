package ru.hexronimo.hyberskill.webcrawler.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Page {

	private String body = "EMPTY";
	private String title = "EMPTY";
	private URL url;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public List<String> findLinks() {
		List<String> urls = new ArrayList<>();
		Pattern pattern = Pattern.compile("<a .* href\\s*=\\s*\\Q\\E .*>", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(body);
		while (matcher.find()) {
			String match = matcher.group();
			urls.add(match);
		}
		return urls;
	}

	public void readPage() {
		// Reading page html from url
		try (InputStream inputStream = this.url.openStream();) {
			String LINE_SEPARATOR = "\n";
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
			StringBuilder stringBuilder = new StringBuilder();
			String nextLine;
			while ((nextLine = reader.readLine()) != null) {
				stringBuilder.append(nextLine);
				stringBuilder.append(LINE_SEPARATOR);
			}
			this.body = stringBuilder.toString();
		} catch (MalformedURLException e) {
			System.out.println();
			System.out.println("Something wrong with URL: " + url.toString());
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Take title of the page: text from <title> to </title>
		int titlestart = body.indexOf("<title>") + 7;
		int titleend = body.indexOf("</title>");
		if (titlestart != -1 && titleend != -1) {
			this.title = "Title:" + body.substring(titlestart, titleend);
		}
	}
}
