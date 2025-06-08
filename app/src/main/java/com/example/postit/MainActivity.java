package com.example.postit;

import static android.view.View.GONE;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentContainerView;

public class MainActivity extends AppCompatActivity {
    FragmentContainerView fragmentContainerView;
    ImageButton btnMenu;
    LinearLayout menu, menuLogged;
    TextView tvHome, tvLogin, tvRegister, tvPostIt, tvHomeLogged, tvYourPost, tvNewPost, tvLogout;

    static SharedPreferences sharedPreferences;
    String token;

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

        sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);

        fragmentContainerView = findViewById(R.id.fragmentContainerView);
        btnMenu = findViewById(R.id.btnMenu);
        menu = findViewById(R.id.menu);
        menuLogged = findViewById(R.id.menuLogged);

        tvHome = findViewById(R.id.tvHome);
        tvLogin = findViewById(R.id.tvLogin);
        tvRegister = findViewById(R.id.tvRegister);
        tvPostIt = findViewById(R.id.tvPostIt);
        tvHomeLogged = findViewById(R.id.tvHomeLogged);
        tvYourPost = findViewById(R.id.tvYourPost);
        tvNewPost = findViewById(R.id.tvNewPost);
        tvLogout = findViewById(R.id.tvLogout);

        setToHome();

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu();
            }
        });

        tvPostIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setToHome();
                hideMenu();
            }
        });

        tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setToHome();
                showMenu();

                highlightTextView(tvHome);
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setToLogin();
                showMenu();

                highlightTextView(tvLogin);
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setToRegister();
                showMenu();

                highlightTextView(tvRegister);
            }
        });

        tvHomeLogged.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setToHome();
                showMenu();

                highlightTextView(tvHomeLogged);
            }
        });

        tvYourPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setToUserPost();
                showMenu();

                highlightTextView(tvYourPost);
            }
        });

        tvNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setToNewPost();
                showMenu();

                highlightTextView(tvNewPost);
        }
    });

    }

    @Override
    protected void onResume() {
        super.onResume();
        token = sharedPreferences.getString("token", null);
    }

    private void hideMenu(){
        if (token != null) {
            menuLogged.setVisibility(GONE);
        } else {
            menu.setVisibility(GONE);
        }
    }

    private void showMenu(){
        if (token != null) {
            if (menuLogged.getVisibility() == GONE) {
                menuLogged.setVisibility(View.VISIBLE);
            } else {
                menuLogged.setVisibility(GONE);
            }
        } else {
            if (menu.getVisibility() == GONE) {
                menu.setVisibility(View.VISIBLE);
            } else {
                menu.setVisibility(GONE);
            }
        }
    }

    private void highlightTextView(TextView textView) {
        tvHome.setTextColor(ContextCompat.getColor(this, R.color.light_gray));
        tvLogin.setTextColor(ContextCompat.getColor(this, R.color.light_gray));
        tvRegister.setTextColor(ContextCompat.getColor(this, R.color.light_gray));
        tvHomeLogged.setTextColor(ContextCompat.getColor(this, R.color.light_gray));
        tvYourPost.setTextColor(ContextCompat.getColor(this, R.color.light_gray));
        tvNewPost.setTextColor(ContextCompat.getColor(this, R.color.light_gray));
        tvLogout.setTextColor(ContextCompat.getColor(this, R.color.light_gray));

        textView.setTextColor(ContextCompat.getColor(this, R.color.white));
    }

    private void setToHome(){
        HomeFragment fragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit();
    }

    private void setToLogin(){
        LoginFragment fragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit();
    }

    private void setToRegister(){
        RegisterFragment fragment = new RegisterFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit();
    }

    private void setToUserPost(){
        UserPostFragment fragment = new UserPostFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit();
    }

    private void setToNewPost() {
        NewPostFragment fragment = new NewPostFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit();
    }

}