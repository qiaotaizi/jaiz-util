package com.jaiz.utils;

import org.junit.Test;

public class VOInitializingTest {

    @Test
    public void test(){
        System.out.println("test");
        TestVO vo=VOInitializeUtil.initialize(TestVO.class);
        System.out.println(vo.getComboVO().getCombo());
        System.out.println(vo.getComboVO().getDate());
    }

}
