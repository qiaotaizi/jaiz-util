package com.jaiz.utils;

/**
 * 快捷的字符串工具类
 * @author graci
 *
 */
public class FastStringUtil {

    /**
     * 判断字符串为空
     * 无法处理换行符,制表符等特殊字符
     * 仅对null值和全空格进行判断
     * @param str
     * @return
     */
    public static final boolean isBlank(String str) {
        return str==null || "".equals(str.trim());
    }
    
    /**
     * 判断字符串非空
     * @param str
     * @return
     */
    public static final boolean isNotBlank(String str) {
        return !isBlank(str);
    }
}
