package datacontainer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import nepalinewsfeeder.sabinkharel.com.fragment.R;

public class StationView extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private ImageView imageTitle;
    private TextView newsName;
    private ItemClickListener listener;

    public StationView(View itemView) {
        super(itemView);
        this.imageTitle = itemView.findViewById(R.id.main_image);
        this.newsName = itemView.findViewById(R.id.main_text);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void getUI(CategoryItems categoryItems) {
        String uri = categoryItems.getImageName();
        int resources = imageTitle.getResources().getIdentifier(uri, null, imageTitle.getContext().getPackageName());
        imageTitle.setImageResource(resources);
        newsName.setText(categoryItems.getName());
    }

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
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
}

interface ItemClickListener {
    void onClick(View view, int position, boolean isLongClick);
}
