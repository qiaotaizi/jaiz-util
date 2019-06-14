package com.jaiz.utils;

import org.junit.Test;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VOInitializingTest {

    @Test
    public void test3(){
        VOInitializer init=new VOInitializer();
        //基本数据类型
//        int result=init.universalInit(int.class);
//        System.out.println(result);

        List<String> list=new ArrayList<String>();

        List<String> res=init.universalInit(list.getClass(),String.class,null);
        System.out.println(res==null);
        System.out.println(res.get(0));
    }

    @Test
    public void genericTest(){

        List<String> list=new ArrayList<>();
        ParameterizedType pType=(ParameterizedType)list.getClass().getGenericSuperclass();
        System.out.println(pType.getTypeName());
        System.out.println(pType.getRawType().getTypeName());
        System.out.println(pType.getOwnerType().getTypeName());
        Type[] gTypes=pType.getActualTypeArguments();

        for(Type t:gTypes){
            System.out.println(t.getTypeName());
        }
    }

}
