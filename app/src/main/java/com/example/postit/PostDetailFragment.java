package com.example.postit;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostDetailFragment extends Fragment {
    TextView tvPostTitle, tvPostDate, tvPostDescription;
    ImageView imageViewBanner;
    Button btnEdit;

    List<PostModelClass> dataList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    SharedPreferences authSharedPreferences;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PostDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostDetailFragment newInstance(String param1, String param2) {
        PostDetailFragment fragment = new PostDetailFragment();
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
    public void onPause() {
        super.onPause();
        //btnEdit.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvPostTitle = view.findViewById(R.id.tvPostTitle);
        tvPostDate = view.findViewById(R.id.tvPostDate);
        tvPostDescription = view.findViewById(R.id.tvPostDescription);
        imageViewBanner = view.findViewById(R.id.imageViewBanner);
        btnEdit = view.findViewById(R.id.btnEdit);

        sharedPreferences = getContext().getSharedPreferences("post", MODE_PRIVATE);
        authSharedPreferences = getContext().getSharedPreferences("auth", MODE_PRIVATE);
        fetchPostDetails();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditPostFragment fragment = new EditPostFragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, fragment)
                        .commit();
            }
        });
    }

    private void fetchPostDetails(){
        dataList.clear();

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<PostModelClass> call = apiService.getPostById(sharedPreferences.getInt("postId", 0));

        call.enqueue(new Callback<PostModelClass>() {
            @Override
            public void onResponse(Call<PostModelClass> call, Response<PostModelClass> response) {
                PostModelClass post = response.body();
                if (response.isSuccessful()) {
                    if (post != null) {
                        Log.d("Post1", post.getUser());
                        Log.d("Post2", authSharedPreferences.getString("username", ""));
                        if (post.getUser().equals(authSharedPreferences.getString("username", ""))) {
                            btnEdit.setVisibility(View.VISIBLE);
                        }
                        tvPostTitle.setText(post.getTitle());
                        tvPostDate.setText("" + post.getCreated_at() + " by " + post.getUser());
                        Glide.with(imageViewBanner.getContext()).load(RetrofitClient.BASE_URL + post.getBanner()).into(imageViewBanner);
                        tvPostDescription.setText(post.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<PostModelClass> call, Throwable t) {
                Toast.makeText(getContext(), "Error fetching post data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}