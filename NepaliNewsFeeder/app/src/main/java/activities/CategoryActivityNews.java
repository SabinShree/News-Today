package activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import datacontainer.RecycleAdapter;
import nepalinewsfeeder.sabinkharel.com.fragment.R;

public class CategoryActivityNews extends AppCompatActivity implements GetNewsJsonData.OnDataAvailable {
    private NewsApiRecycleViewAdapter mNewsApiRecycleViewAdapter;
    private static final String TAG = "CategoryActivityNews";
    private ArrayList<NewsItems> mNewsItems = new ArrayList<>();
    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private MaterialSearchView searchView;
    private RecyclerView recyclerView;
    private GetNewsJsonData getNewsJsonData;
    ArrayList<NewsItems> clonedList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_news2);
        mToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        recyclerView = findViewById(R.id.recycler_view);
        mProgressBar = findViewById(R.id.progressBar);
        loadRecycleView(recyclerView);
        mProgressBar.setVisibility(View.VISIBLE);
        if (!isNetworkAvailable()) {
            mProgressBar.setVisibility(View.GONE);
            final Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), "No internet available to load news.", Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(Color.RED);
            snackbar.setActionTextColor(Color.WHITE);
            snackbar.show();
            snackbar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
        }
    }

    public void loadRecycleView(RecyclerView recyclerView) {
        recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getApplicationContext()),
                DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mNewsApiRecycleViewAdapter = new NewsApiRecycleViewAdapter(mNewsItems, this);

        recyclerView.setAdapter(mNewsApiRecycleViewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView = findViewById(R.id.search_view);
        searchView.setHint("Search by headline.");
        searchView.setMenuItem(item);
        return true;
    }

//    public void processSearch(String title) {
//        ArrayList<NewsItems> result = new ArrayList<>();
//        for (NewsItems newsItems : getNewsJsonData.getNewsItems()) {
//            if (newsItems.getNewsName().toLowerCase().contains(title.toLowerCase())) {
//                result.add(newsItems);
//            }
//        }
//        mNewsApiRecycleViewAdapter.loadNewData(result);
//        loadRecycleView(recyclerView);
//
//    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
            mNewsApiRecycleViewAdapter.loadNewData(mNewsItems);
            loadRecycleView(recyclerView);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume starts");
        super.onResume();
        Intent intent = getIntent();
        String category = intent.getStringExtra(RecycleAdapter.CATEGORY);
        String upperCase = category.substring(0, 1).toUpperCase();
        String secondLowerCase = category.substring(1, category.length());
        String total = upperCase.concat(secondLowerCase);
        mToolbar.setTitle(total);
        getNewsJsonData = new GetNewsJsonData(this);
        getNewsJsonData.execute("https://newsapi.org/v2/top-headlines?country=in" + "&category=" + category + "&apiKey=<<YOURAPIKEY>>");
        Log.d(TAG, "onResume ends");
    }

    @Override
    public void onDataAvailable(final List<NewsItems> data, DownloadStatus status) {
        Log.d(TAG, "onDataAvailable: starts");
        if (status == DownloadStatus.OK) {
            mNewsApiRecycleViewAdapter.loadNewData(data);

        } else {
            Log.e(TAG, "onDataAvailable failed with status " + status);
        }
        mProgressBar.setVisibility(View.GONE);
        Log.d(TAG, "onDataAvailable: ends");
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
