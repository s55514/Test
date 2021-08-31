package util;


import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.ccservice.util.http.RequestUtil;
import com.ccservice.util.security.MD5Util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

//加解密采购传的账号密码用的

public class DesUtil {
    private static final  String DES = "DES";
    private static final  String key = "u0FKpzhZa1x1DeWn";

    public static void main(String[] args) throws Exception {
        String username = "";
        String password = "sunfujia52qian";
        username = DesUtil.encrypt(username);
        password = DesUtil.encrypt(password);

        System.out.println("加密username：" + username);
        System.out.println("加密password：" + password);

        username = DesUtil.decrypt(username, key);
        password  = DesUtil.decrypt(password, key);

        System.out.println("解密username：" + username);
        System.out.println("解密password：" + password);

        System.out.println("-----------------------");
    }

    public static String decrypt(String data, String key) throws IOException, Exception {
        try {
            if (data == null)
                return null;
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] buf = decoder.decodeBuffer(data);
            byte[] bt = decrypt(buf, key.getBytes());
            return new String(bt, "UTF-8");
        }
        catch (Exception e) {
            return data;
        }

    }

    /**
     */
    public static String encrypt(String data) throws Exception {
        return new BASE64Encoder().encode(encrypt(data.getBytes("UTF-8"), key.getBytes()));

    }

    /**
     * 加密
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        //生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        //从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        //创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        //Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);
        //用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        //返回
        return cipher.doFinal(data);
    }



    /**
     * Description 根据键值进行解密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
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
    
    
    
    /**
     * MD5加密
     * @param origin
     * @param toUpperCase  true 转大写  false  不转
     * @return
     */
    public String MD5(String origin,Boolean toUpperCase) {
        if(toUpperCase){
            return MD5Util.MD5Encode(origin).toUpperCase();
        }else {
            return MD5Util.MD5Encode(origin, RequestUtil.UTF8);

        }
    }
}
