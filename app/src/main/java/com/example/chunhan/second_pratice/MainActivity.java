package com.example.chunhan.second_pratice;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private DatabaseControl mDatabaseControll;
    private int newsnums;
    private ListView newsList;
    private ArrayList<Pair<String,String>> categoryNewsValuesPairList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseControll = new DatabaseControl();
        mDatabaseControll
                .checkOrCreateTable(this);

        newsList = (ListView)findViewById(R.id.news_list);

        DownloadTask task = new DownloadTask();
        //DownloadTask task2 = new DownloadTask();

        try {
            String newsIDs = task.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty").get();
            Log.i("test get ids",newsIDs);
            String newsID[] = newsIDs.split("\\[ |, | \\]");
            if(newsID.length > 20){
                newsnums = 20;
            }else{
                newsnums = newsID.length;
            }
			for(int i = 1 ; i < newsnums ; i++){
				DownloadTask newsTask = new DownloadTask();
				JSONObject newsJSONObject = new JSONObject(newsTask.execute("https://hacker-news.firebaseio.com/v0/item/" + newsID[i] + ".json").get());
				String title = newsJSONObject.getString("title");
				String url = newsJSONObject.getString("url");
				mDatabaseControll.insertOrUpdateNews(title,url,i);
			}
				ArrayList<Pair<String,String>> newsList = mDatabaseControll.getNewsList();
			for(int i=0;i<newsList.size();i++){
				String title = newsList.get(i).first;
				String url = newsList.get(i).second;
				Log.i("testTitle",title);
				Log.i("testURL",url);
			}
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
		catch (JSONException e) {
			e.printStackTrace();
		}



    }
	private class newsAdapter extends BaseAdapter{

		private final LayoutInflater inflater;
		int newsnums;

		private newsAdapter(int newsnums) {
			newsnums = this.newsnums;
			this.inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return categoryNewsValuesPairList.size();
		}

		@Override
		public Object getItem(int i) {
			return categoryNewsValuesPairList.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			CustomViewHolder holder;
			if(view == null){
				view = inflater.inflate(R.layout.news_categorieslist,null);
				holder = new CustomViewHolder();
				holder.titleView = (TextView)findViewById(R.id.texttitle);
				holder.urlView = (TextView)findViewById(R.id.texturl);
				view.setTag(holder);
			}else{
				holder = (CustomViewHolder)view.getTag();
			}

			return null;
		}
	}

	private static class CustomViewHolder {
		TextView titleView;
		TextView urlView;
	}

}


