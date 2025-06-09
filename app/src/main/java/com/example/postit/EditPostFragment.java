package com.example.postit;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditPostFragment extends Fragment {
    TextView tvEditFileName;
    EditText etEditTitle, etEditDescription;
    LinearLayout btnEditFileSelect;
    ImageView imageViewEditBanner;
    Button btnEditSubmit, btnDelete;

    Uri imageUri;
    SharedPreferences sharedPreferences;
    SharedPreferences authSharedPreferences;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditPostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditPostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditPostFragment newInstance(String param1, String param2) {
        EditPostFragment fragment = new EditPostFragment();
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
        return inflater.inflate(R.layout.fragment_edit_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvEditFileName = view.findViewById(R.id.tvEditFileName);
        etEditTitle = view.findViewById(R.id.etEditTitle);
        etEditDescription = view.findViewById(R.id.etEditDescription);
        imageViewEditBanner = view.findViewById(R.id.imageViewEditBanner);
        btnEditSubmit = view.findViewById(R.id.btnEditSubmit);
        btnDelete = view.findViewById(R.id.btnDelete);

        btnEditFileSelect = view.findViewById(R.id.btnEditFileSelect);

        sharedPreferences = getActivity().getSharedPreferences("post", MODE_PRIVATE);
        authSharedPreferences = getActivity().getSharedPreferences("auth", MODE_PRIVATE);

        String fileName = null;
        if (imageUri != null) {
            String path = FileUtils.getFilePath(requireContext(), imageUri);
            if (path != null) {
                fileName = new File(path).getName();
            }
        }

        tvEditFileName.setText(fileName != null ? fileName : "No file chosen");
        etEditTitle.setText(sharedPreferences.getString("postTitle", ""));
        etEditDescription.setText(sharedPreferences.getString("postDescription", ""));
        Glide.with(imageViewEditBanner.getContext()).load(RetrofitClient.BASE_URL + sharedPreferences.getString("postBanner", "")).into(imageViewEditBanner);

        btnEditSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitEdit();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePost();
            }
        });

        btnEditFileSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImage();
            }
        });
    }

    public void getImage() {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    imageUri = uri;
                    imageViewEditBanner.setVisibility(VISIBLE);
                    imageViewEditBanner.setImageURI(imageUri);
                    File imageFile = new File(FileUtils.getFilePath(requireContext(), imageUri));
                    tvEditFileName.setText(imageFile.getName());
                } else {
                    imageUri = uri;
                    imageViewEditBanner.setImageURI(imageUri);
                    imageViewEditBanner.setVisibility(GONE);
                    tvEditFileName.setText("No file chosen");
                }
            });

    private void submitEdit(){
        String title = etEditTitle.getText().toString();
        String description = etEditDescription.getText().toString();
        int id = sharedPreferences.getInt("postId", 0);

        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody descriptionBody = RequestBody.create(MediaType.parse("text/plain"), description);

        MultipartBody.Part imagePart = null;
        if (imageUri != null) {
            File imageFile = new File(FileUtils.getFilePath(requireContext(), imageUri));
            RequestBody imageRequest = RequestBody.create(MediaType.parse("image/*"), imageFile);
            imagePart = MultipartBody.Part.createFormData("banner", imageFile.getName(), imageRequest);
            Log.d("Image name", imageFile.toString());
        }

        String token = authSharedPreferences.getString("token", null);

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<PostModelClass> call = apiService.editPost("Token " + token, id, titleBody, descriptionBody, imagePart);
        call.enqueue(new Callback<PostModelClass>() {
            @Override
            public void onResponse(Call<PostModelClass> call, Response<PostModelClass> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getContext(), "Post created successfully", Toast.LENGTH_SHORT).show();
                    etEditTitle.setText("");
                    etEditDescription.setText("");
                    imageViewEditBanner.setImageURI(null);

                    HomeFragment fragment = new HomeFragment();
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView, fragment)
                            .commit();
                } else {
                    Toast.makeText(getContext(), "Failed to create post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostModelClass> call, Throwable t) {
                Toast.makeText(getContext(), "Error creating post: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deletePost(){
        String token = authSharedPreferences.getString("token", null);
        int id = sharedPreferences.getInt("postId", 0);

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<Void> call = apiService.deletePost("Token " + token, id);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getContext(), "Post deleted successfully", Toast.LENGTH_SHORT).show();

                    HomeFragment fragment = new HomeFragment();
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView, fragment)
                            .commit();
                } else {
                    Toast.makeText(getContext(), "Failed to delete post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error deleting post: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}