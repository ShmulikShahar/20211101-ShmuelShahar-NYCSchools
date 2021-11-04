package com.example.nychighschools.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NYCSchoolSAT extends ServerObject
{
    public static final Logger log = LoggerFactory.getLogger("NYC High Schools");

    private String dbn = new String();
    private String schoolName = new String();
    private String takers;
    private String satReading;
    private String satMath;
    private String satWriting;

    public String get_DBN()
    {
        return dbn;
    }

    public String get_SchoolName()
    {
        return schoolName;
    }

    public String get_Takers()
    {
        return takers;
    }

    public String get_SATReading()
    {
        return satReading;
    }

    public String get_SATMath()
    {
        return satMath;
    }

    public String get_SATWriting()
    {
        return satWriting;
    }

    public NYCSchoolSAT(String schoolName)
    {
        this.schoolName = schoolName;
    }

    /**
     * Input: Json response , Method Name FromJSON
     * Description: This method is used to parse or Deserialize the got json response. Here we are using Gson library's fromJson method and passing the Object to it
     * Output: returns server Object
     */
    public ServerObject FromJSON(String strJSON) throws Exception {
        log.debug("FromJSON : FromJSON: Start of Method");
        Gson gson = new Gson();

        JsonParser jp = new JsonParser();
        JsonElement jsonElement = jp.parse(strJSON);

        JsonArray jsonArr = jsonElement.getAsJsonArray();

        boolean schoolFound = false;

        for(int i = 0; i < jsonArr.size(); i++)
        {
            JsonElement elm = jsonArr.get(i);
            JsonObject rootObject = elm.getAsJsonObject();
            String schoolName = rootObject.get("school_name").getAsString();

            boolean isEqual = this.schoolName.equalsIgnoreCase(schoolName);
            if(isEqual)
            {
                dbn = rootObject.get("dbn").getAsString();
                takers = rootObject.get("num_of_sat_test_takers").getAsString();
                satReading = rootObject.get("sat_critical_reading_avg_score").getAsString();
                satMath = rootObject.get("sat_math_avg_score").getAsString();
                satWriting = rootObject.get("sat_writing_avg_score").getAsString();
                schoolFound = true;
                break;
            }
        }

        if(!schoolFound)
        {
            throw new Exception("School not found");
        }
        else {
            NYCSchoolSAT NYCSchoolSATObj = this;
            log.debug("NYCSchools :FromJSON: End of Method");
            return NYCSchoolSATObj;
        }
    }
}
