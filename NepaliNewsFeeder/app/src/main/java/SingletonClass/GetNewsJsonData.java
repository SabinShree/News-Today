package SingletonClass;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import datacontainer.NewsItems;

public class GetNewsJsonData extends AsyncTask<String, Void, List<NewsItems>> implements GetJsonDataOnline.OnDownloadComplete {
    private static final String TAG = "GetNewsJsonData";
    private List<NewsItems> mNewsItems = null;
    private final OnDataAvailable mOnDataAvailable;
    private String baseUri;
    private boolean isRunningOnSameThread = false;

    public GetNewsJsonData(OnDataAvailable callBack) {
        Log.d(TAG, "GetFlickrJsonData called");
        mOnDataAvailable = callBack;
    }

    public interface OnDataAvailable {
        void onDataAvailable(List<NewsItems> data, DownloadStatus status);
    }

    @Override
    protected void onPostExecute(List<NewsItems> newsItems) {
        Log.d(TAG, "onPostExecute starts");

        if (mOnDataAvailable != null) {
            mOnDataAvailable.onDataAvailable(newsItems, DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute ends");
    }

    @Override
    public void OnDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete starts. Status = " + status);

        if (status == DownloadStatus.OK) {
            mNewsItems = new ArrayList<>();

            try {
                JSONObject jsonData = new JSONObject(data);
                JSONArray itemsArray = jsonData.getJSONArray("articles");

                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject jsonPhoto = itemsArray.getJSONObject(i);
                    String title = jsonPhoto.getString("title");
                    String author = jsonPhoto.getString("author");
                    String description = jsonPhoto.getString("description");
                    String url = jsonPhoto.getString("url");
                    String publishedAt = jsonPhoto.getString("publishedAt");

//                    JSONObject jsonMedia = jsonPhoto.getJSONObject("media");
                    String photoUrl = jsonPhoto.getString("urlToImage");
                    JSONObject jsonObject = jsonPhoto.getJSONObject("source");
                    String authorName = jsonObject.getString("name");
//                    String link = photoUrl.replaceFirst("_m.", "_b.");

                    NewsItems items = new NewsItems(photoUrl, title, description, publishedAt, authorName, url);
                    mNewsItems.add(items);

                    Log.d(TAG, "onDownloadComplete " + items.toString());
                }
            } catch (JSONException jsone) {
                jsone.printStackTrace();
                Log.e(TAG, "onDownloadComplete: Error processing Json data " + jsone.getMessage());
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
        }

        if (isRunningOnSameThread && mOnDataAvailable != null) {
            // now inform the caller that processing is done - possibly returning null if there
            // was an error
            mOnDataAvailable.onDataAvailable(mNewsItems, status);
        }

        Log.d(TAG, "onDownloadComplete ends");
    }

    @Override
    protected List<NewsItems> doInBackground(String... strings) {
        Log.d(TAG, "doInBackground starts");
        String destinationUri = createUri(strings[0]);

        GetJsonDataOnline getRawData = new GetJsonDataOnline(this);
        getRawData.runOnSameThread(destinationUri);
        Log.d(TAG, "doInBackground ends");
        return mNewsItems;
    }

    private String createUri(String baseUrl) {
        Log.d(TAG, "createUri starts");
        this.baseUri = baseUrl;

        return Uri.parse(baseUrl)
                .toString();
    }

    void executeOnSameThread() {
        Log.d(TAG, "executeOnSameThread starts");
        isRunningOnSameThread = true;
        String destinationUri = createUri(baseUri);
        GetJsonDataOnline getRawData = new GetJsonDataOnline(this);
        getRawData.execute(destinationUri);
        Log.d(TAG, "executeOnSameThread ends");
    }
}
