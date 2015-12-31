package ca.weindex.common.util;

import java.util.UUID;

public class UUIDGenerator {
	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		while (str.indexOf("-") != -1) {
			str = str.substring(0, str.indexOf("-")) + str.substring(str.indexOf("-") + 1);
		}
		return str;
	}
	
	public static void main(String[] args) {
		System.out.println(getUUID());
	}
}
