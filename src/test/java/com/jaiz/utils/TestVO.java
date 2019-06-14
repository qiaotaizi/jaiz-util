package com.jaiz.utils;

import java.util.List;

public class TestVO extends SuperTestVO{

    private String userName;

    private Integer age;

    private int count;

    private TestComboVO comboVO;

    private List<String> listMember;

    public List<String> getListMember() {
        return listMember;
    }

    public void setListMember(List<String> listMember) {
        this.listMember = listMember;
    }

    public TestComboVO getComboVO() {
        return comboVO;
    }

    public void setComboVO(TestComboVO comboVO) {
        this.comboVO = comboVO;
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
