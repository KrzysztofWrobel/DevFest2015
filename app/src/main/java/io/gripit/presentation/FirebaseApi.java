package io.gripit.presentation;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.Query;

import io.gripit.presentation.models.SurveyAnswer;
import io.gripit.presentation.utils.RxFirebase;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by krzysztofwrobel on 28/11/15.
 */
public class FirebaseApi {
    private static final String BASE_URL = "https://wawdevfest2015.firebaseio.com/";
    private static final String SURVEY_PUBLIC_SCHEMA = "surveys";

    public static Firebase getFirebaseSurveys() {
        return new Firebase(BASE_URL).child(SURVEY_PUBLIC_SCHEMA);
    }

    public static Query getLastSurveyQuery() {
        Firebase root = getFirebaseSurveys();

        return root.limitToLast(1);
    }

    public static Observable<Long> getSurveysCount() {
        return RxFirebase.observe(getFirebaseSurveys()).flatMap(dataSnapshot -> Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                long childrenCount = dataSnapshot.getChildrenCount();
                subscriber.onNext(childrenCount);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()));
    }

    public static Observable<SurveyAnswer> getSurveyAnswers() {
        return RxFirebase.observeSingle(getFirebaseSurveys()).flatMap(snapshot -> Observable.create(new Observable.OnSubscribe<SurveyAnswer>() {
            @Override
            public void call(Subscriber<? super SurveyAnswer> subscriber) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    SurveyAnswer surveyAnswer = child.getValue(SurveyAnswer.class);
                    surveyAnswer.setId(child.getKey());
                    subscriber.onNext(surveyAnswer);
                }
                subscriber.onCompleted();

            }
        }));
    }

    public static Observable<SurveyAnswer> observeNewSurveyAnswers() {
        Firebase root = getFirebaseSurveys();
        return RxFirebase.observeChildAdded(root).flatMap(firebaseEvent -> Observable.create(new Observable.OnSubscribe<SurveyAnswer>() {
            @Override
            public void call(Subscriber<? super SurveyAnswer> subscriber) {
                DataSnapshot child = firebaseEvent.snapshot;
                SurveyAnswer surveyAnswer = child.getValue(SurveyAnswer.class);
                surveyAnswer.setId(child.getKey());
                subscriber.onNext(surveyAnswer);
            }
        }).subscribeOn(Schedulers.io()));
    }
}
