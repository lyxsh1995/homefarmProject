package bean;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * MD5加密工具类
 * <p/>
 * 作者：余天然 on 16/5/9 上午11:46
 */
public class MD5 {
    /**
     * 加密
     * @param plaintext 明文
     * @return ciphertext 密文
     */
    public final static String  jiami(String plaintext) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String date = formatter.format(curDate);
        date = date.substring(0,15);
        plaintext = plaintext+"yzr@csnc"+date;
        try {
            byte[] btInput = plaintext.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
}
