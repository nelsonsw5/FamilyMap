package com.example.familymapclient.net;

import android.os.FileUtils;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


import Encoder.ObjectEncoder;
import Requests.LoadRequest;
import Requests.LoginRequest;
import Requests.RegisterRequest;
import Results.AllEventsResult;
import Results.AllPersonsResult;
import Results.ClearResult;
import Results.LoadResult;
import Results.LoginResult;
import Results.RegisterResult;

public class ServerProxy {

    public static LoginResult login(String serverHost, String serverPort, LoginRequest lr) {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.addRequestProperty("Accept", "application/json");
            http.connect();
            Gson gson = new Gson();
            String reqData = gson.toJson(lr);
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();
            InputStream respBody;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                respBody = http.getInputStream();
            } else {
                respBody = http.getErrorStream();
            }
            String respData = readString(respBody);
            return ObjectEncoder.deserialize(respData, LoginResult.class);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static RegisterResult register(String serverHost, String serverPort, RegisterRequest rr) {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.addRequestProperty("Accept", "application/json");
            http.addRequestProperty("Connection","close");
            http.connect();
            Gson gson = new Gson();
            String reqData = gson.toJson(rr);
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();
            InputStream respBody;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                respBody = http.getInputStream();
            } else {
                respBody = http.getErrorStream();
            }
            String respData = readString(respBody);
            return ObjectEncoder.deserialize(respData, RegisterResult.class);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static AllPersonsResult getAllPersons (String serverHost, String serverPort, String authToken) {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Accept", "application/json");
            http.addRequestProperty("Authorization", authToken);
            http.connect();
            InputStream respBody;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                respBody = http.getInputStream();
            } else {
                respBody = http.getErrorStream();
            }
            String respData = readString(respBody);
            return ObjectEncoder.deserialize(respData, AllPersonsResult.class);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static AllEventsResult getAllEvents (String serverHost, String serverPort, String authToken) {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Accept", "application/json");
            http.addRequestProperty("Authorization", authToken);
            http.connect();
            InputStream respBody;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                respBody = http.getInputStream();
            } else {
                respBody = http.getErrorStream();
            }
            String respData = readString(respBody);
            return ObjectEncoder.deserialize(respData, AllEventsResult.class);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ClearResult clear(String serverHost, String serverPort)  {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/clear");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(false);
            http.addRequestProperty("Accept", "application/json");
            http.connect();
            InputStream respBody;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                respBody = http.getInputStream();
            } else {
                respBody = http.getErrorStream();
            }
            String respData = readString(respBody);
            return ObjectEncoder.deserialize(respData, ClearResult.class);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static LoadResult load(String serverHost, String serverPort)  {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/load");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.addRequestProperty("Accept", "application/json");
            http.addRequestProperty("Connection","close");
            http.connect();
            Gson gson = new Gson();
            String reqData = new String(Files.readAllBytes(Paths.get("/Users/higginsjg/AndroidStudioProjects/" +
                    "FamilyMapClient/app/libs/LoadData.json")));
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();
            InputStream respBody;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                respBody = http.getInputStream();
            } else {
                respBody = http.getErrorStream();
            }
            String respData = readString(respBody);
            return ObjectEncoder.deserialize(respData, LoadResult.class);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

}
