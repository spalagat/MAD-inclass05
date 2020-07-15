package com.example.in_class_05;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class keywordAsync extends AsyncTask<String,Void,String> {

    MainActivity activity;
    public keywordAsync(MainActivity activity) {

        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... strings) {
        StringBuilder stringBuilder = new StringBuilder();
        HttpURLConnection connection = null;
        //BufferedReader reader = null;
        String result = null;
        URL url = null;
        try {
            url = new URL( strings[0]);
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
        super.onPostExecute(s);
        Log.d("res1",s);
        String[] s1 = s.split(";");
        //Log.d("length",String.valueOf(s1.length));
        //for (String s2:s1)
            //Log.d("keywords",s2);

        activity.handlekeywords(s1);

    }
}

