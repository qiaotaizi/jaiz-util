package com.jaiz.utils;

/**
 * 正则工具类
 */
public class RegExUtil {

    /**
     * 手机号码正则
     */
    private final static String PHONE_REGEX="^[0-9]{11}$";

    /**
     * 用户登录名正则
     */
    private final static String LOGIN_NAME_REGEX="^\\w{6,12}$";

    /**
     * 用户Email正则
     */
    private final static String EMAIL_REGEX="^[A-Za-z0-9\u4e00-\u9fa5_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

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

    /**
     * 校验email是否符合正则
     * @param email
     * @return
     */
    public static boolean validEmail(String email){
        return email.matches(EMAIL_REGEX);
    }
}
