package io.gripit.presentation.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.gripit.presentation.FirebaseApi;
import io.gripit.presentation.R;
import io.gripit.presentation.models.SurveyAnswer;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;


public class MainActivity extends RxAppCompatActivity {
    @InjectView(R.id.iv_logo)
    ImageView logoImageView;
    @InjectView(R.id.tv_last_survey_text)
    TextView lastSurveyTextView;
    @InjectView(R.id.b_take_survey)
    Button takeSurveyButton;
    @InjectView(R.id.b_show_answers)
    Button showAnswersButton;
    @InjectView(R.id.b_statistics)
    Button statisticsButton;
    @InjectView(R.id.b_about)
    Button aboutGripItButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        setupButtonsListeners();

        //Load DevFest logo with Picasso
        Picasso.with(this).load(getString(R.string.devfest_logo_url)).into(logoImageView);

        //Observe last survey from Firebase normally
        FirebaseApi.getLastSurveyQuery().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChild) {
                SurveyAnswer survey = dataSnapshot.getValue(SurveyAnswer.class);
                bindLastSurveyResponse(survey);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //Observe survey count using RxJava
        FirebaseApi.getSurveysCount()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(surveyCount -> {
                    showAnswersButton.setText(String.format(getString(R.string.show_answers_format), surveyCount));
                }, throwable -> {
                    Timber.e("Error: ", throwable);
                });
    }

    private void setupButtonsListeners() {
        takeSurveyButton.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://goo.gl/forms/lJFxjm6PFx"));
            startActivity(browserIntent);
        });
        showAnswersButton.setOnClickListener(v -> {
            Intent showAnswersIntent = new Intent(this, SurveyListActivity.class);
            startActivity(showAnswersIntent);
        });

        statisticsButton.setOnClickListener(v -> {
            Intent statisticsIntent = new Intent(this, StatisticsActivity.class);
            startActivity(statisticsIntent);
        });

        aboutGripItButton.setOnClickListener(v -> {
            Intent aboutGripItIntent = new Intent(this, GripitActivity.class);
            startActivity(aboutGripItIntent);
        });
    }

    private void bindLastSurveyResponse(SurveyAnswer survey) {
        String studentText = survey.getStudentBlock();
        String startupText = survey.getStartupBlock();

        lastSurveyTextView.setText(String.format(getString(R.string.survey_format),
                survey.getTime(), survey.getName(), studentText, startupText, survey.getInterest()));
    }

}
