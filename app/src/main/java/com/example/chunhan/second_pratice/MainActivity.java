package com.example.chunhan.second_pratice;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private DatabaseControl mDatabaseControll;
    private int newsnums;
    private ListView newsList;
    public ArrayList<Pair<String,String>> categoryNewsValuesPairList;
	static final String TAG = "SECOND_PRARICE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseControll = new DatabaseControl();
        mDatabaseControll
                .checkOrCreateTable(this);

        newsList = (ListView)findViewById(R.id.news_list);

        DownloadTask task = new DownloadTask();

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
				try {
					String title = newsJSONObject.getString("title");
					String url = newsJSONObject.getString("url");
					mDatabaseControll.insertOrUpdateNews(title, url, i);
				}catch(Exception e){
					Log.i(TAG,"title or url is null");
				}
			}

			categoryNewsValuesPairList = mDatabaseControll.getNewsList();
			Log.i("testing","categoryNewsValuesPairList1=" + categoryNewsValuesPairList.get(1).first) ;
			Log.i("testing","categoryNewsValuesPairList1=" + categoryNewsValuesPairList.get(1).second) ;
			NewsAdapter mNewsAdapter = new NewsAdapter(newsnums, categoryNewsValuesPairList);
			newsList.setOnItemClickListener(this);
			newsList.setAdapter(mNewsAdapter);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
		catch (JSONException e) {
			e.printStackTrace();
		}



    }
	private class NewsAdapter extends BaseAdapter{

		private final LayoutInflater inflater;
		int newsnums;
		ArrayList<Pair<String,String>> categoryNewsValuesPairList;

		private NewsAdapter(int newsnums, ArrayList<Pair<String,String>> categoryNewsValuesPairList) {
			newsnums = this.newsnums;
			this.inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.categoryNewsValuesPairList = categoryNewsValuesPairList;

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
				holder.titleView = (TextView)view.findViewById(R.id.texttitle);
				//holder.urlView = (TextView)view.findViewById(R.id.texturl);
				view.setTag(holder);
			}else{
				holder = (CustomViewHolder)view.getTag();
			}
			holder.titleView.setText(categoryNewsValuesPairList.get(i).first);
			//holder.urlView.setText(categoryNewsValuesPairList.get(i).second);
			return view;
		}

	}

	private static class CustomViewHolder {
		TextView titleView;
		//TextView urlView;
	}

	@Override
	public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
		Intent urlIntent = new Intent();
		urlIntent.setAction(Intent.ACTION_VIEW);
		urlIntent.setData(Uri.parse(categoryNewsValuesPairList.get(position).second));
		startActivity(urlIntent);
	}

}


