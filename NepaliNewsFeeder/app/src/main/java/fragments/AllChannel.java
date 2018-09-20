package fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import activities.ChannelWiseNews;
import adapter.CustomAdapter;
import datacontainer.HTMLClass;
import nepalinewsfeeder.sabinkharel.com.fragment.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllChannel.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AllChannel#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllChannel extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private Context context = this.getContext();
    private static final String ARG_PARAM2 = "param2";
    public static final String URL = "https://newsapi.org/sources";
    private boolean isLoaded = false;
    private ProgressBar mProgressBar;
    public static final String NEWS_NAME = "news";

    private GridView gridView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AllChannel() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllChannel.
     */
    // TODO: Rename and change types and number of parameters
    public static AllChannel newInstance(String param1, String param2) {
        AllChannel fragment = new AllChannel();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_channel, container, false);
        gridView = view.findViewById(R.id.mainGrids);
        mProgressBar = view.findViewById(R.id.progress_bar);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String items = (String) gridView.getItemAtPosition(position);
                Intent intent = new Intent(getContext(), ChannelWiseNews.class);
                intent.putExtra(NEWS_NAME, items);
                startActivity(intent);
                Toast.makeText(getContext(), "Button clicked " + items, Toast.LENGTH_LONG).show();
            }
        });
        mProgressBar.setVisibility(View.VISIBLE);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new DataFetcher().execute(URL);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     **/

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public class DataFetcher extends AsyncTask<String, Void, Document> {
        private static final String TAG = "DataFetcher";
        private Document document;
        private ArrayList<HTMLClass> htmlClassList = new ArrayList<>();
        private ArrayList<String> newsName = new ArrayList<>();
        private ArrayList<String> newsPhotoUrl = new ArrayList<>();

        @Override
        protected Document doInBackground(String... strings) {
            try {
                document = Jsoup.connect(strings[0]).timeout(20000).get();
                parseHTML();
            } catch (IOException e) {
//                Toast.makeText(context, "Problem loading to news channel.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return document;
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);
            CustomAdapter adapter = new CustomAdapter(Objects.requireNonNull(getActivity()).getBaseContext(), newsName, newsPhotoUrl);
            gridView.setAdapter(adapter);
            mProgressBar.setVisibility(View.GONE);
        }

        private void parseHTML() {
            try {
                Element wrapper = document.getElementsByClass("sources-container mb4 center ph3 relative").get(0);
                Elements div = wrapper.getElementsByClass("source publisher fl-ns w-25-l w-50-m mb2");
                for (Element a : div) {
                    htmlClassList.add(new HTMLClass(a.getElementsByClass("name f3").get(0).text(), a.select("a > img").get(0).attr("data-src")));
                }
                for (HTMLClass w : htmlClassList) {
                    newsPhotoUrl.add(w.getImageURL());
                    newsName.add(w.getName());
                }
//                for (String s : newsName) {
//                    Log.d(TAG, "parseHtml : NewsName " + s);
//                }
//                for (String s : newsPhotoUrl) {
//                    Log.d(TAG, "parseHtml : Photo URL " + s);
//                }
            } catch (Exception e) {
                Log.d(TAG, "parseHTML: " + e.getMessage());
            }
        }
    }

}
