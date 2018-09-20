package datacontainer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import activities.CategoryActivityNews;
import nepalinewsfeeder.sabinkharel.com.fragment.R;

public class RecycleAdapter extends RecyclerView.Adapter<StationView> {
    private ArrayList<CategoryItems> stationViews;
    private Context context;
    public static final String CATEGORY = "CATEGORY";

    public RecycleAdapter(Context context, ArrayList<CategoryItems> stationViews) {
        this.stationViews = stationViews;
        this.context = context;
    }

    @NonNull
    @Override
    public StationView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_insert_adapter, parent, false);
        return new StationView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StationView holder, int position) {
        CategoryItems categoryItems = stationViews.get(position);
        holder.getUI(categoryItems);
        holder.setListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    Toast.makeText(context, "No Menu for long press", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(holder.itemView.getContext(), CategoryActivityNews.class);
                    intent.putExtra(CATEGORY, stationViews.get(position).getName().toLowerCase());

                    holder.itemView.getContext().startActivity(intent);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return stationViews.size();
    }
}
