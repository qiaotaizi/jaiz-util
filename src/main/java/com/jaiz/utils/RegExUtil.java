package com.jaiz.utils;

/**
 * 正则工具类
 */
public class RegExUtil {

    private final static String PHONE_REGEX="^[0-9]{11}$";

    private final static String LOGIN_NAME_REGEX="^\\w{6,12}$";

    /**
     * 校验手机号是否符合正则
     * 注意,未进行空值校验,可能抛出空值异常
     * @param phoneNo
     * @return
     */
    public static boolean validPhone(String phoneNo){
        return phoneNo.matches(PHONE_REGEX);
    }

    /**
     * 校验用户名是否符合正则
     * @param loginName
     * @return
     */
    public static boolean validLoginName(String loginName){
        return loginName.matches(LOGIN_NAME_REGEX);
    }

    public static void main(String[] args) {
        String s="asd123_@Ah";
        System.out.println(validLoginName(s));

    }
}
