package com.example.chunhan.second_pratice;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;

import java.util.ArrayList;

/**
 * Created by chunhan on 2016/12/16.
 */

public class DatabaseControl {

	private Context context;
	private SQLiteDatabase newsDatabase;
	private String data;

	public void checkOrCreateTable(Context context){

		this.context = context;

		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		data = sDateFormat.format(new java.util.Date());

		newsDatabase = context.openOrCreateDatabase("News",Context.MODE_PRIVATE,null);
		newsDatabase.execSQL("CREATE TABLE IF NOT EXISTS news_" + data + " (title VARCHAT, url VARCHAT)");

	}

	public void insertNews(String title, String url){
		newsDatabase.execSQL("\"INSERT INTO news_" + data + " (title, url) VALUES ('" + title + "', '" + url + "')");

	}

	public ArrayList<String[]> getNewsList(){
		String newsItem[] = new String[2];
		Cursor mCursor = newsDatabase.rawQuery("SELECT * FROM news_" + data, null);
		

		return null;
	}

}
