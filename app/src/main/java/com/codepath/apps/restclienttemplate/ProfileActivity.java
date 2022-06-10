package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.databinding.ActivityProfileBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;
import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    ImageView ivProfilePic;
    ImageView ivProfileBackground;
    TextView tvScreenName;
    TextView tvName;
    TextView tvBio;
    TextView tvFollowerCount;
    TextView tvFriendCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ivProfilePic = binding.ivProfilePhoto;
        ivProfileBackground = binding.ivProfileBackground;
        tvScreenName = binding.tvProfileScreenName;
        tvName = binding.tvProfileName1;
        tvBio = binding.tvProfileBio;
        tvFollowerCount = binding.tvFollowersCount;
        tvFriendCount = binding.tvFriendsCount;

        User user = Parcels.unwrap(getIntent().getParcelableExtra(User.class.getSimpleName()));

        Glide.with(ProfileActivity.this).load(user.profileImageUrl).transform(new RoundedCorners(90)).into(ivProfilePic);
        if(user.profileBackgroundImageUrl != null) {
            Glide.with(ProfileActivity.this).load(user.profileBackgroundImageUrl).override(500, 500).centerCrop().transform(new RoundedCorners(30)).into(ivProfileBackground);
        }
        tvScreenName.setText("@" + user.screenName);
        tvName.setText(user.name);
        tvBio.setText(user.bio);
        tvFollowerCount.setText(String.valueOf(user.followers_count) + " Followers");
        tvFriendCount.setText(String.valueOf(user.friends_count) + " Friends");
    }
}