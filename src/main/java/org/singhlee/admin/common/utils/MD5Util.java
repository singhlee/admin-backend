package org.singhlee.admin.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @program: admin-backend
 * @description:
 * @author: singhlee
 * @date: 2020-07-09 17:56
 **/
@Slf4j
public class MD5Util {
    /**
     * @param text 明文
     * @param key 密钥
     * @return 密文
     */
    // 带秘钥加密
    public static String md5(String text, String key) throws Exception {
        // 加密后的字符串
        String md5str = DigestUtils.md5Hex(text + key);
        log.info("MD5加密后的字符串为:{}" ,md5str);
        return md5str;
    }

    // 不带秘钥加密
    public static String md52(String text) throws Exception {
        // 加密后的字符串
        String md5str = DigestUtils.md5Hex(text);
        log.info("MD52加密后的字符串为:{} 长度：{}" ,md5str,md5str.length());
        return md5str;
    }

    /**
     * MD5验证方法
     *
     * @param text 明文
     * @param key 密钥
     * @param md5 密文
     */
    // 根据传入的密钥进行验证
    public static boolean verify(String text, String key, String md5) throws Exception {
        String md5str = md5(text, key);
        if (md5str.equalsIgnoreCase(md5)) {
            log.info("MD5验证通过");
            return true;
        }
        return false;
    }

    /**
     * MD5验证方法 不带秘钥加密
     *
     * @param text 明文
     * @param md5 密文
     */
    // 根据传入的密钥进行验证
    public static boolean verify(String text,String md5) throws Exception {
        String md5str = md52(text);
        if (md5str.equalsIgnoreCase(md5)) {
            log.info("MD5验证通过");
            return true;
        }
        return false;
    }

}
