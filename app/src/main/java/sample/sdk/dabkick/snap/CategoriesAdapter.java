package sample.sdk.dabkick.snap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabkick.videosdk.publicsettings.DabKickMedia;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by iFocus on 02-04-2018.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryHolder> {

    private ArrayList<DabKickMedia> list;
    private Context context;
    private OnItemClickListener itemClickListener;

    CategoriesAdapter(Context context, ArrayList<DabKickMedia> categories, OnItemClickListener itemClickListener) {
        list = categories;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {

        DabKickMedia url = list.get(position);
        Picasso.with(context)
                .load(url.getUrl())
                .into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    interface OnItemClickListener {
        void onItemClick(DabKickMedia videoInfo);
    }

    public class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView thumbnail;
        CategoryHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            thumbnail = itemView.findViewById(R.id.item_video_thumbnail);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(list.get(getAdapterPosition()));
        }
    }
}
