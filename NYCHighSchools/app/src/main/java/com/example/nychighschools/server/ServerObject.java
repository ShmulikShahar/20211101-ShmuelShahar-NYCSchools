/*
 * Copyright © All Rights Reserved.
 * Created by  Seed Startup House,LLC. Shmuel Shahar
 */

package com.example.nychighschools.server;

import com.google.gson.Gson;

/**
 * This is the main Server Object class used to convert the values to be sent to server as JSON. Here we are using Gson library.
 */
public class ServerObject
{
    /**
     * Description: This method is used to convert or Deserialize the values to send a JSON request. Here we are using Gson library's toJson method.
     * Output: returns server Object
     */
    public String ToJSON()
    {
        Gson gson = new Gson();
        String jsonData = gson.toJson(this).replace("\\\"","");
        String json = jsonData.replaceAll(" ","%20");
        int maxLogSize = 1000;
        for(int i = 0; i <= json.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = Math.min(end, json.length());
        }
        return json;
    }

    /**
     * Input: Json response , Method Name
     * Description: This method is used to set the Server Object
     * Output: returns server Object
     */
    public ServerObject FromJSON(String strJSON, String MethodName)
    {
        return ServerObject.this;
    }
}