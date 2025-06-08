package com.example.postit;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
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
import android.widget.EditText;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    EditText etUsername, etPassword;
    Button btnLogin;
    SharedPreferences sharedPreferences;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etUsername = view.findViewById(R.id.etUsername);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    public void login(){
        RequestBody username = RequestBody.create(MediaType.parse("text/plain"), etUsername.getText().toString());
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), etPassword.getText().toString());

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<LoginModelClass> call = apiService.login(username, password);

        call.enqueue(new Callback<LoginModelClass>() {
            @Override
            public void onResponse(Call<LoginModelClass> call, Response<LoginModelClass> response) {
                if (response.isSuccessful()) {
                    String token = response.body().getToken();
                    int userId = response.body().getUser_id();
                    String userName = response.body().getUsername();
                    sharedPreferences = getContext().getSharedPreferences("auth", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", token);
                    editor.putInt("user_id", userId);
                    editor.putString("username", userName);
                    editor.apply();

                    Log.d("TOKEN", sharedPreferences.getString("token", null));
                    Log.d("USERID", String.valueOf(sharedPreferences.getInt("user_id", -1)));

                    HomeFragment fragment = new HomeFragment();
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView, fragment)
                            .commit();

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    getActivity().finish();
                    startActivity(intent);
                } else {
                    Log.e("LOGIN_FAIL", "Invalid credentials or error");
                }
            }

            @Override
            public void onFailure(Call<LoginModelClass> call, Throwable t) {
                Log.e("LOGIN_ERROR", t.getMessage());
            }
        });
    }
}