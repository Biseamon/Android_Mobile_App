package com.example.authorisationfirebase;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadUrl {

    /**
     * DownloadUrl.class is needed for downloading data by using
     * internet HTTP request.
     *
     */

    public String readUrl(String myUrl) throws IOException
    {
        String data = "";
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(myUrl);                                //initialise the URL class.
            urlConnection=(HttpURLConnection) url.openConnection(); //opens a new connection
            urlConnection.connect();                               // the urls to connected status

            inputStream = urlConnection.getInputStream();        //reads the input and converts it to a string
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();

            String line = "";
            while((line = br.readLine()) != null)   //while loop for reading the data from the internet and convert it to a string
            {
                sb.append(line);
            }

            data = sb.toString();
            br.close();    //after completion the reader needs to be closed.


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            inputStream.close();
            urlConnection.disconnect();
        }
        Log.d("DownloadURL","Returning data= "+data);

        return data;  //retruns the requested data in form of a String
    }

}
