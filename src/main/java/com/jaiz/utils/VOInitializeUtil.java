package com.jaiz.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class VOInitializeUtil {

    /**
     * 递归初始化一个类的所有属性
     * 按照javaBean的命名规范
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T initialize(Class<T> clazz) {
        T instance;
        try {
            instance = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            System.err.println("类型" + clazz.getName() + "无法实例化");
            e.printStackTrace();
            return null;
        }
        //获取所有成员,包括继承过来的属性,解决继承问题
        Field[] fs = clazz.getDeclaredFields();
        List<Field> fList = new LinkedList<>(Arrays.asList(fs));
        Class superClazz = clazz.getSuperclass();
        do {
            fs = superClazz.getDeclaredFields();
            fList.addAll(Arrays.asList(fs));
            superClazz = superClazz.getSuperclass();
        } while (superClazz != null);
        clazz.getSuperclass();
        for (Field f : fList) {
            System.out.println("成员名=" + f.getName());
            System.out.println("成员类型=" + f.getType());
            //字段名
            String fieldName = f.getName();
            //public set方法名
            String fieldSetterName = "set" + CharacterUtil.charToUpperCase(fieldName.charAt(0)) + fieldName.substring(1, fieldName.length());
            Method fieldSetter;
            try {
                fieldSetter = clazz.getMethod(fieldSetterName, f.getType());
            } catch (NoSuchMethodException e) {
                System.err.println("属性" + fieldName + "无对应的setter");
                e.printStackTrace();
                continue;
            }
            try {

                Class fType = f.getType();

                if (fType == String.class) {
                    //字符串
                    fieldSetter.invoke(instance, "测试字符串值");
                } else if (fType == Integer.class || fType == int.class ||
                        fType == Long.class || fType == long.class ||
                        fType == Short.class || fType == short.class ||
                        fType == Byte.class || fType == byte.class) {
                    //整形数值
                    fieldSetter.invoke(instance, 1);
                } else if (fType == Float.class || fType == float.class ||
                        fType == Double.class || fType == double.class) {
                    //浮点型数值
                    fieldSetter.invoke(instance, 1.23);
                } else if (fType == Boolean.class || fType == boolean.class) {
                    //布尔
                    fieldSetter.invoke(instance, true);
                } else if (fType == Character.class || fType == char.class) {
                    //字符
                    fieldSetter.invoke(instance, 'a');
                }else if (fType== Array.class){
                    //数组
                    System.out.println(fieldName+"是数组");
                }

            } catch (IllegalAccessException | InvocationTargetException e) {
                System.err.println("成员" + fieldName + "设置值失败");
                e.printStackTrace();
            }
        }
        return instance;
    }

}
