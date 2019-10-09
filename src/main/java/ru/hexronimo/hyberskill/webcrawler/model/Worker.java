package ru.hexronimo.hyberskill.webcrawler.model;

import java.net.URL;

public class Worker implements Runnable {
	private URL url;
	private int depth;
	
	@Override
	public void run() {
			Page page = new Page();
			try {
				page.setUrl(url);
				if (page.isHTML()) {
					page.readPage();
					page.setTitle();
					
					// store this link in the final Result
					Result.store(url.toExternalForm(), page.getTitle(), depth);
					
					// if depth limit not reached add children-links to Task					
					if (depth + 1 < Task.getMaxDepth()) { 
						System.out.println("SEARCH FOR CHILDREN-LINKS");
						String findLink;
						while((findLink = page.nextLink()) != null) {
							Task.addTask(new URL(findLink), depth + 1);
						}
					}
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}
	

}
