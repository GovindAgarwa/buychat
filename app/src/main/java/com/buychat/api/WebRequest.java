package com.buychat.api;

import android.util.Log;


import com.buychat.extras.Constants;
import com.buychat.extras.Keys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AMAN on 25-10-2015.
 */
public class WebRequest {

        public static JSONObject getData(String url){
            StringBuffer response = new StringBuffer();
            URL obj = null;
            JSONObject jsonObject = null;
            try {
                System.out.println("URL :: " + url);
                obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                int responseCode = con.getResponseCode();
                System.out.println("GET Response Code :: " + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK ) { // success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            con.getInputStream()));
                    String inputLine;
                    response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // print result
                    System.out.println(response.toString());
                    jsonObject = new JSONObject(response.toString());
                } else {
                    jsonObject = new JSONObject();
                    jsonObject.accumulate(Keys.error,"Server not responding");
                    System.out.println("GET request not worked");
                }



            } catch (UnsupportedEncodingException e) {
                Log.d("login issue", e.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (JSONException e){
                e.printStackTrace();
            }
            return jsonObject;

        }
    public static JSONObject getData(String url,String secret,String secret_id){
        StringBuffer response = new StringBuffer();
        URL obj = null;
        JSONObject jsonObject = null;
        try {
            System.out.println("URL :: " + url);
            obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("secret", secret);
            con.setRequestProperty("secret_id", secret_id);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            int responseCode = con.getResponseCode();
            System.out.println("GET Response Code :: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK ) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
                jsonObject = new JSONObject(response.toString());
            } else {
                jsonObject = new JSONObject();
                jsonObject.accumulate(Keys.error,"Server not responding");
                System.out.println("GET request not worked");
            }



        } catch (UnsupportedEncodingException e) {
            Log.d("login issue", e.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return jsonObject;

    }
    public static JSONArray getArrayData(String url){
        StringBuffer response = new StringBuffer();
        URL obj = null;
        JSONArray jsonObject = null;
        try {
            System.out.println("URL :: " + url);
            obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            int responseCode = con.getResponseCode();
            System.out.println("GET Response Code :: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == 201) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
                jsonObject = new JSONArray(response.toString());
            } else {
                System.out.println("GET request not worked");
            }



        } catch (UnsupportedEncodingException e) {
            Log.d("login issue", e.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return jsonObject;

    }

    public static JSONObject postData(String data,String url){
        StringBuffer response = new StringBuffer();
        JSONObject jsonObject = null;
        URL obj = null;
        try {
            System.out.println("Data :: " + data);
            System.out.println("URL :: " + url);
            obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            // For POST only - START
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(data.getBytes("UTF-8"));
            os.flush();
            os.close();
            // For POST only - END
            int responseCode = con.getResponseCode();
            System.out.println("POST Response Code :: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == 201) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());

                jsonObject = new JSONObject(response.toString());

           } else if (responseCode == 404) {
                jsonObject = new JSONObject();
                jsonObject.accumulate(Keys.message, Constants.Failed);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }


        return jsonObject;

    }

    public static  JSONObject  performPostCall(String requestURL,
                                   HashMap<String, String> postDataParams) {
        StringBuffer responses = new StringBuffer();
        URL url;
        JSONObject response = null;
        try {
            System.out.println("POST :: "+ requestURL);

            System.out.println("responseCode"+ getPostDataString(postDataParams));

            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();
            System.out.println("responseCode"+ responseCode);
           // if (responseCode == HttpsURLConnection.HTTP_OK || responseCode == 201) {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    responses.append(inputLine);
                }
                in.close();

                // print result

                response = new JSONObject(responses.toString());
                System.out.println("RESPONSE "+response.toString());
         //   }
           // else {

//                System.out.println("POST request not worked");
  //          }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }



    public static JSONObject postWithoutData(String url){
        StringBuffer response = new StringBuffer();

        JSONObject jsonObject = null;
        URL obj = null;
        try {
            System.out.println("URL :: " + url);
            obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setReadTimeout(15000);
            con.setConnectTimeout(15000);
           // con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            // For POST only - START
           // con.connect();
            // For POST only - END
            int responseCode = con.getResponseCode();
            System.out.println("POST Response Code :: " + responseCode);

            if (responseCode != 0) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());

                jsonObject = new JSONObject(response.toString());

            } else {
                System.out.println("POST request not worked");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }


        return jsonObject;

    }


    public static JSONObject deleteData(String urls){
        URL url = null;
        StringBuffer responses = new StringBuffer();
        JSONObject response = null;
        try {
            url = new URL(urls);
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        }
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            httpURLConnection.setRequestMethod("DELETE");
            int responseCode =  httpURLConnection.getResponseCode();
            System.out.println(""+httpURLConnection.getResponseCode());
            response = new JSONObject();
            response.accumulate("Result", "Success");
        } catch (IOException exception) {

            exception.printStackTrace();
        } catch (Exception exception) {

            exception.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return response;
    }


    public static JSONObject putData(String url){
        StringBuffer response = new StringBuffer();

        JSONObject jsonObject = null;
        URL obj = null;
        try {
            System.out.println("URL :: " + url);
            obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setReadTimeout(15000);
            con.setConnectTimeout(15000);


            con.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            con.setRequestMethod("PUT");

            int responseCode = con.getResponseCode();
            System.out.println("POST Response Code :: " + responseCode);

                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());

                jsonObject = new JSONObject(response.toString());
           //}


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }


        return jsonObject;

    }



    public static JSONObject putDataWithJSON(String url,String data){
        StringBuffer response = new StringBuffer();

        JSONObject jsonObject = null;
        URL obj = null;
        try {
            System.out.println("Data :: " + data);
            System.out.println("URL :: " + url);


            obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setReadTimeout(15000);
            con.setConnectTimeout(15000);
            con.setRequestProperty("Content-Type",
                    "application/json");
            con.setRequestMethod("PUT");

            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(data.getBytes("UTF-8"));
            os.flush();
            os.close();
            // For POST only - END

            int responseCode = con.getResponseCode();
            System.out.println("PUT Response Code :: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK|| responseCode == 201 || responseCode == 422 ) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());

                jsonObject = new JSONObject(response.toString());
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }


        return jsonObject;

    }

    //POST WITH NAMESPACE
    private static  String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

}
