package com.example.coursework;

public class Country {

    private String name;
    private String code;
    private String ifAdded;
    private String id;

    public Country(String name, String code, String ifAdded, String id) {
        this.name = name;
        this.code = code;
        this.ifAdded = ifAdded;
        this.id = id;
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

    public String getId() {
        return id;
    }
}
