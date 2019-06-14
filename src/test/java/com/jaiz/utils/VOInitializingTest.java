package com.jaiz.utils;

import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class VOInitializingTest {

    @Test
    public void test3(){
        VOInitializer init=new VOInitializer();
        TestVO test=init.universalInit(TestVO.class);
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
