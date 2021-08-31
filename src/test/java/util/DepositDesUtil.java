package util;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * 双鱼托管加解密工具类
 * 
 * @author Administrator
 *
 */
public class DepositDesUtil {

	public static String AES_username_key = "mTEA1lJo6731zU8w";
	
	public static String AES_userpwd_key = "RWE32Ha7Lx9";

	public static String usernameEncrypt(String username) {
		try {
			byte[] md5Hash = md5Hash(AES_username_key);
			byte[] aesByte = aesEncrypt(username, md5Hash);
			return bytesToHex(aesByte);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String usernameDecrypt(String username) {
		try {
			byte[] md5Hash = md5Hash(AES_username_key);
			byte[] byyeStr=hex2byte(username.getBytes());
			return new String(aesDecrypt(byyeStr, md5Hash));
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String userpwdEncrypt(String userpwd,String username) {
		try {
			byte[] md5Hash = md5Hash(username+AES_userpwd_key);
			byte[] aesByte = aesEncrypt(userpwd, md5Hash);
			return bytesToHex(aesByte);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String userpwdDecrypt(String userpwd,String username) {
		try {
			byte[] md5Hash = md5Hash(username+AES_userpwd_key);
			byte[] byyeStr=hex2byte(userpwd.getBytes());
			return new String(aesDecrypt(byyeStr, md5Hash));
		} catch (Exception e) {
			return null;
		}
	}

	public static byte[] md5Hash(String str) throws NoSuchAlgorithmException {
		return md5Hash(str, "UTF-8");
	}

	public static byte[] md5Hash(String str, String codeType) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(str.getBytes());
		return md.digest();
	}

	private static byte[] aesIv = { 0x5a, (byte) 0xe3, (byte) 0xf0, 0x46, (byte) 0xcc, 0x11, (byte) 0xb4, 0x45, 0x09,
			0x04, 0x47, 0x58, 0x00, (byte) 0xbf, (byte) 0x88, (byte) 0xd5 }; // AES

	public static byte[] aesEncrypt(String arrB, byte[] key) {
		try {
			// 初始化
			Security.addProvider(new BouncyCastleProvider());

			byte[] keyBytes = initAESKey(key);
			Key secretKey = new SecretKeySpec(keyBytes, "AES");
			IvParameterSpec ivp = new IvParameterSpec(aesIv);

			Cipher encryptCipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
			encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey, ivp);

			return encryptCipher.doFinal(arrB.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static byte[] initAESKey(byte[] key) {
		return key;
	}
	
	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("长度不是偶数");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}
	
	/**
	* 字节数组转16进制
	* @param bytes 需要转换的byte数组
	* @return 转换后的Hex字符串
	*/
	public static String bytesToHex(byte[] bytes) {
	StringBuffer sb = new StringBuffer();
	for(int i = 0; i < bytes.length; i++) {
		String hex = Integer.toHexString(bytes[i] & 0xFF);
		if(hex.length() < 2){
			sb.append(0);
		}
		sb.append(hex);
	}
	return sb.toString();
	}



	public static byte[] aesDecrypt(byte[] arrB, byte[] key) {
		try {
			// 初始化
			Security.addProvider(new BouncyCastleProvider());

			byte[] keyBytes = initAESKey(key);
			Key secretKey = new SecretKeySpec(keyBytes, "AES");
			IvParameterSpec ivp = new IvParameterSpec(aesIv);

			Cipher encryptCipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
			encryptCipher.init(Cipher.DECRYPT_MODE, secretKey, ivp);

			return encryptCipher.doFinal(arrB);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
