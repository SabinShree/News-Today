package activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import datacontainer.NewsItems;
import nepalinewsfeeder.sabinkharel.com.fragment.R;

public class NewsApiRecycleViewAdapter extends RecyclerView.Adapter<NewsApiRecycleViewAdapter.NewsImageViewHolder> {
    private static final String TAG = "NewsApiRecycleViewAdapt";
    private List<NewsItems> mNewsItems;
    private Context context;
    public static final String NEWSLINK = "";

    public NewsApiRecycleViewAdapter(List<NewsItems> newsItems, Context context) {
        mNewsItems = newsItems;
        this.context = context;
    }

    public List<NewsItems> getNewsItems() {
        return mNewsItems;
    }

    @NonNull
    @Override
    public NewsImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card_layout, parent, false);
        return new NewsImageViewHolder(view);
    }

    void loadNewData(List<NewsItems> newsItems) {
        mNewsItems = newsItems;
        notifyDataSetChanged();
    }

    public NewsItems getItems(int position) {
        return ((mNewsItems != null) && (mNewsItems.size() != 0) ? mNewsItems.get(position) : null);
    }


    @Override
    public void onBindViewHolder(@NonNull final NewsImageViewHolder holder, int position) {
        try {
            NewsItems newsItems = mNewsItems.get(position);
            Log.d(TAG, "onBindViewHolder: " + newsItems.getImageUrl() + " --> " + position);
            Picasso.get().load(newsItems.getImageUrl())
                    .error(R.drawable.no_any_image)
                    .placeholder(R.drawable.noimage)
                    .into(holder.newsImage);
            String noSource = "Unknown";
            if (newsItems.getAuthor() == null) {
                holder.newsSource.setText(noSource);
            }
            SimpleDateFormat format = null;
            Date date;
            Date dateFromString = null;
            String description = "No Description displayed. But you can read the full news here.";
            try {
//                format = new SimpleDateFormat(
//                        "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//                format.setTimeZone(TimeZone.getTimeZone("GMT"));
                date = new Date();
                String dateAsString = format.format(date);
                dateFromString = format.parse(dateAsString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (newsItems.getDescription() == null || newsItems.getDescription().equals("null")) {
                holder.description.setText(description);
            } else {
                holder.description.setText(newsItems.getDescription());
            }
            PrettyTime prettyTime = new PrettyTime();
            holder.uploadTime.setText(prettyTime.format(dateFromString));
            holder.newsSource.setText(newsItems.getAuthor());
            holder.newsHeadline.setText(newsItems.getNewsName());
            holder.setListener(new NewsImageViewHolder.ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    if (isLongClick) {
                        Toast.makeText(context, "No Menu for long press", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(holder.itemView.getContext(), WebViewOpener.class);
                        intent.putExtra(NEWSLINK, mNewsItems.get(position).getUrl().toLowerCase());
                        holder.itemView.getContext().startActivity(intent);

                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return (mNewsItems != null ? mNewsItems.size() : 0);
    }

    static class NewsImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private static final String TAG = "NewsImageViewHolder";
        ImageView newsImage;
        TextView newsHeadline;
        TextView description;
        TextView newsSource;
        TextView uploadTime;
        private ItemClickListener listener;


        public NewsImageViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "NewsImageViewHolder: starts.");
            newsImage = itemView.findViewById(R.id.newsImage);
            newsHeadline = itemView.findViewById(R.id.newsHeadline);
            description = itemView.findViewById(R.id.description);
            newsSource = itemView.findViewById(R.id.newsSource);
            uploadTime = itemView.findViewById(R.id.uploadTime);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getLayoutPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onClick(view, getLayoutPosition(), true);
            return true;
        }

        public void setListener(ItemClickListener listener) {
            this.listener = listener;
        }

        interface ItemClickListener {
            void onClick(View view, int position, boolean isLongClick);
        }

    }
}
