package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {

    public String name;
    public String screenName;
    public String profileImageUrl;
    public String profileBackgroundImageUrl;
    public String profileName;
    public String bio;
    public boolean verified;
    public Integer followers_count;
    public Integer friends_count;


    // empty constructor needed by the Parceler library
    public User() {}

    public static User fromJson(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.name = jsonObject.getString("name");
        user.screenName = jsonObject.getString("screen_name");
        user.profileImageUrl = jsonObject.getString("profile_image_url_https");
        user.profileBackgroundImageUrl = jsonObject.has("profile_banner_url") ? jsonObject.getString("profile_banner_url") : null;
        user.profileName = jsonObject.getString("name");
        user.verified = jsonObject.getBoolean("verified");
        user.bio = jsonObject.getString("description");
        user.followers_count = jsonObject.getInt("followers_count");
        //user.friends_count = jsonObject.getInt("friends_count");
        return user;
    }
}
