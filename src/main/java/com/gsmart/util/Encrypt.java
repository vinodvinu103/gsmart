package com.gsmart.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This is CheckPermissions class. The class is inside nk.util package.
 *
 * @version 1.10 29 June 2009
 * @author Nitin Gupta
 */
public class Encrypt {

	public static void main(String[] args){
		System.out.println(md5("26005008"));
	}
	
	
	public static String md5(String pass){
		StringBuffer hexString = new StringBuffer();
		try{
			byte[] defaultBytes =pass.getBytes();
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(defaultBytes);
			byte messageDigest[] = algorithm.digest();
			for (int i=0;i<messageDigest.length;i++) {
				String hex = Integer.toHexString(0xFF & messageDigest[i]); 
				if(hex.length()==1)
					hexString.append('0');
				hexString.append(hex);
			}
		}
		catch(NoSuchAlgorithmException nsae){
			nsae.printStackTrace();
		}
		return  hexString.toString();
	}
}