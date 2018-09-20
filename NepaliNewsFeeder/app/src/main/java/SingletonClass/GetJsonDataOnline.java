package SingletonClass;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetJsonDataOnline extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetJsonDataOnline";

    private DownloadStatus mDownloadStatus;
    private final OnDownloadComplete mOnDownloadComplete;

    interface OnDownloadComplete {
        void OnDownloadComplete(String data, DownloadStatus status);
    }

    public GetJsonDataOnline(OnDownloadComplete downloadComplete) {
        this.mOnDownloadComplete = downloadComplete;
        mDownloadStatus = DownloadStatus.IDLE;
    }


    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        if (strings == null) {
            mDownloadStatus = DownloadStatus.NOT_INITIALIZED;
            return null;
        }

        try {
            mDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(strings[0]);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: The response code was " + response);

            StringBuilder result = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                result.append(line).append("\n");
            }

            mDownloadStatus = DownloadStatus.OK;
            return result.toString();


        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground: Invalid URL " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: IO Exception reading data: " + e.getMessage());
        } catch (SecurityException e) {
            Log.e(TAG, "doInBackground: Security Exception. Needs permission? " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: Error closing stream " + e.getMessage());
                }
            }
        }
        mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if (mOnDownloadComplete != null) {
            mOnDownloadComplete.OnDownloadComplete(s, mDownloadStatus);
        }
    }

    void runOnSameThread(String s) {
        if (mOnDownloadComplete != null) {
            mOnDownloadComplete.OnDownloadComplete(doInBackground(s), mDownloadStatus);
        }
    }
}

