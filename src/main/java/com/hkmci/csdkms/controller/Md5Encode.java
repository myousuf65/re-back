package com.hkmci.csdkms.controller;

import java.math.BigInteger;
import java.security.MessageDigest;

public class Md5Encode {
	/**
	 * MD5 encript for a string
	 *
	 * @param str
	 * @return
	 */
	 public String getMD5(String str) {
		 String ret = null;
		 try {
		 // Generate MD5 disgest
		 MessageDigest md = MessageDigest.getInstance("MD5");
		 // count md5 function
		 md.update(str.getBytes());
		 // digest() return md5 hash value，return in 8 bites。d5 hash value is 16 bite's hex value，it's 8 bite in actual
		 
		 ret = new BigInteger(1, md.digest()).toString(16);
		 
		 } catch (Exception e) {
		 //throw new SpeedException("MD5 Error");
		 e.printStackTrace();
		 }
		 return ret;
	 }
}
