package com.example.simpleparadox.listycity;

public class City implements Comparable<City>{
    private String city;
    private String province;

    City(String city, String province){
        this.city = city;
        this.province = province;
    }

    String getCityName(){
        return this.city;
    }

    String getProvinceName(){
        return this.province;
    }

    @Override
    public int compareTo(City o) {
        int cityCompare = city.compareTo(o.getCityName());
        int provinceCompare = province.compareTo(o.getProvinceName());

        if (cityCompare > 0 || provinceCompare > 0)
            return 1;
        else if (cityCompare == 0 && provinceCompare == 0)
            return 0;
        else
            return -1;
    }
}
