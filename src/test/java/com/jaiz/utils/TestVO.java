package com.jaiz.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestVO extends SuperTestVO{

    private String userName;

    private Integer age;

    private int count;

    private TestComboVO comboVO;

    private List<String> listMember;

    private Set<String> setMember;

    public Set<String> getSetMember() {
        return setMember;
    }

    public void setSetMember(Set<String> setMember) {
        this.setMember = setMember;
    }

    private Map<String,String> mapMember;

    public Map<String, String> getMapMember() {
        return mapMember;
    }

    public void setMapMember(Map<String, String> mapMember) {
        this.mapMember = mapMember;
    }

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
