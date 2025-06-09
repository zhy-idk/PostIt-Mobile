package com.example.postit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

import java.util.List;

public interface ApiService {
    @GET("api/posts/")
    Call<List<PostModelClass>> getPosts();

    @GET("api/post/{post_id}/")
    Call<PostModelClass> getPostById(
            @Path("post_id") int postId
    );

    @GET("api/user_posts/{user_id}/")
    Call<List<PostModelClass>> getUserPosts(
            @Header("Authorization") String authToken,
            @Path("user_id") int userId
    );

    @Multipart
    @POST("api/login/")
    Call<LoginModelClass> login(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password
    );

    @Multipart
    @POST("api/register/")
    Call<RegisterModelClass> register(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part("password2") RequestBody password2
    );

    @POST("api/logout/")
    Call<Void> logout(
            @Header("Authorization") String token
    );

    @Multipart
    @POST("api/new_post/")
    Call<PostModelClass> createPost(
            @Header("Authorization") String token,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part banner
    );

    @Multipart
    @PUT("api/edit_post/{post_id}/")
    Call<PostModelClass> editPost(
            @Header("Authorization") String token,
            @Path("post_id") int postId,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part banner
    );

    @DELETE("api/delete/{post_id}/")
    Call<Void> deletePost(
            @Header("Authorization") String token,
            @Path("post_id") int postId
    );


}