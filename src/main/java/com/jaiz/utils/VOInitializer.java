package com.jaiz.utils;

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
     * @param err
     */
    private void err(String err){
        System.err.println(err);
    }

    /**
     * 实例化一切
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T universalInit(Class<T> clazz){
        if(clazz.isPrimitive()){
            //处理基本数据类型
            return initPrimitive(clazz);
        }

        err(clazz.getName()+"实例化失败");
        return null;
    }

    /**
     * 处理基本数据类型
     * @param clazz
     * @param <T>
     * @return
     */
    private <T> T initPrimitive(Class<T> clazz){
        if(clazz==byte.class){
            //整型
            return (T)Byte.valueOf((byte)1);
        }

        if(clazz==short.class){
            return (T)Short.valueOf((short)1);
        }

        if(clazz==int.class){
            return (T)Integer.valueOf(1);
        }

        if(clazz==long.class){
            return (T)Long.valueOf(1l);
        }

        if(clazz==float.class){
            //浮点
            return (T)Float.valueOf(1.23f);
        }
        if(clazz==double.class){
            return (T)Double.valueOf(1.23);
        }

        if(clazz==boolean.class){
            //布尔
            return (T)Boolean.valueOf(true);
        }

        if(clazz==char.class){
            //字符
            return (T)Character.valueOf('a');
        }
        //实际上不会出现
        err("未处理的基本类型:"+clazz.getName());
        return null;
    }




}
