package com.example.postit;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvPosts;
    List<PostModelClass> dataList = new ArrayList<>();
    PostAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvPosts = findViewById(R.id.rvPosts);
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        dataAdapter = new PostAdapter(dataList);
        rvPosts.setAdapter(dataAdapter);
        fetchPosts();
    }

    private void fetchPosts(){
        dataList.clear();

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<List<PostModelClass>> call = apiService.getPosts();

        call.enqueue(new Callback<List<PostModelClass>>() {
            @Override
            public void onResponse(Call<List<PostModelClass>> call, Response<List<PostModelClass>> response) {
                if (response.isSuccessful()) {
                    List<PostModelClass> posts = response.body();
                    if (posts != null && !posts.isEmpty()) {
                        dataList.addAll(posts);

                        dataAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Failed to fetch posts", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PostModelClass>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error fetching post data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}