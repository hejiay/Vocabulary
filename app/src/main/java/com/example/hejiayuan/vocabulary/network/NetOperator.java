package com.example.hejiayuan.vocabulary.network;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetOperator {

    public final static String iCiBaURL = "http://dict-co.iciba.com/api/dictionary.php?w=";
    public final static String API = "&key=708691705809AD117E43B78C66CB07BE";

    public static InputStream getInputStreamByUrl(String urlStr) {
        InputStream inputStream = null;
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(10000);
            inputStream = connection.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }
}
