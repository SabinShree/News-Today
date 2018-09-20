package activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import SingletonClass.DownloadStatus;
import SingletonClass.GetNewsJsonData;
import datacontainer.NewsItems;
import fragments.AllChannel;
import nepalinewsfeeder.sabinkharel.com.fragment.R;

public class ChannelWiseNews extends AppCompatActivity implements GetNewsJsonData.OnDataAvailable {
    private static final String TAG = "ChannelWiseNews";
    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private NewsApiRecycleViewAdapter mNewsApiRecycleViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_news2);
        mToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        mProgressBar = findViewById(R.id.progressBar);
        recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getApplicationContext()),
                DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mNewsApiRecycleViewAdapter = new NewsApiRecycleViewAdapter(new ArrayList<NewsItems>(), this);
        recyclerView.setAdapter(mNewsApiRecycleViewAdapter);
        mProgressBar.setVisibility(View.VISIBLE);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        MaterialSearchView searchView = findViewById(R.id.search_view);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume starts");
        super.onResume();
        Intent intent = getIntent();
        String newsName = intent.getStringExtra(AllChannel.NEWS_NAME);
        mToolbar.setTitle(newsName);
        String finalName = makeReadableToJson(newsName);
//        String result = newsName.replaceAll("\\p{P}", "-").substring(0, newsName.length() - 1);
//        String a = result.replaceAll(" ", "-");

//        GetNewsJsonData getNewsJsonData = new GetNewsJsonData(this, "https://newsapi.org/v2/everything?");
        GetNewsJsonData getNewsJsonData = new GetNewsJsonData(this);
//        getFlickrJsonData.executeOnSameThread("android, nougat");
        getNewsJsonData.execute("https://newsapi.org/v2/top-headlines?sources=" + finalName + "&apiKey=<<YOURKEY>>");
        Log.d(TAG, "onResume ends");
    }

    @Override
    public void onDataAvailable(List<NewsItems> data, DownloadStatus status) {
        Log.d(TAG, "onDataAvailable: starts");
        if (status == DownloadStatus.OK) {
            mNewsApiRecycleViewAdapter.loadNewData(data);
        } else {
            // download or processing failed
            Log.e(TAG, "onDataAvailable failed with status " + status);
        }
        mProgressBar.setVisibility(View.GONE);
        Log.d(TAG, "onDataAvailable: ends");
    }

    public static String makeReadableToJson(String name) {
        if (name.equals("Reddit /r/all")) {
            return "reddit-r-all";
        }
        if (name.equals("ANSA.it")) {
            return "ansa";
        }
        if (name.equals("News.com.au")) {
            return "news-com-au";
        }
        String result = null;
//        assert name != null;
        String finalResult;//        String t2 = "!@#$%^&*()-';,./?><+abdd";
        result = name.replaceAll("\\p{P}", "");
        result = result.replaceAll(" ", "-").toLowerCase();
        finalResult = result.replace("/", "-").toLowerCase();
        return finalResult;
    }
}
