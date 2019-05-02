package com.example.coursework;

public class Country {

    private String name;
    private String code;
    private String ifAdded;

    public Country(String name, String code, String ifAdded) {
        this.name = name;
        this.code = code;
        this.ifAdded = ifAdded;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getIfAdded() {
        return ifAdded;
    }

    public void setIfAdded(String ifAdded) {
        this.ifAdded = ifAdded;
    }
}
