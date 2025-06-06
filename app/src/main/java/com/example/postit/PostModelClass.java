package com.example.postit;

public class PostModelClass {
    private int id;
    private String title;
    private String description;
    private String banner;
    private String user;
    private String created_at;

    public PostModelClass(int id, String title, String description, String banner, String user, String created_at) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.banner = banner;
        this.user = user;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
