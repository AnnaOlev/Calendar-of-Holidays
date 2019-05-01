package com.example.coursework;

public class Holiday {

    private String date;
    private String localName;
    private String name;
    private String countryCode;

    Holiday(String date, String localName, String name, String countryCode) {
        this.date = date;
        this.localName = localName;
        this.name = name;
        this.countryCode = countryCode;
    }

    @Override
    public String toString() {
        return "This holiday is celebrated on " +
                "date '" + date + '\'' +
                ", it's local name is '" + localName + '\'' +
                ", it's english name is '" + name + '\'' +
                ", it's holiday of '" + countryCode + '\'';
    }

    String getDate() {
        return date;
    }

    public String getLocalName() {
        return localName;
    }

    public String getName() {
        return name;
    }

    public String getCountryCode() {
        return countryCode;
    }
}
