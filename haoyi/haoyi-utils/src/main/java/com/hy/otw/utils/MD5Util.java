package com.hy.otw.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class MD5Util {

	public static String getMD5(String inputStr) {
		String algorithm = "";
		if (inputStr == null) {
			return null;
		}
		try {
			algorithm = System.getProperty("MD5.algorithm", "MD5");
		} catch (Exception e) {
			// TODO: handle exception
		}
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			//logger.error(e.getMessage());
		}

		//inputStr = "tsh"+inputStr;

		byte buffer[] = inputStr.getBytes();

		md.update(buffer);

		byte[] bDigest = md.digest();

		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < bDigest.length; i++) {
			if (Integer.toHexString(0xFF & bDigest[i]).length() == 1)
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & bDigest[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & bDigest[i]));
		}

		return md5StrBuff.toString();

	}

	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));

		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
		} catch (Exception exception) {
		}
		return resultString;
	}

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	
	public static final String md5(String password, String salt){
	    //加密方式
	    String hashAlgorithmName = "MD5";
	    //盐：为了即使相同的密码不同的盐加密后的结果也不同
	    ByteSource byteSalt = ByteSource.Util.bytes(salt);
	    //密码
	    Object source = password;
	    //加密次数
	    int hashIterations = 1024;
	    SimpleHash result = new SimpleHash(hashAlgorithmName, source, byteSalt, hashIterations);
	    return result.toString();
	}
}
