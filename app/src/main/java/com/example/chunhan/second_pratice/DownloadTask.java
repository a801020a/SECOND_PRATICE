package com.example.chunhan.second_pratice;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by chunhan on 2016/12/16.
 */

public class DownloadTask extends AsyncTask<String, Void, String>{

	@Override
	protected String doInBackground(String... urls) {

		String result = "";
		URL url;
		HttpURLConnection urlConnection = null;
		try {
			url = new URL(urls[0]);
			urlConnection = (HttpURLConnection)url.openConnection();
			InputStream in = urlConnection.getInputStream();
			InputStreamReader reader = new InputStreamReader(in);

			int data = reader.read();
			while (data != -1) {
				char current = (char) data;
				result += current;
				data = reader.read();
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "False";
		}

	}
}
