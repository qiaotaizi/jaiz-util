package com.jaiz.utils;

import java.util.Random;

/**
 * 随机字符串获取工具类
 */
public class RandomUtil {

    /**
     * 在Random中不指定种子,
     * 将会默认采用当前时间的毫秒数作为种子
     * 随机性比指定种子时更强
     */
    private static final Random randomGenerator=new Random();

    /**
     * 获取6位随机数
     * @return
     */
    public static final String randomNumber6(){
        return randomNumber(6);
    }

    /**
     * 获取n位随机数
     * @return
     */
    private static final String randomNumber(int length){
        StringBuilder sb=new StringBuilder(length);
        for (int i=0;i<length;i++){
            sb.append(randomGenerator.nextInt(10));//获取0-9之间的整数
        }
        return sb.toString();
    }
}
