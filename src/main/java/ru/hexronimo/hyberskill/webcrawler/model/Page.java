package ru.hexronimo.hyberskill.webcrawler.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Page {
	
	private boolean isHTML = false;
	private String body = null;
	private String title = null;
	private URL url;
	private URLConnection connection;

	private Matcher matcher; //to find links one by one

	public String getBody() {
		if (body == null) return "EMPTY / UNREADABLE";
		return body;
	}

	public String getTitle() {
		if (this.title == null) return "EMPTY / UNREADABLE";
		return this.title;
	}

	public URL getUrl() {
		return url;
	}
	
	public boolean isHTML() {
		return isHTML;
	}

	public void setHTML(boolean isHTML) {
		this.isHTML = isHTML;
	}

	public void setUrl(URL url) throws NullPointerException, FileNotFoundException, IOException, Exception {
		this.url = url;
		URLConnection connection = url.openConnection(); // this may throw NullPointerException
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0");
		if (connection.getContentType().startsWith("text/html")) { 
			isHTML = true;
			this.connection = connection;
		}
	}
	
	public String nextLink() {
		Pattern pattern = Pattern.compile("<a.*?\\shref\\s{0,1}=\\s{0,1}[\"'](?<url>.+?)[\"'][\\s>]", Pattern.CASE_INSENSITIVE);
		if (matcher == null) matcher = pattern.matcher(body);
		if (matcher.find()) {
			String match = matcher.group("url");
			if (match.startsWith("#")) nextLink(); // skip if anchor
			if (match == null || match.trim().length() == 0) nextLink(); //skip if empty
			match = normalizeUrl(match);	
			return match;
		}
		return null;
	}

	public void readPage() throws FileNotFoundException, IOException {
		if (isHTML) { 
			// Reading page html from url
			try (InputStream inputStream = connection.getInputStream()) {
				String LINE_SEPARATOR = System.getProperty("line.separator");
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
				StringBuilder stringBuilder = new StringBuilder("");
				String nextLine;
				while ((nextLine = reader.readLine()) != null) {
					stringBuilder.append(nextLine);
					stringBuilder.append(LINE_SEPARATOR);
				}
				this.body = stringBuilder.toString();
			}
		}
	}
	
	public void setTitle() throws IOException {
		if (isHTML) {
			if (body == null) readPage(); // just for code re-use
			
			// finding <title> tag
			int titlestart = body.indexOf("<title") + 6;
			int titleend = body.indexOf("</title>");
			
			// if title presented 
			if (titlestart != -1 && titleend != -1) {
				StringBuilder title = new StringBuilder(body.substring(titlestart, titleend));
				
				// fix titles with parameters like <title something="whatever">Caption</title>
					if (title.toString().startsWith(">")) { 
						this.title = title.substring(1);
					} else {
						this.title = title.substring(title.indexOf(">") + 1);
					}
			} else {
				this.title = "EMPTY";
			}
		}
	}
	
	private String normalizeUrl(String url) {
		StringBuilder result = new StringBuilder("");
		
		// cutting anchors, filename and GET params, is something like "http://example.com/dir/"
		String base = this.url.getProtocol() + "://" + this.url.getHost() + this.url.getPath();

		if (url.startsWith("//")) { // "//en.wikipedia.org"
			result.append("http:");
		} else if (url.startsWith("/")) { // "/dir/something.html"
			result.append(this.url.getProtocol()); 
			result.append("://");
			result.append(this.url.getHost());
		} else if (!url.startsWith("www.") && !url.startsWith("http://") && !url.startsWith("https://")) { // "dir/something.html"
			result.append(base);
		}
		result.append(url);
		return result.toString();
	}
}
