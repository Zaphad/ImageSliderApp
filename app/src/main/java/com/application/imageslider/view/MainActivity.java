package com.application.imageslider.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.application.imageslider.R;
import com.application.imageslider.adapter.PostersAdapter;
import com.application.imageslider.model.Poster;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private GridLayoutManager layoutManager;
    private PostersAdapter postersAdapter;
    private SwipeRefreshLayout swipeContainer;

    private void setOrientation(){
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        else{
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }
    }

    private void initializeUI() {

        swipeContainer = findViewById(R.id.swipe_container);
        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.recycler_view);
        setOrientation();
        postersAdapter = new PostersAdapter(new ArrayList<>(), this);
        postersAdapter.addPosters(new ArrayList<>());
        recyclerView.setAdapter(postersAdapter);
    }

    private void setListeners() {

            swipeContainer.setOnRefreshListener(() -> {
                swipeContainer.setRefreshing(true);
                loadIntoAdapter(true);
                swipeContainer.setRefreshing(false);
            });
        }

    private void loadIntoAdapter(Boolean onRefresh){
        if(onRefresh){postersAdapter.removePosters();}
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://dev-tasks.alef.im/task-m-001/list.php").build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {

                String str = response.body()
                        .string()
                        .trim()
                        .replace('[', ' ')
                        .replace(']', ' ')
                        .replaceAll("[\"]", "");
                List<String> responseBody = Arrays.asList(str.split("\\s*,\\s*"));
                List<Poster> posterList = new ArrayList<>();

                for(String string : responseBody) {
                    Poster poster = new Poster();
                    Log.i("address:",string);
                    poster.setPosterUri(string.trim());
                    posterList.add(poster);
                }

                runOnUiThread(() -> {
                    Log.i("posters count", "" + posterList.size());
                        postersAdapter.addPosters(posterList);
                });

            }
        });
    }

    public void onPosterClick(int position) {
        Toast toast = new Toast(getApplicationContext());
        ImageView imageView = new ImageView(this);
        Picasso.get().load(postersAdapter.getPosterList().get(position).getPosterUri()).into(imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.FILL,0,0);
        toast.setView(imageView);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUI();
        setListeners();
        loadIntoAdapter(false);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setOrientation();
    }
}



