package com.jaiz.utils;

import java.util.*;

/**
 * 验签工具
 */
public class SignUtil {

    private final static String SIGN_SALT="(*Φ皿Φ*)";

    /**
     * 签名参数值
     */
    private final static String SIGN_KEY_NAME="sign";



    /**
     * 签名的组织:
     * 将所有参数按照字典序组合为形如
     * k1=v1&k2=v2&...&kn=vn的字符串
     * 最后拼上&signKey=${SIGN_SALT}
     * 整体MD5
     * 如果无任何参数,则不进行验签
     * 若某一个参数对应的value是一个字符串数组
     * 则以e1,e2,e3...,en的形式组成这个参数值参与验签
     * 请求传参时将sign=签名值带上
     * 空参数不参与签名
     *
     * 签名的校验
     * 将sign参数先行移除
     * 在按照上述逻辑重新计算签名值,两个值进行对比
     * @param parameterMap
     * @return
     */
    public static boolean signValid(Map<String, String[]> parameterMap) {
        if(parameterMap==null || parameterMap.size()==0) {
            return true;
        }
        String[] signArr=parameterMap.get(SIGN_KEY_NAME);
        if(signArr==null || signArr.length==0){
            return false;
        }
        String clientSign=signArr[0];
        Set<String> keySetCopy=new HashSet<>(parameterMap.keySet());
        keySetCopy.remove(SIGN_KEY_NAME);
        List<String> keyList= new ArrayList<>(keySetCopy);
        Collections.sort(keyList);
        StringBuilder sb=new StringBuilder();
        for(String key:keyList){
            String[] valueArr=parameterMap.get(key);
            if(valueArr==null || valueArr.length==0){
                continue;
            }
            //这里先认为不会以数组进行传参
            String valueForSign= valueArr[0];
            sb.append(key).append("=").append(valueForSign).append("&");
        }
        sb.append("signKey=").append(SIGN_SALT);
        String serverSign=MD5Util.md5(sb.toString());
        System.out.println("sign value should be: "+serverSign);
        return serverSign.equalsIgnoreCase(clientSign);
    }

}
