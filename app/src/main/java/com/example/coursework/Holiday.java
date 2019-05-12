package com.example.coursework;

public class Holiday {

    private String date;
    private String localName;
    private String name;
    private String countryCode;
    private String favourite;

    Holiday(String date, String localName, String name, String countryCode, String favourite) {
        this.date = date;
        this.localName = localName;
        this.name = name;
        this.countryCode = countryCode;
        this.favourite = favourite;
    }

    /*@Override
    public String toString() {
        return "This holiday is celebrated on " +
                "date '" + date + '\'' +
                ", it's local name is '" + localName + '\'' +
                ", it's english name is '" + name + '\'' +
                ", it's holiday of '" + countryCode + '\'';
    }*/

    @Override
    public String toString() {
        return localName +
                ". It is also known as " + name +
                ". It is celebrated in " + countryCode + ".";
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

    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }
}
