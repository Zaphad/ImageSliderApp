package com.application.imageslider.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.imageslider.R;
import com.application.imageslider.model.Poster;
import com.application.imageslider.view.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostersAdapter extends RecyclerView.Adapter<PostersAdapter.ViewHolder> {

    private List<Poster> posterList;
    private MainActivity mainActivity;

    public PostersAdapter(List<Poster> posterList, MainActivity mainActivity){
        this.posterList = posterList;
        this.mainActivity = mainActivity;
    }

    public List<Poster> getPosterList() {return posterList; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poster_layout, parent, false);
        int size = calculateSizeOfView(mainActivity);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = size;
        view.setLayoutParams(params);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Poster poster = posterList.get(position);
        Picasso.get().load(poster.getPosterUri()).into(holder.posterHolder);
        holder.itemView.setOnClickListener(v -> mainActivity.onPosterClick(position));
    }

    @Override
    public int getItemCount() {
        return posterList.size();
    }

    public void addPosters(List<Poster> posterList) {
        if (!this.posterList.contains(posterList)) {
            for (Poster poster : posterList) {

                this.posterList.add(poster);
            }
        }
        notifyDataSetChanged();
    }

    public void removePosters() {
        this.posterList.clear();
        notifyDataSetChanged();
    }

    public int calculateSizeOfView(Context context){
       if(mainActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            return (context.getResources().getDisplayMetrics().widthPixels/2);
       }else {
           return (context.getResources().getDisplayMetrics().widthPixels/3);
       }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView posterHolder;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            posterHolder = itemView.findViewById(R.id.poster);
        }
    }
}
