package ru.hexronimo.hyberskill.webcrawler.model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Result {
	
	private static Storage<String, String, Integer> result = new Storage<>();
	private static Result instance = null;
	private Result() {}
	
   public static Result getInstance() {
	      if(instance == null) {
	         instance = new Result();
	      }
	      return instance;
	}
   
   public static void writeToFile(String path) {
	   try (FileWriter fw = new FileWriter(path)){
		   	int i = 0;
			for(String url : result.getFirsts()) {
				StringBuilder sb = new StringBuilder("");  
				sb.append("|_");
				for (int j = 0; j < result.getThird(i); j++) {
					sb.append("_");
				}
				sb.append(url);
				sb.append(" - ");
				sb.append(result.getSecond(i));
				sb.append("\n");
				i++;
				fw.write(sb.toString());
			}
			fw.flush();
		} catch (IOException e) {
			System.out.println("Failed to write to a file");
		}
   }
   
   public static void store(String url, String title, Integer depth) {
	   result.put(url, title, depth);
	   System.out.println("STORED: " + url + " | " + title + " | " + depth);
   }
   
   public static boolean contains(String url) {
	   	if (result.getFirsts().contains(url)) return true;
	   	return false;
   }
	
	static class Storage<U, T, D> {  // url, title, depth
		private List<U> first = new ArrayList<>();
		private List<T> second = new ArrayList<>();
		private List<D> third = new ArrayList<>();
		
		void put(U u, T t, D d) {
			if (!first.contains(u)) {
				first.add(u);
				second.add(t);
				third.add(d);
			} 
		}
		List<U> getFirsts () {
			return first;
		}
		T getSecond (int i) {
			return second.get(i);
		}
		D getThird (int i) {
			return third.get(i);
		}
	}
}
