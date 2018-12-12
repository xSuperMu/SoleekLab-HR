package com.example.moham.soleeklab.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("job_title")
    @Expose
    private String jobTitle;
    @SerializedName("email")
    @Expose
    private String email;

    public User() {
    }

    public User(int id, String name, String email, String jobTitle, String profilePic, String createdAt) {
        this.name = name;
        this.profilePic = profilePic;
        this.createdAt = createdAt;
        this.id = id;
        this.jobTitle = jobTitle;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return
                "User{" +
                        "name = '" + name + '\'' +
                        ",profile_pic = '" + profilePic + '\'' +
                        ",created_at = '" + createdAt + '\'' +
                        ",id = '" + id + '\'' +
                        ",job_title = '" + jobTitle + '\'' +
                        ",email = '" + email + '\'' +
                        "}";
    }
}