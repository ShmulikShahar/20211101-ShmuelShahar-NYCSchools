/*
 * Copyright © All Rights Reserved.
 * Created by  Seed Startup House,LLC. Shmuel Shahar
 */

package com.example.nychighschools.server;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

/**
 * Created by SUSHMA-PC on 3/2/2018.
 */
public class VolleySingleton
{
    private static VolleySingleton sInstance = new VolleySingleton();

    private final RequestQueue m_RequestQueue;


    private VolleySingleton()
    {
        m_RequestQueue = Volley.newRequestQueue(ServerCtrl.m_context, null);
    }

    public static VolleySingleton getInstance()
    {
        if(sInstance == null)
        {
            sInstance = new VolleySingleton();
        }

        return sInstance;
    }

    public RequestQueue getRequestQueue()
    {
        return m_RequestQueue;
    }
}
