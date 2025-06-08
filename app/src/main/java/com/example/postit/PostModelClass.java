package com.example.postit;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostModelClass that = (PostModelClass) o;
        return id == that.id &&
                Objects.equals(title, that.title) &&
                Objects.equals(description, that.description) &&
                Objects.equals(banner, that.banner) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, banner, user);
    }

    @Override
    public String toString() {
        return "PostModelClass{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", banner='" + banner + '\'' +
                ", user='" + user + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }

}
