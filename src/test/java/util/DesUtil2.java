package util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


/**
 * DES加密、解密(加解密我们自己的账号 去数据库查信息用的)
 *
 * @author WH
 * @version 1.0
 * @time 2016年3月25日 下午1:52:46
 */

public class DesUtil2 {
    private static final String DES = "DES";

    private static final String key = "A1B2C3D4E5F60708";

    public static void main(String[] args) {
    	
    	LogWriteUtil logWriteUtil = new LogWriteUtil();
    	//调用多个账号加密
//    	String desstr = "zhouliyga5tadhje,zhouliytindayeaz";
//    	String[] str = desstr.split(",");
//    	for (int i = 0; i < str.length; i++) {
//    		//logWriteUtil.write("账号-", str[i]);
//    		System.out.println(DesUtil2.encrypt(str[i]));
//		}
    	
    	
    	
    	
    	//调用多个账号解密
//    	String desstr2 = "dAbEvFzufJw=,j9pJSuYwxmE=,Ca+oYnrwsJtDs5lcH77QUg==,Rtt+J4/C5oA=,0VZjDHCZTl1HA2TQK6a6VQ==,srAgNQ+UFJk=,JEKvQo/qGTw=,ogs4XmLTDl4n51gmrA2Vfw==,wGyY4/yXYTA=,jr4VmmZJp5s=,R1MWoSOxrTI=,bskoQJEbXHk=,fEvbJ5DMj7I=,qoWo6cODIpGw/CnPSn2y5Q==,P/saATGK6AM=,EvXQ0xwK5Os=,Lcurz0oIhjryB+g2XFL4cw==,Zt8d0uxXx2I=";
//    	String[] str2 = desstr2.split(",");
//    	for (int i = 0; i < str2.length; i++) {
//    		System.out.println(DesUtil2.decrypt(str2[i]));
//		}
    	
    	
    	
    	//调用单个账号加密
    	System.out.println(DesUtil2.encrypt("te3Cm2obJ1p8muPk"));
    	//调用单个解密
    	System.out.println(DesUtil2.decrypt("1088zz"));
	}
    

    /**
     * 加密
     */
    public static String encrypt(String data) {
        try {
            // BYTE
            byte[] bytes = encrypt(data.getBytes(), key.getBytes());
            // 返回数据
            return new BASE64Encoder().encode(bytes);
        } catch (Exception e) {

            return data;
        }
    }

    /**
     * 解密
     */
    public static String decrypt(String data) {
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            // BYTE
            byte[] bytes = decrypt(decoder.decodeBuffer(data), key.getBytes());
            // 返回数据
            return new String(bytes);
        } catch (Exception e) {

            return data;
        }

    }

    /**
     * DES加密
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }

    /**
     * DES解密
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }

}