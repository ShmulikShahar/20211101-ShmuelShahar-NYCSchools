/*
 * Copyright © All Rights Reserved.
 * Created by  Seed Startup House,LLC. Shmuel Shahar
 */

package com.example.nychighschools;

/**
 * Created by SUSHMA-PC on 3/2/2018.
 */

public class JSONCleaner
{
    static public String clean(String response)
    {
        //removing the backslashes & trimming the first & last quotes in the response
        response = response.replace("\\", "").replaceAll("^\"|\"$", "");
        return response;
    }
}