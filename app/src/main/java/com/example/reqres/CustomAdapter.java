package com.example.reqres;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.reqres.model.data;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final String BASE_AVATAR_IMG = "https://s3.amazonaws.com/uifaces/faces/twitter/calebogden/128.jpg";

    private List<data> data;
    private Context context;

    private boolean isLoadingAdded = false;

    public CustomAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    public List<data> getData(){
        return data;
    }

    public void setData (List<data> data){
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (i) {
            case ITEM:
                viewHolder = getViewHolder(viewGroup, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, viewGroup, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    private RecyclerView.ViewHolder getViewHolder(ViewGroup viewGroup, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.item_list, viewGroup, false);
        viewHolder = new UserVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        data userData = data.get(i);

        switch (getItemViewType(i)) {
            case ITEM:
                final UserVH userVH = (UserVH) viewHolder;

                userVH.mFname.setText(userData.getFname());


                userVH.mLname.setText(userData.getLname());

                Glide
                        .with(context)
                        .load(BASE_AVATAR_IMG + userData.getAvtar())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                userVH.mProgress.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                                userVH.mProgress.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .crossFade()
                        .into(userVH.mAvatar);

                break;

            case LOADING:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void add(data d) {
        data.add(d);
        notifyItemInserted(data.size() - 1);
    }

    public void addAll(List<data> d1) {
        for (data data1 : d1) {
            add(data1);
        }
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new data());
    }

    protected class UserVH extends RecyclerView.ViewHolder {
        private TextView mFname;
        private TextView mLname;
        private ImageView mAvatar;
        private ProgressBar mProgress;

        public UserVH(View itemView) {
            super(itemView);

            mFname = (TextView) itemView.findViewById(R.id.fname);
            mLname = (TextView) itemView.findViewById(R.id.lname);
            mAvatar = (ImageView) itemView.findViewById(R.id.avatar);
            mProgress = (ProgressBar) itemView.findViewById(R.id.avatar_progress);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }
}
