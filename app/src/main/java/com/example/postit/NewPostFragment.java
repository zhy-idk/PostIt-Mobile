package com.example.postit;

import static android.content.Context.MODE_PRIVATE;

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

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewPostFragment extends Fragment {
    TextView tvFileName;
    EditText etTitle, etDescription;
    LinearLayout btnFileSelect;
    ImageView imageView;
    Button btnSubmit;

    Uri imageUri;
    SharedPreferences sharedPreferences;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewPostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewPostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewPostFragment newInstance(String param1, String param2) {
        NewPostFragment fragment = new NewPostFragment();
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
        return inflater.inflate(R.layout.fragment_new_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvFileName = view.findViewById(R.id.tvFileName);
        etTitle = view.findViewById(R.id.etTitle);
        etDescription = view.findViewById(R.id.etDescription);
        btnFileSelect = view.findViewById(R.id.btnFileSelect);
        imageView = view.findViewById(R.id.imageView);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        sharedPreferences = getActivity().getSharedPreferences("auth", MODE_PRIVATE);

        btnFileSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImage();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPost();
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
                imageUri = uri;
                imageView.setImageURI(imageUri);
                File imageFile = new File(FileUtils.getFilePath(requireContext(), imageUri));
                tvFileName.setText(imageFile.getName());
            });

    public void createPost(){

        String title = etTitle.getText().toString();
        String description = etDescription.getText().toString();
        String fileName = tvFileName.getText().toString();

        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody descriptionBody = RequestBody.create(MediaType.parse("text/plain"), description);

        MultipartBody.Part imagePart = null;
        if (imageUri != null) {
            File imageFile = new File(FileUtils.getFilePath(requireContext(), imageUri));
            RequestBody imageRequest = RequestBody.create(MediaType.parse("image/*"), imageFile);
            imagePart = MultipartBody.Part.createFormData("banner", imageFile.getName(), imageRequest);
            Log.d("Image name", imageFile.toString());
        }

        String token = sharedPreferences.getString("token", null);

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<PostModelClass> call = apiService.createPost("Token " + token, titleBody, descriptionBody, imagePart);
        call.enqueue(new Callback<PostModelClass>() {
            @Override
            public void onResponse(Call<PostModelClass> call, Response<PostModelClass> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), "Post created successfully", Toast.LENGTH_SHORT).show();
                    etTitle.setText("");
                    etDescription.setText("");
                    imageView.setImageURI(null);

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
}