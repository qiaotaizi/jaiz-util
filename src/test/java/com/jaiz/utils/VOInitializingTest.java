package com.jaiz.utils;

import junit.framework.TestCase;

public class VOInitializingTest extends TestCase {

    public void test(){
        System.out.println("test");
        TestVO vo=VOInitializeUtil.initialize(TestVO.class);
        System.out.println(vo.getComboVO().getCombo());
    }

}
