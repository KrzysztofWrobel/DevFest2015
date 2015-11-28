package io.gripit.presentation.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.gripit.presentation.R;


public class GripitActivity extends AppCompatActivity {
    @InjectView(R.id.tv_gripit_twitter)
    TextView gripitTwitterTextView;
    @InjectView(R.id.tv_presenter_twitter)
    TextView authorTwitterTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gripit);
        ButterKnife.inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Here we have Gripit logo and links to our website
        gripitTwitterTextView.setOnClickListener(v -> {
            openTwitter("@Gripit_IO");
        });
        authorTwitterTextView.setOnClickListener(v -> {
            openTwitter("@MrChrisWrobel");
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openTwitter(String twitterUserName) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + twitterUserName)));
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/" + twitterUserName)));
        }
    }
}
