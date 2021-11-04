/*
 * Copyright © All Rights Reserved.
 * Created by  Seed Startup House,LLC. Shmuel Shahar
 */

package com.example.nychighschools.server;
/**
 * Created by SUSHMA-PC on 3/1/2018.
 */

public interface ServerMethodCalls
{
//  Server methods
    void Init(ServerEvents iServerEvents);
    void getNYCSchools();
    void getNYCSchoolSAT(String dbn);
}