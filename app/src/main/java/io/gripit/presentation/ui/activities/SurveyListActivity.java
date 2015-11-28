package io.gripit.presentation.ui.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.MenuItem;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import io.gripit.presentation.FirebaseApi;
import io.gripit.presentation.R;
import io.gripit.presentation.databinding.ActivitySurveysListBinding;
import io.gripit.presentation.viewmodels.SurveysWrapper;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class SurveyListActivity extends RxAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActivitySurveysListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_surveys_list);
        SurveysWrapper surveysWrapper = new SurveysWrapper();
        binding.setSurveysWrapper(surveysWrapper);

        FirebaseApi.observeNewSurveyAnswers()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(surveyAnswer -> {
                    surveysWrapper.addSurveyItem(surveyAnswer);
                }, throwable -> {
                    Timber.e("Error: ", throwable);
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
