package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import nepalinewsfeeder.sabinkharel.com.fragment.R;


public class CustomAdapter extends ArrayAdapter<String> {
    private final Context context;
    private static final String TAG = "CustomAdapter";
    private final ArrayList<String> items;
    private final ArrayList<String> logo;

    public CustomAdapter(@NonNull Context context, ArrayList<String> items, ArrayList<String> logo) {
        super(context, R.layout.custom_layout, items);
        this.context = context;
        this.items = items;
        this.logo = logo;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.custom_layout, null, true);
        TextView newsName = rowView.findViewById(R.id.textView);
        ImageView imageView = rowView.findViewById(R.id.imageView);
        newsName.setText(items.get(position));
        Picasso.get().load(logo.get(position)).into(imageView);
//        imageView.setimage(loadImageFromUrl(""));
//        imageView.setImageResource(R.drawable.abcnews);

        return rowView;
    }
}
