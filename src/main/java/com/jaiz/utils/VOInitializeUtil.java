package com.jaiz.utils;

import java.lang.reflect.*;
import java.util.*;

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
        Class<?> superClazz = clazz.getSuperclass();
        do {
            fs = superClazz.getDeclaredFields();
            fList.addAll(Arrays.asList(fs));
            superClazz = superClazz.getSuperclass();
        } while (superClazz != null);

        for (Field f : fList) {
            //echo("成员名=" + f.getName());
            //echo("成员类型=" + f.getType());
            //字段名
            String fieldName = f.getName();
            //public set方法名
            String fieldSetterName = "set" + CharacterUtil.charToUpperCase(fieldName.charAt(0)) + fieldName.substring(1, fieldName.length());
            Method fieldSetter;
            try {
                fieldSetter = clazz.getMethod(fieldSetterName, f.getType());
            } catch (NoSuchMethodException e) {
                System.err.println("类" + clazz.getName() + "的属性" + fieldName + "无对应的setter");
                //e.printStackTrace();
                continue;
            }
            try {

                Class<?> fType = f.getType();
                //echo(fType.isPrimitive());
                if (fType.isPrimitive()) {
                    //处理基本数据类型
                    dealWithPrimitiveType(fType, instance, fieldSetter);
                } else {
                    //处理非基本数据类型
                    dealWithNonPrimitiveType(f, instance, fieldSetter);
                }

            } catch (IllegalAccessException | InvocationTargetException e) {
                System.err.println("成员" + fieldName + "设置值失败");
                //e.printStackTrace();
            }
        }
        return instance;
    }

    /**
     * 处理非基本数据类型
     *
     * @param f
     * @param instance
     * @param setter
     * @param <T>
     */
    private static <T> void dealWithNonPrimitiveType(Field f, T instance, Method setter) throws InvocationTargetException, IllegalAccessException {
        //考虑一下几种情形:
        //1.基本数据类型的装箱
        //2.String
        //Date
        //3.数组
        //4.列表
        //5.set
        //6.Map
        //其他引用类型
        Class<?> fType = f.getType();
        if (fType.getName().startsWith("[")) {
            //数组
            //echo("这是数组:" + fType.getName() + ";元素类型是:" + fType.getComponentType().getName());
        } else if (fType.isAssignableFrom(List.class)) {
            //echo("这是List:" + fType.getName() + ";泛型是:" + f.getGenericType().getTypeName());
            try {
                setter.invoke(instance, initAGenericList(fType, Class.forName(clipGenericTypeName(f.getGenericType().getTypeName()))));
            } catch (ClassNotFoundException e) {
                System.err.println("列表成员" + f.getName() + "实例化失败,无法找到泛型类型");
            }
        } else if (fType.isAssignableFrom(Set.class)) {
            //echo("这是Set:" + fType.getName() + ";泛型是:" + f.getGenericType());
        } else if (fType.isAssignableFrom(Map.class)) {
            //echo("这是Map:" + fType.getName() + ";泛型是:" + f.getGenericType());
        } else if (fType == String.class) {
            //字符串
            setter.invoke(instance, "测试字符串值");
        } else if (fType == Date.class) {
            setter.invoke(instance, new Date());
        } else if (fType == Integer.class ||
                fType == Long.class ||
                fType == Short.class ||
                fType == Byte.class) {
            //整形数值
            setter.invoke(instance, 1);
        } else if (fType == Float.class ||
                fType == Double.class) {
            //浮点型数值
            setter.invoke(instance, 1.23);
        } else if (fType == Boolean.class) {
            //布尔
            setter.invoke(instance, true);
        } else if (fType == Character.class) {
            //字符
            setter.invoke(instance, 'a');
        } else {
            //其他引用类型
            setter.invoke(instance, initialize(fType));
        }
    }

    /**
     * 截取泛型类型名
     * 如java.util.List<java.lang.String>
     * 截取为java.lang.String
     *
     * @param fullName
     * @return
     */
    private static String clipGenericTypeName(String fullName) {
        if(FastStringUtil.isBlank(fullName)){
            return "EMPTY_TYPE_NAME";
        }
        int leftIndex = fullName.indexOf('<');
        int rightIndex = fullName.indexOf('>');
        if (
                //符号存在
                leftIndex >= 0 && rightIndex >= 0 &&
                //位置正确
                leftIndex < rightIndex &&
                //避免越界
                leftIndex<fullName.length() && rightIndex<fullName.length()
        ) {
            return fullName.substring(leftIndex+1,rightIndex);
        }
        return "UNKOWN_GENERIC_TYPE_NAME";
    }

    private static Object initAGenericList(Class<?> fType, Class<?> gType) {
        if (fType == List.class) {
            //使用ArrayList作为默认实现
            ArrayList listInst = new ArrayList(1);
            Object e=initialize(gType);
            listInst.add(e);
            return listInst;
        } else {
            try {
                List listInst = (List) fType.newInstance();
                Object e=initialize(gType);
                listInst.add(e);
                return listInst;
            } catch (InstantiationException | IllegalAccessException e) {
                System.err.println("列表实例化失败");
            }
        }
        return null;
    }

    /**
     * 处理基本数据类型
     *
     * @param fType
     * @param instance
     * @param setter
     * @param <T>
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static <T> void dealWithPrimitiveType(Class<?> fType, T instance, Method setter) throws InvocationTargetException, IllegalAccessException {
        if (fType == int.class ||
                fType == long.class ||
                fType == short.class ||
                fType == byte.class) {
            //整形数值
            setter.invoke(instance, 1);
        } else if (fType == float.class ||
                fType == double.class) {
            //浮点型数值
            setter.invoke(instance, 1.23);
        } else if (fType == boolean.class) {
            //布尔
            setter.invoke(instance, true);
        } else if (fType == char.class) {
            //字符
            setter.invoke(instance, 'a');
        }
    }

}
