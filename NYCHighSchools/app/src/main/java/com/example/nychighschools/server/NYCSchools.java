/*
 * Copyright © All Rights Reserved.
 * Created by  Seed Startup House,LLC. Shmuel Shahar
 */

package com.example.nychighschools.server;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class NYCSchools extends ServerObject
{
    public static final Logger log = LoggerFactory.getLogger("NYC High Schools");
    private ArrayList<String> schools = new ArrayList<>();

    public ArrayList<String> Schools()
    {
        return schools;
    }

    /**
     * Input: Json response , Method Name FromJSON
     * Description: This method is used to parse or Deserialize the got json response. Here we are using Gson library's fromJson method and passing the Object to it
     * Output: returns server Object
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ServerObject FromJSON(String strJSON)
    {
        log.debug("FromJSON : FromJSON: Start of Method");
        Gson gson = new Gson();

        JsonParser jp = new JsonParser();
        JsonElement jsonElement = jp.parse(strJSON);

        JsonArray jsonArr = jsonElement.getAsJsonArray();

        for(int i = 0; i < jsonArr.size(); i++)
        {
            JsonElement elm = jsonArr.get(i);
            JsonObject rootObject = elm.getAsJsonObject();
            //String dbn = rootObject.get("dbn").getAsString();
            String schoolName = rootObject.get("school_name").getAsString();
            schools.add(schoolName);
        }

        Comparator<String> ALPHABETICAL_ORDER = new Comparator<String>() {
            public int compare(String str1, String str2) {
                int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
                if (res == 0) {
                    res = str1.compareTo(str2);
                }
                return res;
            }
        };

        schools.sort(ALPHABETICAL_ORDER);

        NYCSchools NYCSchoolsObj = this;
        log.debug("NYCSchools :FromJSON: End of Method");
        return NYCSchoolsObj;
    }
}