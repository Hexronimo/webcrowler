package ru.hexronimo.hyberskill.webcrawler.model;

import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Task {
	private static List<Deque<URL>> depths;
	private static int maxDepth;
	
	public static int getMaxDepth() {
		return maxDepth;
	}

	public static void setMaxDepth(int max) {
		maxDepth = max + 1;
		depths = new ArrayList<>();
		for(int i = 0; i < max + 1; i++) {
			Deque<URL> tasks = new ArrayDeque<>();
			depths.add(tasks);
		}
	}
	
	public static int currentDepth() {
		for (int i = 0; i < depths.size(); i++) {
			if (depths.get(i).size() > 0) return i;
		}
		return -1; //all empty
	}

	public static void addTask(URL url, int depth) {
		boolean contain = false;	
		if (Result.contains(url.toExternalForm())) contain = true; 	// if link already in Result do not add it
		if (contain == false) { 									// if link already in Task do not add it
			for(Deque<URL> tasks : depths) {
				if (tasks.contains(url)) contain = true;
			}
		}
		if(!contain) depths.get(depth).add(url);
	}

	public static List<Deque<URL>> getDepths() {
		return depths;
	}

	public static void setDepths(List<Deque<URL>> depths) {
		Task.depths = depths;
	}
	
}
