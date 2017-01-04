package com.example.chunhan.second_pratice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by chunhan on 2016/12/16.
 */

public class DatabaseControl {

	private Context context;
	private SQLiteDatabase newsDatabase;
	private String data;
	private boolean isFirstTime = false;

	public void checkOrCreateTable(Context context){

		this.context = context;

		newsDatabase = context.openOrCreateDatabase("News",Context.MODE_PRIVATE,null);
		newsDatabase.execSQL("CREATE TABLE IF NOT EXISTS news (_id INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAT, url VARCHAT)");

	}

	/*public void insertNews(String title, String url){
		if(url != null)
		newsDatabase.execSQL("INSERT INTO news (title, url) VALUES ('" + title + "', '" + url + "')");
		else{
			newsDatabase.execSQL("INSERT INTO news (title, url) VALUES ('" + title + " No url link)");
		}

	}

	public void updateNews(String title, String url, int num){
		ContentValues cv = new ContentValues();
		if(url != null) {
			cv.put("title", title);
			cv.put("url", url);
			String where ="_id=" + Integer.toString(num);
			newsDatabase.update("news",cv,where,null);
		}

	}*/

	public void insertOrUpdateNews(String title,String url,int num) {
		ContentValues cv = new ContentValues();
		cv.put("_id", num);
		String where = "_id=" + Integer.toString(num);
		if (url != null) {
			cv.put("title", title);
			cv.put("url", url);
			int id = (int) newsDatabase.insertWithOnConflict("news", null, cv, SQLiteDatabase.CONFLICT_IGNORE);
			if (id == -1) {
				newsDatabase.update("news", cv, where, null);
			}
		} else {
			cv.put("title", title + ". No url link!");
			cv.put("url", " ");
			int id = (int) newsDatabase.insertWithOnConflict("news", null, cv, SQLiteDatabase.CONFLICT_IGNORE);//cv不同先別合起來
			if (id == -1) {
				newsDatabase.update("news", cv, where, null);
			}
		}
	}

		public ArrayList<Pair<String, String>> getNewsList(){
			ArrayList<Pair<String, String>> newsList = new ArrayList<Pair<String, String>>();
			Cursor mCursor = newsDatabase.rawQuery("SELECT * FROM news", null);
			int titleIndex = mCursor.getColumnIndex("title");
			int urlIndex = mCursor.getColumnIndex("url");
			mCursor.moveToFirst();
			for (int i = 0; i < mCursor.getCount(); i++) {
				newsList.add(new Pair<String, String>(mCursor.getString(titleIndex), mCursor.getString(urlIndex)));
				mCursor.moveToNext();
			}

			return newsList;
		}
	}


