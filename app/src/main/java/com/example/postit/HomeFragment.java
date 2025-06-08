package com.example.postit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    RecyclerView rvPosts;
    List<PostModelClass> dataList = new ArrayList<>();
    PostAdapter dataAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPosts = view.findViewById(R.id.rvPosts);
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        dataAdapter = new PostAdapter(dataList);
        rvPosts.setAdapter(dataAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
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
                        List<PostModelClass> newDataList = new ArrayList<>(posts);
                            dataList.addAll(posts);

                            dataAdapter.notifyDataSetChanged();
                    }

                } else {
                    Toast.makeText(getContext(), "Failed to fetch posts", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PostModelClass>> call, Throwable t) {
                Toast.makeText(getContext(), "Error fetching post data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}