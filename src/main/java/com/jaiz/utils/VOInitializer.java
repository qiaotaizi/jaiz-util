package com.jaiz.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class VOInitializer {

    /**
     * 开关控制
     */
    private final boolean on = true;
    private final boolean off = false;

    private final boolean echoSwitch = on;

    /**
     * 控制台输出
     * 取代System.out.println()
     */
    private void echo(String str) {
        if (echoSwitch) {
            System.out.println(str);
        }
    }

    /**
     * 控制台输出
     * 取代System.out.println()
     */
    private void echo(Boolean bool) {
        if (echoSwitch) {
            System.out.println(bool);
        }
    }

    /**
     * 调用标准异常流输出异常
     *
     * @param err
     */
    private void err(String err) {
        System.err.println(err);
    }


    /**
     * 对象类型的实例化
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T universalInit(Class<T> clazz) {
        return universalInit(clazz, null, null);
    }

    /**
     * 集合类型的实例化
     *
     * @param clazz
     * @param gType
     * @param <T>
     * @param <G>
     * @return
     */
    public <T, G> T universalInit(Class<T> clazz, Class<G> gType) {
        return universalInit(clazz, gType, null);
    }

    /**
     * 常用VO对象的递归实例化方法实
     * 用于快速生成一个大的对象
     * 里面还封装了常用类型的实例化
     * 支持Map类型的实例化
     *
     * @param clazz
     * @param <T>   要初始化的对象的类型
     * @param <G1>  集合类泛型类型,Map类键泛型
     * @param <G2>  Map类值泛型
     * @return
     */
    public <T, G1, G2> T universalInit(Class<T> clazz, Class<G1> gType1, Class<G2> gType2) {

        //基本数据类型
        if (clazz.isPrimitive()) {
            return initPrimitive(clazz);
        }

        //字符串
        if (clazz == String.class) {
            return (T) initString();
        }

        //日期
        if (clazz == Date.class) {
            return (T) initDate();
        }
        if (clazz == Calendar.class) {
            return (T) initCalenda();
        }

        //八大装箱
        if (isBoxedPrimitive(clazz)) {
            echo("实例化装箱");
            return initBoxedPrimitive(clazz);
        }

        //数组
        if (isArrayType(clazz)) {
            //数组元素类型
            return initArray(clazz);
        }

        //List
        if (isListType(clazz)) {
            return (T) initList(clazz, gType1);
        }

        //Set
        if (isSetType(clazz)) {
            return (T) initSet(clazz, gType1);
        }

        //Map
        if (isMapType(clazz)) {
            echo("是Map");
            return (T) initMap(clazz, gType1, gType2);
        }

        //其他对象,认为是javaBean
        //按照getter/setter规则来处理
        return initJavaBean(clazz);
    }

    private <T> T initJavaBean(Class<T> clazz) {
        T inst;
        try {
            inst= clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            err("java Bean实例化失败");
            e.printStackTrace();
            return null;
        }
        //找到所有成员属性
        //包括基类
        Class superClass=clazz;
        List<Field> fList=new ArrayList<>(clazz.getDeclaredFields().length);
        do{
            fList.addAll(Arrays.asList(superClass.getDeclaredFields()));
            superClass=superClass.getSuperclass();
        }while (superClass.getSuperclass()!=null);
        //找到所有public Setter方法
        //与成员进行名称匹配
        //匹配到时调用setter初始化对象成员
        Method[] allMethods = clazz.getMethods();

        for(Field f:fList){
            for(Method m:allMethods){
                if(isSetterForMember(f,m)){
                    echo("成员"+f.getName()+"匹配到setter");
                    Class fType=f.getType();
                    Object fInst=universalInit(fType);
                    try {
                        m.invoke(inst,fInst);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        err("成员"+f.getName()+"的setter调用失败");
                        e.printStackTrace();
                    }
                }
            }
        }
        return inst;
    }

    /**
     * 判断方法是成员的setter
     * @param f
     * @param m
     * @return
     */
    private boolean isSetterForMember(Field f, Method m) {
        String fieldName=f.getName();
        String setterName=m.getName();
        String setterName_="set"+CharacterUtil.charToUpperCase(fieldName.charAt(0))+fieldName.substring(1);
        return setterName.equals(setterName_);

    }

    private <T, G1, G2> Map<G1, G2> initMap(Class<T> clazz, Class<G1> gType1, Class<G2> gType2) {
        try {
            Map<G1, G2> mapInst = (Map<G1, G2>) clazz.newInstance();
            G1 key = universalInit(gType1);
            G2 value = universalInit(gType2);
            mapInst.put(key, value);
            return mapInst;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        err("map实例化失败");
        return null;
    }

    private <T> boolean isMapType(Class<T> clazz) {
        return Map.class.isAssignableFrom(clazz);
    }

    private <T, G> Set<G> initSet(Class<T> clazz, Class<G> gType) {
        try {
            Set<G> setInst = (Set<G>) clazz.newInstance();
            setInst.add(universalInit(gType));
            return setInst;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        err("set实例化失败");
        return null;
    }

    private <T> boolean isSetType(Class<T> clazz) {
        return Set.class.isAssignableFrom(clazz);
    }

    private <T, G> List<G> initList(Class<T> clazz, Class<G> gType) {
        //一定是某个实现类才可能实例化
        //获取List实例
        try {
            List<G> listInst = (List<G>) clazz.newInstance();
            //获取泛型类型
            //暂时认为List中不会存储List
            listInst.add(universalInit(gType));
            return listInst;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        err("list实例化失败");
        return null;
    }

    /**
     * 判断是List或其实现类
     *
     * @param clazz
     * @param <T>
     * @return
     */
    private <T> boolean isListType(Class<T> clazz) {
        return List.class.isAssignableFrom(clazz);
    }

    private <T> T initArray(Class<T> clazz) {
        Class cType = clazz.getComponentType();
        Object arr = Array.newInstance(cType, 1);
        Array.set(arr, 0, universalInit(cType));
        return (T) arr;
    }

    /**
     * 判断类型为数组
     *
     * @param clazz
     * @param <T>
     * @return
     */
    private <T> boolean isArrayType(Class<T> clazz) {
        return clazz.getName().startsWith("[");
    }

    /**
     * 实例化基本装箱类型
     *
     * @param clazz
     * @param <T>
     * @return
     */
    private <T> T initBoxedPrimitive(Class<T> clazz) {
        if (clazz == Byte.class) {
            //整型
            return (T) Byte.valueOf((byte) 1);
        }

        if (clazz == Short.class) {
            return (T) Short.valueOf((short) 1);
        }

        if (clazz == Integer.class) {
            return (T) Integer.valueOf(1);
        }

        if (clazz == Long.class) {
            return (T) Long.valueOf(1l);
        }

        if (clazz == Float.class) {
            //浮点
            return (T) Float.valueOf(1.23f);
        }
        if (clazz == Double.class) {
            return (T) Double.valueOf(1.23);
        }

        if (clazz == Boolean.class) {
            //布尔
            return (T) Boolean.valueOf(true);
        }

        if (clazz == Character.class) {
            //字符
            return (T) Character.valueOf('a');
        }
        //实际上不会出现
        err("未处理的基本类型:" + clazz.getName());
        return null;
    }

    private <T> boolean isBoxedPrimitive(Class<T> clazz) {
        if (clazz == Byte.class
                || clazz == Short.class
                || clazz == Integer.class
                || clazz == Long.class
                || clazz == Float.class
                || clazz == Double.class
                || clazz == Boolean.class
                || clazz == Character.class) {
            return true;
        }
        return false;
    }

    private Calendar initCalenda() {
        return Calendar.getInstance();
    }

    private Date initDate() {
        return new Date();
    }

    private String initString() {
        return "测试字符串";
    }

    /**
     * 处理基本数据类型
     *
     * @param clazz
     * @param <T>
     * @return
     */
    private <T> T initPrimitive(Class<T> clazz) {
        if (clazz == byte.class) {
            //整型
            return (T) Byte.valueOf((byte) 1);
        }

        if (clazz == short.class) {
            return (T) Short.valueOf((short) 1);
        }

        if (clazz == int.class) {
            return (T) Integer.valueOf(1);
        }

        if (clazz == long.class) {
            return (T) Long.valueOf(1l);
        }

        if (clazz == float.class) {
            //浮点
            return (T) Float.valueOf(1.23f);
        }
        if (clazz == double.class) {
            return (T) Double.valueOf(1.23);
        }

        if (clazz == boolean.class) {
            //布尔
            return (T) Boolean.valueOf(true);
        }

        if (clazz == char.class) {
            //字符
            return (T) Character.valueOf('a');
        }
        //实际上不会出现
        err("未处理的基本类型:" + clazz.getName());
        return null;
    }


}
