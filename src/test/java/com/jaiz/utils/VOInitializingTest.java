package com.jaiz.utils;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class VOInitializingTest {

    @Test
    public void test(){
        System.out.println("test");
        TestVO vo=VOInitializeUtil.initialize(TestVO.class);
        List<String> list=vo.getArrayList();
        System.out.println(list!=null);
        System.out.println(list.size());
        for (String s:list){
            System.out.println(s);
        }
    }

    @Test
    public void test2(){
        TestVO2 vo=VOInitializeUtil.initialize(TestVO2.class);
        System.out.println(vo.getMember().size());
        System.out.println(vo.getMember().get(0));
    }

    @Test
    public void test3(){
        VOInitializer init=new VOInitializer();
        int result=init.universalInit(int.class);
        System.out.println(result);
    }

}
