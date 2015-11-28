package io.gripit.presentation.viewmodels;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import io.gripit.presentation.BR;
import io.gripit.presentation.R;
import io.gripit.presentation.models.SurveyAnswer;
import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by krzysztofwrobel on 28/11/15.
 */
public class SurveysWrapper {
    public final ObservableList<SurveyAnswer> surveyAnswers = new ObservableArrayList<>();
    public final ItemView surveyItemView = ItemView.of(BR.survey, R.layout.survey_item);

    public void addSurveyItem(SurveyAnswer surveyAnswer) {
        if (surveyAnswers.contains(surveyAnswer)) {
            surveyAnswers.remove(surveyAnswer);
        }
        surveyAnswers.add(0, surveyAnswer);
    }
}
