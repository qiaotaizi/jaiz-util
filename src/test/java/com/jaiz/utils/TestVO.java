package com.jaiz.utils;

import java.util.ArrayList;
import java.util.List;

public class TestVO extends SuperTestVO{

    private String userName;

    private Integer age;

    private int count;

    private String[] arr;

    private long[] arr2;

    private List<String> list;

    private ArrayList<String> arrayList;

    private TestComboVO comboVO;

    public TestComboVO getComboVO() {
        return comboVO;
    }

    public void setComboVO(TestComboVO comboVO) {
        this.comboVO = comboVO;
    }

    public ArrayList<String> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public long[] getArr2() {
        return arr2;
    }

    public void setArr2(long[] arr2) {
        this.arr2 = arr2;
    }

    public String[] getArr() {
        return arr;
    }

    public void setArr(String[] arr) {
        this.arr = arr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
