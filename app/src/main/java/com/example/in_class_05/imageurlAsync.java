package com.example.in_class_05;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class imageurlAsync extends AsyncTask<String,Void,String> {
    MainActivity activity;

    public imageurlAsync(MainActivity activity) {
        this.activity = activity;

    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d("keyword",strings[0]);
        String strurl  = activity.url1;
        strurl = strurl+"?"+"keyword="+strings[0];
        //Log.d("url",strurl);
        Log.d("Stringurl",strurl);
        StringBuilder stringBuilder = new StringBuilder();
        HttpURLConnection connection = null;
        URL url = null;
        //BufferedReader reader = null;
        String result = null;
        try {
             url = new URL(strurl);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                       /* reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String line = "";
                        while ((line = reader.readLine()) != null) {
                            stringBuilder.append(line);
                        }*/
                result = IOUtils.toString(connection.getInputStream(), "UTF-8");
                //result = stringBuilder.toString();
            }

        } catch  (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Handle the exceptions
        finally {
            //Close open connections and reader
            if (connection != null) {
                connection.disconnect();
            }
        }
        //Log.d("res",result);
        return result;

    }

    @Override
    protected void onPostExecute(String s) {
        System.out.println(s);
        String[] s1 = s.split("\n");
        activity.get_image_url_data(s1);


    }
}

