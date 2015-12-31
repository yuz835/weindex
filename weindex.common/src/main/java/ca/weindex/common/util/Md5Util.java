package ca.weindex.common.util;

import java.security.MessageDigest;

public class Md5Util {
	@SuppressWarnings("restriction")
	public static String getMd5(String src) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(src.getBytes());
			byte tmp[] = md.digest();
			return (new sun.misc.BASE64Encoder()).encode(tmp);
		} catch (Exception e) {
			return src;
		}
	}
	
	public static void main(String[] args) {
		String md5 = getMd5("lkdjsflksdj");
		System.out.println(md5);
	}
}
