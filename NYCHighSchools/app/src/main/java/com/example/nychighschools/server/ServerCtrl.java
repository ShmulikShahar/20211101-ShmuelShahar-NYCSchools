/*
 * Copyright © All Rights Reserved.
 * Created by  Seed Startup House,LLC. Shmuel Shahar
 */

package com.example.nychighschools.server;

import static com.example.nychighschools.JSONCleaner.clean;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.nychighschools.R;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;


public class ServerCtrl implements ServerMethodCalls
{
    public static final Logger log = LoggerFactory.getLogger("Eden Sender App");
    public static Context m_context = null;
    private static ServerEvents m_serverEvents = null;
    private static NYCSchoolsServerEvents m_NYCSchoolsServerEvents = null;

    private static final String url = "https://data.cityofnewyork.us/resource/";
    public int MY_SOCKET_TIMEOUT_MS = 20000;
    public int mStatusCode = 0;

    /**
     * Input : ServerEvents object
     * Description : This method is used to set the ServerEvents
     */
    @Override
    public void Init(ServerEvents iServerEvents)
    {
        log.debug("Server : set ServerEvents : Start of Function");
        m_serverEvents = iServerEvents;
        log.debug("Server : set ServerEvents : End of Function");
    }

    public void SetContext(Context context)
    {
        m_context = context;
    }

    /**
     * Input : NYCSchoolsServerEvents object
     * Description : This method is used to set the NYCSchoolsServerEvents
     */
    public void setNYCSchoolsServerEvents(NYCSchoolsServerEvents NYCSchoolsServerEventsObj)
    {
        log.debug("ServerCtrl : setM_transfersScreenServerEvents : Start of Function");
        m_NYCSchoolsServerEvents = NYCSchoolsServerEventsObj;
        log.debug("ServerCtrl : setM_transfersScreenServerEvents : End of Function");
    }

    @Override
    public void getNYCSchools() {
        log.debug("ServerCtrl : getNYCSchools : Start of Function");
        createRequestToServerWithVolley(ServerMethodNames.GetNYCSchools, null);
        log.debug("ServerCtrl : getNYCSchools : End of Function");
    }

    @Override
    public void getNYCSchoolSAT(String schoolName) {
        log.debug("ServerCtrl : getNYCSchoolSAT : Start of Function");
        createRequestToServerWithVolley(ServerMethodNames.GetNYCSchoolSAT, schoolName);
        log.debug("ServerCtrl : getNYCSchoolSAT : End of Function");
    }

    /**
     * Input:ServerObject & ServerMethodNames
     * Description:This method is used to create the request to server and call appropriate fire response to it.
     */

    private String getMethodName(ServerMethodNames serverMethodName)
    {
        if(serverMethodName.name() == "GetNYCSchools")
        {
            //return "s3k6-pzi2.json";Inconsistent names between the two JSON end point
            return "f9bf-2cp4.json";
        }
        else if(serverMethodName.name() == "GetNYCSchoolSAT")
        {
            return "f9bf-2cp4.json";
        }

        return "";//TBD handle errors.
    }

    private void createRequestToServerWithVolley(final ServerMethodNames serverMethodName, String param)
    {
        log.debug("ServerCtrl : createRequestToServer : Start of Function");

        RequestQueue mRequestQueue;
        mRequestQueue = VolleySingleton.getInstance().getRequestQueue();
        String methodName = getMethodName(serverMethodName);
        String ServerUrl = getUrl(methodName);
        log.info("ServerCtrl : URL:"+ServerUrl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        log.info("ServerCtrl : createRequestToServerWithVolley : Server Response" +(mStatusCode));
                        String retCodeJSON = clean(response);
                        log.info("ServerCtrl : server response"+retCodeJSON);
                        fire_Result(serverMethodName, retCodeJSON, param);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        log.error("ServerCtrl :  createRequestToServerWithVolley : onErrorResponse exception "+error);
                        if (mStatusCode == 0) {
                            m_serverEvents.fire_ErrorResponse(error, mStatusCode);
                        }
                        try {
                            mStatusCode = error.networkResponse.statusCode;
                        } catch (Exception e) {
                            m_serverEvents.fire_ErrorResponse(error, mStatusCode);
                        }

                        log.error("ServerCtrl :  createRequestToServerWithVolley : mStatusCode error"+ mStatusCode);
                        m_serverEvents.fire_ErrorResponse(error, mStatusCode);
                    }
                }) {

            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("app_token", "lmujGslPt8g5Ub8CWvIyRo36R");
                return params;
            };

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response != null) {
                    mStatusCode = response.statusCode;
                    log.info("ServerCtrl : createRequestToServerWithVolley : Server Response" +(mStatusCode));
                } else {
                    log.info("ServerCtrl : createRequestToServerWithVolley : Status Code Null" +(mStatusCode));
                }
                return super.parseNetworkResponse(response);
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
        log.debug("ServerCtrl : createRequestToServer : End of Function");
    }

    /**
     * Input:methodName & jsonRequest
     * Description:This method is used to get the URL
     * Output:returns URL
     */
    private String getUrl(String methodName)
    {
        log.debug("ServerCtrl : getUrl : Start of Function");
        String Url;
        Url=url+ methodName;
        log.debug("ServerCtrl : getUrl : End of Function");
        return Url;
    }

    /**
     * Input:serverMethodNames & retCodeJSON
     * Description:This method is used to call appropriate fire response on getting the response from server
     */
    private void fire_Result(ServerMethodNames serverMethodName, String retCodeJSON, String param)
    {
        log.debug(" : fire_Result : Start of Function");
        log.info("ServerCtrl :  fire_Result : serverMethodNames "+serverMethodName);
        try {
            switch (serverMethodName) {
                case GetNYCSchools:
                    log.debug("ServerCtrl : GetNYCSchools : Start of case");
                    NYCSchools NYCSchoolsObj = new NYCSchools();
                    NYCSchoolsObj = (NYCSchools) NYCSchoolsObj.FromJSON(retCodeJSON);

                    if (m_NYCSchoolsServerEvents != null) {
                        m_NYCSchoolsServerEvents.fire_GetNYCSchoolsResponse(NYCSchoolsObj);
                    } else {
                        log.info("ServerCtrl : fire_Result : Null pointer Exception : " + serverMethodName.name());
                        if (m_serverEvents != null) {
                            m_serverEvents.fire_ExceptionResponse();
                        }
                    }
                    log.debug("ServerCtrl : GetUserID : End of case");
                    break;

                case GetNYCSchoolSAT:
                    log.debug("ServerCtrl : GetNYCSchoolSAT : Start of case");
                    NYCSchoolSAT NYCSchoolSATObj = new NYCSchoolSAT(param);
                    NYCSchoolSATObj = (NYCSchoolSAT) NYCSchoolSATObj.FromJSON(retCodeJSON);

                    if (m_NYCSchoolsServerEvents != null) {
                        m_NYCSchoolsServerEvents.fire_GetNYCSchoolSATResponse(NYCSchoolSATObj);
                    } else {
                        log.info("ServerCtrl : fire_Result : Null pointer Exception : " + serverMethodName.name());
                        if (m_serverEvents != null) {
                            m_serverEvents.fire_ExceptionResponse();
                        }
                    }
                    log.debug("ServerCtrl : GetNYCSchoolSAT : End of case");
                    break;
            }

        }
        catch (Exception e)
        {
            log.error("GetNYCSchoolSAT : fire_Result :  Exception in : "+serverMethodName.name() +e.getMessage());
            if (m_serverEvents != null) {
                m_serverEvents.fire_ExceptionResponse();
            }
        }

        log.debug("GetNYCSchoolSAT : fire_Result : End of Function");
    }
}