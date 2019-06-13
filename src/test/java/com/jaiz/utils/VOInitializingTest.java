package com.jaiz.utils;

import junit.framework.TestCase;

public class VOInitializingTest extends TestCase {

    public void test(){
        System.out.println("test");
        TestVO vo=VOInitializeUtil.initialize(TestVO.class);
        System.out.println(vo.getUserName());
        System.out.println(vo.getFirstName());
        System.out.println(vo.getAge());
        System.out.println(vo.getCount());
        System.out.println(vo.getOk2());
        System.out.println(vo.isOk());
        System.out.println(vo.getA());
        System.out.println(vo.getB());
    }

}
