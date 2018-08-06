package com.jaiz.utils;

/**
 * 驼峰命名工具
 * @author graci
 *
 */
public class CamelUtil {

    /**
     * 下划线命名法转驼峰命名法
     * @param dash
     * @return
     */
    public static String dash2Camel(String dash) {
        dash = dash.toLowerCase();
        char[] carr = dash.toCharArray();
        boolean caseChange = false;
        StringBuilder sb = new StringBuilder(dash.length());
        for (char c : carr) {
            if (c == '_') {
                caseChange = true;
            } else {
                if (caseChange) {
                    // 小写转大写
                    c = CharacterUtil.charToUpperCase(c);
                    caseChange=false;
                }
                // 拼接
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
}
