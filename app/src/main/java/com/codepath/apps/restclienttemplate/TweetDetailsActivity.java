package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.databinding.ActivityTweetDetailsBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TweetDetailsActivity extends AppCompatActivity {

    private ActivityTweetDetailsBinding binding;

    ImageView ivProfile;
    ImageView ivMedia;
    ImageView ivVerified;
    TextView tvUserName;
    TextView tvName;
    TextView tvFullText;
    TextView tvTime;
    TextView tvDate;
    TextView tvRetweets;
    TextView tvFavorites;
    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTweetDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ivProfile = binding.ivProfile;
        ivMedia = binding.ivMediaContent;
        tvUserName = binding.tvUsername;
        tvName = binding.tvName;
        tvFullText = binding.tvTweetBody;
        tvTime = binding.tvTimePosted;
        tvDate = binding.tvDatePosted;
        tvRetweets = binding.tvRetweets;
        tvFavorites = binding.tvFavorites;
        ivVerified = binding.ivVerified2;

        tweet = Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        Glide.with(TweetDetailsActivity.this).load(tweet.user.profileImageUrl).transform(new RoundedCorners(90)).into(ivProfile);
        if (tweet.media != null) {
            Glide.with(TweetDetailsActivity.this).load(tweet.media).override(600, 600).centerCrop().transform(new RoundedCorners(30)).into(ivMedia);
            ivMedia.setVisibility(View.VISIBLE);
        } else {
            ivMedia.setImageURI(null);
            ivMedia.setVisibility(View.GONE);
        }

        tvFullText.setText(tweet.body);
        tvName.setText(tweet.user.name);
        tvUserName.setText("@" + tweet.user.screenName);
        tvDate.setText(tweet.createdAt.substring(4, 7) + "/" + tweet.createdAt.substring(8, 10)  + "/" + tweet.createdAt.substring(28, 30));
        tvTime.setText(tweet.createdAt.substring(11, 16));
        tvRetweets.setText(String.valueOf(tweet.retweet_count + " Retweets"));
        tvFavorites.setText(tweet.favorite_count + " Likes");

        if (tweet.user.verified) {
            ivVerified.setVisibility(View.VISIBLE);
        } else {
            ivVerified.setVisibility(View.GONE);
        }
    }
}