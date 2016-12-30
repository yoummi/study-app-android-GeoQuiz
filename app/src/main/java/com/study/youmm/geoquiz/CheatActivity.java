package com.study.youmm.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.youmm.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.youmm.geoquiz.answer_shown";
    private static final String STATE_IS_ANSWER_SHOWN = "com.youmm.geoquiz.is_answer_shown";    // state에 저장된 컨닝여부 키

    private static final String TAG = "cabsoft.CheatActivity";

    private boolean mAnswerIsTrue;              // 질문 답변 TRUE 여부
    private boolean mIsAnswerShown;             // 컨닝여부 저장

    private Button mShowAnswerButton;
    private TextView mAnswerTextView;

    /**
     * CheatActivity를 호출시 생성하는 Intent를 CheatActivity에서 생성하여 리턴한다.
     * @param packageContext
     * @param answerIsTrue
     * @return
     */
    public static Intent newIntent(Context packageContext,boolean answerIsTrue){
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if(null != savedInstanceState){
            mIsAnswerShown = savedInstanceState.getBoolean(STATE_IS_ANSWER_SHOWN, false);
            mAnswerIsTrue = savedInstanceState.getBoolean(EXTRA_ANSWER_IS_TRUE, false);
            showResult();
            setAnswerShownResult();
        }

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
//        Log.d(TAG, "RESULT >>>> " + mAnswerIsTrue);

        mShowAnswerButton = (Button) findViewById(R.id.showAnswerButton);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mIsAnswerShown = true;
                showResult();
                setAnswerShownResult();

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mShowAnswerButton.getWidth() / 2;
                    int cy = mShowAnswerButton.getHeight() / 2;
                    float radius = mShowAnswerButton.getWidth();
                    Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mShowAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                }else{
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(STATE_IS_ANSWER_SHOWN, mIsAnswerShown);
        savedInstanceState.putBoolean(EXTRA_ANSWER_IS_TRUE, mAnswerIsTrue);
    }

    private void showResult() {
        mAnswerTextView = (TextView) findViewById(R.id.answerTextView);

        if(mIsAnswerShown){
            if(mAnswerIsTrue){
                mAnswerTextView.setText(R.string.true_button);
            }else{
                mAnswerTextView.setText(R.string.false_button);
            }
        }

//        mIsAnswerShown = true;

    }

    /**
     * 컨닝여부를 메인에 리턴해준다.
     */
    private void setAnswerShownResult() {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, mIsAnswerShown);
        setResult(RESULT_OK, data);
    }

    /**
     * 메인에서 CheatActivity Intent를 직접 제어하지 않기 위해 CheatActivity에서 메서드 제공
     * @param result
     * @return
     */
    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "##### Cheat onStart() called !!!! ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "##### Cheat onPause() called !!!!!");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "##### Cheat onResume() called ..!!");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "##### Cheat onStop() called !!!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "##### Cheat onDestroy called !!!!!");
    }

}
