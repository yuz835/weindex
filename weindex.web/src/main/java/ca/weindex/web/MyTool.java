package ca.weindex.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyTool {
	public String output() {
		return "hehehehehehe";
	}
	
	public static void main(String[] arg) {
		String reg = "[a-zA-Z][a-zA-Z\\d]+";
		Pattern p = Pattern.compile(reg);
		String s = "alkdj14 32lksfj";
		Matcher m = p.matcher(s);
		System.out.println(m.matches());
	}
}
