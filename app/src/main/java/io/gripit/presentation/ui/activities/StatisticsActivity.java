package io.gripit.presentation.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.gripit.presentation.FirebaseApi;
import io.gripit.presentation.R;
import io.gripit.presentation.models.SurveyAnswer;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;


public class StatisticsActivity extends RxAppCompatActivity {
    String[] STUDENTS = new String[]{"Tak", "Nie"};
    String[] STARTUPERS = new String[]{"Tak", "Nie", "Zamierzam"};
    String[] INTERESTS = new String[]{
            "Startup lifestyle",
            "Business",
            "Marketing",
            "Design & UX",
            "Programming",
            "User research"
    };
    HashMap<String, Integer> interestsValues = new HashMap<>();
    HashMap<String, Integer> studentsValues = new HashMap<>();
    HashMap<String, Integer> startupValues = new HashMap<>();

    @InjectView(R.id.tv_surveys)
    TextView surveysTextView;
    @InjectView(R.id.pcv_students)
    PieChartView studentsPieChartView;
    @InjectView(R.id.pcv_startupers)
    PieChartView startupersPieChartView;
    @InjectView(R.id.ccv_interests)
    ColumnChartView interestsChartView;
    private boolean hasLabels = true;
    private PieChartData studentData;
    private PieChartData startupData;
    private ColumnChartData interestData;

    private int surveyCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);

        ButterKnife.inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        surveyCount = 0;

        FirebaseApi.observeNewSurveyAnswers()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(surveyAnswer -> {
                    addSurveyItem(surveyAnswer);
                }, throwable -> {
                    Timber.e("Error: ", throwable);
                });

        generateStudentData();
        generateStartupersData();
        generateInterestDefaultData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void addSurveyItem(SurveyAnswer surveyAnswer) {
        surveyCount++;
        surveysTextView.setText(String.format("Udzielonych ankiet: %d", surveyCount));

        String student = surveyAnswer.getStudent();
        Integer value = studentsValues.get(student);
        studentsValues.put(student, value + 1);

        String startup = surveyAnswer.getStartup();
        Integer value1 = startupValues.get(startup);
        startupValues.put(startup, value1 + 1);

        String interest = surveyAnswer.getInterest();
        Integer value2 = interestsValues.get(interest);
        interestsValues.put(interest, value2 + 1);

        prepareStudentDataAnimation();
        prepareStartupDataAnimation();
        prepareInterestDataAnimation();
    }

    private void generateStudentData() {
        List<SliceValue> values = new ArrayList<>();
        for (String label : STUDENTS) {
            studentsValues.put(label, 0);
            SliceValue sliceValue = new SliceValue(0, ChartUtils.nextColor());
            sliceValue.setLabel(label);
            values.add(sliceValue);
        }

        studentData = new PieChartData(values);
        studentData.setHasLabels(hasLabels);
        studentData.setHasLabelsOutside(false);
        studentData.setHasCenterCircle(true);
        studentData.setSlicesSpacing(8);
        studentData.setCenterText1("Student");

        studentsPieChartView.setPieChartData(studentData);
    }

    private void generateStartupersData() {
        List<SliceValue> values = new ArrayList<>();
        for (String label : STARTUPERS) {
            startupValues.put(label, 0);
            SliceValue sliceValue = new SliceValue(0, ChartUtils.nextColor());
            sliceValue.setLabel(label);
            values.add(sliceValue);
        }

        startupData = new PieChartData(values);
        startupData.setHasLabels(hasLabels);
        startupData.setHasLabelsOutside(false);
        startupData.setHasCenterCircle(true);
        studentData.setSlicesSpacing(8);
        startupData.setCenterText1("Startup");

        startupersPieChartView.setPieChartData(startupData);
    }

    private void generateInterestDefaultData() {
        int numSubcolumns = 1;
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> values;
        for (String interest : INTERESTS) {
            interestsValues.put(interest, 0);

            values = new ArrayList<>();
            for (int j = 0; j < numSubcolumns; ++j) {
                SubcolumnValue value = new SubcolumnValue(0, ChartUtils.nextColor());
                value.setLabel(interest);
                values.add(value);
            }

            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            columns.add(column);
        }

        interestData = new ColumnChartData(columns);

        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
        axisX.setName("Interest");
        axisY.setName("% of surveys");
        interestData.setAxisXBottom(axisX);
        interestData.setAxisYLeft(axisY);

        interestsChartView.setColumnChartData(interestData);

    }

    private void prepareInterestDataAnimation() {
        List<Column> columns = interestData.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            for (SubcolumnValue value : column.getValues()) {
                Integer count = interestsValues.get(INTERESTS[i]);
                value.setTarget(((float) count) / surveyCount * 100);
            }
        }
        interestsChartView.startDataAnimation(500);
    }

    private void prepareStudentDataAnimation() {
        List<SliceValue> values = studentData.getValues();
        for (int i = 0; i < values.size(); i++) {
            SliceValue value = values.get(i);
            Integer count = studentsValues.get(STUDENTS[i]);
            value.setTarget(((float) count) / surveyCount * 100);
        }

        studentsPieChartView.startDataAnimation(500);
    }

    private void prepareStartupDataAnimation() {
        List<SliceValue> values = startupData.getValues();
        for (int i = 0; i < values.size(); i++) {
            SliceValue value = values.get(i);
            Integer count = startupValues.get(STARTUPERS[i]);
            value.setTarget(((float) count) / surveyCount * 100);
        }

        startupersPieChartView.startDataAnimation(500);
    }

}
