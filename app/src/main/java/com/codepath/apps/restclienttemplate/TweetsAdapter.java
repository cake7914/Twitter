package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    Context context;
    List<Tweet> tweets;

    // Pass in the context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    // For each row, inflate the layout for a tweet
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data at the position
        Tweet tweet = tweets.get(position);
        // Bind the tweet with the View Holder
        holder.bind(tweet);
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    public void add(Tweet tweet) {
        tweets.add(tweet);
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

    // Define a View Holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        ImageView ivMedia;
        TextView tvProfileName;
        TextView tvTimeStamp;
        TextView tvReplyCount;
        TextView tvRetweetCount;
        TextView tvFavoriteCount;
        ImageButton btnRetweet;
        ImageButton btnLike;
        ImageButton btnLikePressed;
        ImageView ivVerified;
        TextView tvRetweetedStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            ivMedia = itemView.findViewById(R.id.ivMedia);
            tvProfileName = itemView.findViewById(R.id.tvProfileName);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
            tvReplyCount = itemView.findViewById(R.id.tvReplyCount);
            tvRetweetCount = itemView.findViewById(R.id.tvRetweetCount);
            tvFavoriteCount = itemView.findViewById(R.id.tvFavoriteCount);
            btnRetweet = itemView.findViewById(R.id.btnRetweet);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnLikePressed = itemView.findViewById(R.id.btnLikePressed);
            ivVerified = itemView.findViewById(R.id.ivVerified);
            tvRetweetedStatus = itemView.findViewById(R.id.tvRetweetedStatus);
        }

        public void bindTweet(Tweet tweet)
        {
            tvBody.setText(tweet.body);
            // if name is too long, put ... on the end so that everything can fit
            if(tweet.user.name.length() > 20)
            {
                tvProfileName.setText(tweet.user.name.substring(0, 15) + "...");
            }
            else
            {
                tvProfileName.setText(tweet.user.name);
            }
            tvScreenName.setText("@" + tweet.user.screenName + " •");
            tvTimeStamp.setText(tweet.time_stamp);

            tvRetweetCount.setText(String.valueOf(tweet.retweet_count));
            if (tweet.retweeted) {
                btnRetweet.setColorFilter(Color.rgb(23, 191, 99));
            } else {
                btnRetweet.setColorFilter(Color.rgb(170, 184, 194));
            }

            tvFavoriteCount.setText(String.valueOf(tweet.favorite_count));
            if (tweet.favorited) {
                btnLike.setVisibility(View.INVISIBLE);
                btnLikePressed.setVisibility(View.VISIBLE);
            } else {
                btnLike.setVisibility(View.VISIBLE);
                btnLikePressed.setVisibility(View.INVISIBLE);
            }

            if (tweet.user.verified) {
                ivVerified.setVisibility(View.VISIBLE);
            } else {
                ivVerified.setVisibility(View.GONE);
            }

            Glide.with(context).load(tweet.user.profileImageUrl).transform(new RoundedCorners(90)).into(ivProfileImage);
            if (tweet.media != null) {
                Glide.with(context).load(tweet.media).override(500, 500).centerCrop().transform(new RoundedCorners(30)).into(ivMedia);
                ivMedia.setVisibility(View.VISIBLE);
            } else {
                ivMedia.setImageURI(null);
                ivMedia.setVisibility(View.GONE);
            }
        }


        public void bind(Tweet tweet) {
            //this is a retweet
            if(tweet.original_tweet != null)
            {
                bindTweet(tweet.original_tweet);
                // set retweet user above the original tweet
                tvRetweetedStatus.setText("\uD83D\uDD01" + tweet.user.name + " Retweeted");
                tvRetweetedStatus.setVisibility(View.VISIBLE);
            }
            else // this is a normal tweet
            {
                bindTweet(tweet);
                tvRetweetedStatus.setVisibility(View.GONE);
            }
        }
    }
}
