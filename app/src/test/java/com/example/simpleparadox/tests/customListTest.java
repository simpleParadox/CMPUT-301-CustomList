package com.example.simpleparadox.tests;

import static org.junit.Assert.assertEquals;

import com.example.simpleparadox.listycity.City;
import com.example.simpleparadox.listycity.CustomList;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class customListTest {

    private CustomList customList;

    @Before
    public void createList() {
        customList=  new CustomList(null, new ArrayList<>());
    }

    @Test
    public void addCityTest() {
        int listSize = customList.getCount();
        customList.addCity(new City("Halifax" , "NS"));

        assertEquals(customList.getCount(), listSize + 1);
    }
    //yeerr

}

