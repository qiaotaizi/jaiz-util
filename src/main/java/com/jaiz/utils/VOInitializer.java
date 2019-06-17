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
            inst = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            err("java Bean实例化失败");
            e.printStackTrace();
            return null;
        }
        //找到所有成员属性
        //包括基类
        Class superClass = clazz;
        List<Field> fList = new ArrayList<>(clazz.getDeclaredFields().length);
        do {
            fList.addAll(Arrays.asList(superClass.getDeclaredFields()));
            superClass = superClass.getSuperclass();
        } while (superClass.getSuperclass() != null);
        //找到所有public Setter方法
        //与成员进行名称匹配
        //匹配到时调用setter初始化对象成员
        Method[] allMethods = clazz.getMethods();

        for (Field f : fList) {
            for (Method m : allMethods) {
                if (isSetterForMember(f, m)) {
                    echo("成员" + f.getName() + "匹配到setter");
                    Class fType = f.getType();
                    //这里需要鉴别一下是否属于集合/Map类型,否则在填写泛型的时候,强制传null,会出现异常
                    Object fInst;
                    if (isListType(fType) || isSetType(fType)) {
                        String fullTypeName = f.getGenericType().getTypeName();
                        String gTypeName = clipGTypeName(fullTypeName);
                        try {
                            echo(gTypeName);
                            Class gType = Class.forName(gTypeName);
                            fInst = universalInit(fType, gType);
                        } catch (ClassNotFoundException e) {
                            err("获取集合泛型类型失败");
                            e.printStackTrace();
                            //跳出内层循环,去搞下一个成员
                            break;
                        }
                    } else if (isMapType(fType)) {
                        String gTypeName = f.getGenericType().getTypeName();
                        echo("待裁剪:" + gTypeName);
                        String[] gTypeNames = clipMapGTypeName(gTypeName);
                        try {
                            Class keyType = Class.forName(gTypeNames[0]);
                            Class valueType = Class.forName(gTypeNames[1]);
                            fInst = universalInit(fType, keyType, valueType);
                        } catch (ClassNotFoundException e) {
                            err("Map类型键/值类型获取失败");
                            e.printStackTrace();
                            break;
                        }
                    } else {
                        fInst = universalInit(fType);
                    }
                    try {
                        m.invoke(inst, fInst);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        err("成员" + f.getName() + "的setter调用失败");
                        e.printStackTrace();
                    }
                }
            }
        }
        return inst;
    }

    /**
     * 裁剪Map类型的泛型名
     *
     * @param fullName
     * @return 索引0:键类型名 索引1:值类型名
     */
    private String[] clipMapGTypeName(String fullName) {
        String gTypeName2 = clipGTypeName(fullName);
        String[] gTypeNames = gTypeName2.split(",");
        for (int i = 0; i < gTypeNames.length; i++) {
            gTypeNames[i] = gTypeNames[i].trim();
        }
        return gTypeNames;
    }

    /**
     * 裁剪集合类型的泛型名
     *
     * @param fullName
     * @return
     */
    private String clipGTypeName(String fullName) {
        if (FastStringUtil.isBlank(fullName)) {
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
                        leftIndex < fullName.length() && rightIndex < fullName.length()
        ) {
            return fullName.substring(leftIndex + 1, rightIndex);
        }
        return "UNKOWN_GENERIC_TYPE_NAME";
    }

    /**
     * 判断方法是成员的setter
     *
     * @param f
     * @param m
     * @return
     */
    private boolean isSetterForMember(Field f, Method m) {
        String fieldName = f.getName();
        String setterName = m.getName();
        String setterName_ = "set" + CharacterUtil.charToUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        return setterName.equals(setterName_);

    }

    private <T, G1, G2> Map<G1, G2> initMap(Class<T> clazz, Class<G1> gType1, Class<G2> gType2) {
        try {
            Map mapInst;
            if (clazz == Map.class) {
                //默认采用HashMap作为实现
                mapInst = new HashMap();
            } else {
                mapInst = (Map) clazz.newInstance();
            }
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
            Set setInst;
            if (clazz == Set.class) {
                //采用HashSet作为默认实现
                setInst = new HashSet();
            } else {
                setInst = (Set) clazz.newInstance();
            }
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

    /**
     * 实例化List对象
     *
     * @param clazz
     * @param gType
     * @param <T>
     * @param <G>
     * @return
     */
    private <T, G> List<G> initList(Class<T> clazz, Class<G> gType) {
        //一定是某个实现类才可能实例化
        //获取List实例
        try {
            //List类型无法直接实例化,这里默认使用ArrayList作为实现
            List listInst;
            if (clazz == List.class) {
                listInst = new ArrayList();
            } else {
                //暂不考虑其他List类型接口或抽象List的实例化
                listInst = (List) clazz.newInstance();
            }
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
