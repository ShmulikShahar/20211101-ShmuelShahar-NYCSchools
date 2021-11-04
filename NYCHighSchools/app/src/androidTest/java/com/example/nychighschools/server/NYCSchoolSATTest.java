package com.example.nychighschools.server;

import junit.framework.TestCase;

public class NYCSchoolSATTest extends TestCase {

    public void testFromJSON() {
        NYCSchoolSAT nycSchoolSATObj = new NYCSchoolSAT("EAST SIDE COMMUNITY SCHOOL");

        try {
            nycSchoolSATObj.FromJSON("[{\"dbn\":\"01M292\",\"school_name\":\"HENRY STREET SCHOOL FOR INTERNATIONAL STUDIES\",\"num_of_sat_test_takers\":\"29\",\"sat_critical_reading_avg_score\":\"355\",\"sat_math_avg_score\":\"404\",\"sat_writing_avg_score\":\"363\"}\n" +
                    ",{\"dbn\":\"01M448\",\"school_name\":\"UNIVERSITY NEIGHBORHOOD HIGH SCHOOL\",\"num_of_sat_test_takers\":\"91\",\"sat_critical_reading_avg_score\":\"383\",\"sat_math_avg_score\":\"423\",\"sat_writing_avg_score\":\"366\"}\n" +
                    ",{\"dbn\":\"01M450\",\"school_name\":\"EAST SIDE COMMUNITY SCHOOL\",\"num_of_sat_test_takers\":\"70\",\"sat_critical_reading_avg_score\":\"377\",\"sat_math_avg_score\":\"402\",\"sat_writing_avg_score\":\"370\"}]");
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(nycSchoolSATObj.get_SchoolName(), "EAST SIDE COMMUNITY SCHOOL");
        assertEquals(nycSchoolSATObj.get_SATMath(), "402");
        assertEquals(nycSchoolSATObj.get_SATReading(), "377");
        assertEquals(nycSchoolSATObj.get_Takers(), "70");
        assertEquals(nycSchoolSATObj.get_SATWriting(), "370");
    }
}