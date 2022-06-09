package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcels;

import java.util.List;

import okhttp3.Headers;

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
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

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
        ImageButton btnReply;
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
            btnReply = itemView.findViewById(R.id.btnReply);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //get position
            int position = getAdapterPosition();
            //ensure position is valid
            if (position != RecyclerView.NO_POSITION) {
                //get the movie at that position in the list
                Tweet tweet = tweets.get(position);
                //create an intent to display Movie Details Activity
                Intent intent = new Intent(context, TweetDetailsActivity.class);
                //pass the tweet (possibly the original if retweeted as an extra serialized via Parcels.wrap()
                if(tweet.original_tweet != null)
                {
                    intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet.original_tweet));
                }
                else
                {
                    intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                }
                //show the activity
                context.startActivity(intent);
            }
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

            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TwitterClient client = TwitterApp.getRestClient(context);
                        client.likeTweet(tweet.id, new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                tweet.favorited = true;
                                btnLike.setVisibility(View.INVISIBLE);
                                btnLikePressed.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.e("FavoriteTweet", "failure to favorite tweet");
                            }
                        });
                    }
            });

            btnLikePressed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TwitterClient client = TwitterApp.getRestClient(context);
                    client.unlikeTweet(tweet.id, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            tweet.favorited = false;
                            btnLike.setVisibility(View.VISIBLE);
                            btnLikePressed.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e("UnFavoriteTweet", "failure to unfavorite tweet");
                        }
                    });
                }
            });

            btnRetweet.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    TwitterClient client = TwitterApp.getRestClient(context);
                    if(!tweet.retweeted) {
                        client.retweetTweet(tweet.id, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                tweet.retweeted = true;
                                btnRetweet.setColorFilter(Color.rgb(23, 191, 99));
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.e("RetweetTweet", "failure to retweet tweet");
                            }
                        });
                    }
                    else
                    {
                        client.unretweetTweet(tweet.id, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                tweet.retweeted = false;
                                btnRetweet.setColorFilter(Color.rgb(170, 184, 194));
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.e("UnRetweetTweet", "failure to unretweet tweet");
                            }
                        });
                    }
                }
            });

            btnReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ComposeActivity.class);
                    i.putExtra("reply", true);
                    i.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                    context.startActivity(i);
                }
            });
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
