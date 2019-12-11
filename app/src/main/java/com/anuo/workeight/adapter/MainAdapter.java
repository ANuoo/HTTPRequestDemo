package com.anuo.workeight.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anuo.workeight.R;
import com.anuo.workeight.data.Item;
import com.anuo.workeight.manager.DataManager;
import com.bumptech.glide.Glide;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HAS_IMG = 1;
    private static final int TYPE_NO_IMG = 0;
    private static final int TYPE_LOAD = 2;
    private List<Item> list;
    private Context context;
    private boolean loading = false;

    private LoadViewHolder loadViewHolder;
    private DataManager.Callback loadMoreCallback = new DataManager.Callback() {
        @Override
        public void onSuccess() {
            loading = false;
            notifyItemRangeChanged(list.size() - DataManager.requestNum, DataManager.requestNum);
        }

        @Override
        public void onFail(Exception e) {
            loadViewHolder.load.setText("加载失败");
        }
    };

    public MainAdapter(List<Item> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        if (type == TYPE_HAS_IMG) {
            return new ImageViewHolder(inflateView(viewGroup, R.layout.item_image_recycler_main));
        } else if (type == TYPE_NO_IMG) {
            return new ViewHolder(inflateView(viewGroup, R.layout.item_recycler_main));
        } else {
            return loadViewHolder == null ?
                    (loadViewHolder = new LoadViewHolder(inflateView(viewGroup, R.layout.item_load)))
                    : loadViewHolder;
        }
    }

    private View inflateView(@NonNull ViewGroup viewGroup, @LayoutRes int layoutId) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            holder.title.setText(list.get(i).getTitle());
            holder.user.setText(list.get(i).getUser());
            holder.time.setText(list.get(i).getTime());
        } else if (viewHolder instanceof ImageViewHolder) {
            ImageViewHolder holder = (ImageViewHolder) viewHolder;
            holder.title.setText(list.get(i).getTitle());
            holder.user.setText(list.get(i).getUser());
            holder.time.setText(list.get(i).getTime());
            Glide.with(context).load(list.get(i).getImageUrl()).override(dpToPx(72), dpToPx(72)).centerCrop().into(holder.image);
        } else {
            loadViewHolder.load.setText("正在加载");
            if (!loading) {
                loading = true;
                DataManager.getInstance().loadMore(loadMoreCallback);

            }
        }
    }

    public int dpToPx(float dp) {
        float px = context.getResources().getDisplayMetrics().density;
        return (int) (dp * px + 0.5f);
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size()) {
            return TYPE_LOAD;
        } else if (list.get(position).getImageUrl() == null) {
            return TYPE_NO_IMG;
        } else {
            return TYPE_HAS_IMG;
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, user, time;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.txt_item_title);
            user = view.findViewById(R.id.txt_item_user);
            time = view.findViewById(R.id.txt_item_time);
        }
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView title, user, time;
        ImageView image;

        public ImageViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.txt_item_image_title);
            user = view.findViewById(R.id.txt_item_image_user);
            time = view.findViewById(R.id.txt_item_image_time);
            image = view.findViewById(R.id.img_item_image);
        }
    }

    class LoadViewHolder extends RecyclerView.ViewHolder {
        TextView load;

        public LoadViewHolder(View view) {
            super(view);
            load = view.findViewById(R.id.txt_item_load);
        }
    }


}
