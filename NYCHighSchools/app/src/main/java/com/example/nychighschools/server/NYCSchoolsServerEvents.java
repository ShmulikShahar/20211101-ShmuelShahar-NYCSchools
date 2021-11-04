/*
 * Copyright © All Rights Reserved.
 * Created by  Seed Startup House,LLC. Shmuel Shahar
 */

package com.example.nychighschools.server;

public interface NYCSchoolsServerEvents extends ServerEvents
{
    void fire_GetNYCSchoolsResponse(NYCSchools NYCSchoolsObj);
    void fire_GetNYCSchoolSATResponse(NYCSchoolSAT NYCSchoolSATObj);
}
