package com.example.postit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

import java.util.List;

public interface ApiService {
    @GET("posts/")
    Call<List<PostModelClass>> getPosts();

    @GET("user_posts/{user_id}/")
    Call<List<PostModelClass>> getUserPosts(
            @Header("Authorization") String authToken,
            @Path("user_id") int userId
    );

    @Multipart
    @POST("login/")
    Call<LoginModelClass> login(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password
    );

    @Multipart
    @POST("new_post/")
    Call<PostModelClass> createPost(
            @Header("Authorization") String token,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part banner
    );

}