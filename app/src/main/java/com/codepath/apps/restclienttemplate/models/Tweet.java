package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Parcel
public class Tweet {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public String body;
    public String createdAt;
    public User user;
    public String media;
    public String time_stamp;
    public Long id;
    public Integer favorite_count;
    public Integer retweet_count;
    public Integer reply_count;
    public boolean favorited;
    public boolean retweeted;
    public Tweet original_tweet;

    // empty constructor needed by the Parceler library
    public Tweet() {}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        if(jsonObject.has("full_text")) {
            tweet.body = jsonObject.getString("full_text");
        } else {
            tweet.body = jsonObject.getString("text");
        }
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.id = jsonObject.getLong("id");

        // Get the entities object
        JSONObject entitiesObject = jsonObject.getJSONObject("entities");
        // Get the array of media objects
        JSONArray mediaArray = entitiesObject.has("media") ? entitiesObject.getJSONArray("media") : null;
        // Get the first object within the media array
        JSONObject mediaObject  = mediaArray != null ? mediaArray.getJSONObject(0): null;
        // get the url for the image
        tweet.media = mediaObject != null ? mediaObject.getString("media_url_https") : null;

        //Setting the time stamp
        tweet.time_stamp = getRelativeTimeStamp(tweet.createdAt);

        // Setting the various quantitative aspects of the tweet
        tweet.retweet_count = jsonObject.getInt("retweet_count");
        tweet.favorite_count = jsonObject.getInt("favorite_count");
        //tweet.reply_count = jsonObject.getInt("reply_count");
        tweet.favorited = jsonObject.getBoolean("favorited");
        tweet.retweeted = jsonObject.getBoolean("retweeted");

        if (jsonObject.has("retweeted_status"))
        {
            tweet.original_tweet = Tweet.fromJson(jsonObject.getJSONObject("retweeted_status"));
        }
        else
        {
            tweet.original_tweet = null;
        }

        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++)
        {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

    public static String getRelativeTimeStamp(String jsonCreatedAt)
    {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sfDate = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sfDate.setLenient(true);

        try {
            long timeStamp = sfDate.parse(jsonCreatedAt).getTime();
            long now = System.currentTimeMillis();
            final long diff = now - timeStamp;

            if(diff < MINUTE_MILLIS)
            {
                return "just now";
            }
            else if (diff < 2 * MINUTE_MILLIS)
            {
                return "a minute ago";
            }
            else if (diff < 50 * MINUTE_MILLIS)
            {
                return diff / MINUTE_MILLIS + "m";
            }
            else if (diff < 90 * MINUTE_MILLIS)
            {
                return "an hour ago";
            }
            else if (diff < 24 * HOUR_MILLIS)
            {
                return diff / HOUR_MILLIS + "h";
            }
            else if (diff < 48 * HOUR_MILLIS)
            {
                return "yesterday";
            }
            else
            {
                return diff / DAY_MILLIS + "d";
            }
        }
        catch (ParseException e)
        {
            Log.i("Tweet", "getRelativeTimeStamp failed");
            e.printStackTrace();
        }
        return "";
    }


}
