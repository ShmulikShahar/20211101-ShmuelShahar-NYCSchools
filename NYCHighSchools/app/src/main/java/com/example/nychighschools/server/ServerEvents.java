/*
 * Copyright © All Rights Reserved.
 * Created by  Seed Startup House,LLC. Shmuel Shahar
 */

package com.example.nychighschools.server;

import com.android.volley.VolleyError;

/**
 * Created by SUSHMA on 3/1/2018.
 */
public interface ServerEvents
{
    //Event Listeners
    void fire_ExceptionResponse();

    void fire_ErrorResponse(VolleyError error, int StatusCode);

    void fire_HttpErrorResponse(int StatusCode);

}
