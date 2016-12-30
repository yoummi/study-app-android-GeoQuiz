package com.study.youmm.geoquiz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mBeforeButton;
    private TextView mQuestionTextView;

    private boolean mIsCheater;
    private int mCurrentIndex = 0;

    private static final String TAG = "cabsoft.QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_QUESTION = "com.youmm.geoquiz.question";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_pocketball, false),
            new Question(R.string.question_pikachu, true),
            new Question(R.string.question_qrcode, false),
            new Question(R.string.question_rizamong, false),
            new Question(R.string.question_save, false)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);         // 파라미터로 전달받은 Bundle 객체를 슈퍼클래스에서 던져서 저장된 Bundle 객체가 있다면 받아온다.
        setContentView(R.layout.activity_quiz);

        if(null != savedInstanceState){         // Bundle 객체가 존재한다면, 값 셋팅
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);

            Parcelable[] parcelableArr = savedInstanceState.getParcelableArray(KEY_QUESTION);

            for(int index = 0; index < parcelableArr.length; index++ ){
                mQuestionBank[index] = (Question) parcelableArr[index];
                Log.d(TAG, index +  "/" + mQuestionBank[index].isCheater());
            }

            mIsCheater = mQuestionBank[mCurrentIndex].isCheater();
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mCurrentIndex++;
                if(mCurrentIndex < mQuestionBank.length) {
                    updateQuestion();
                }
            }
        });

        updateQuestion();

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // 여기서 말하는 QuizActivity.this는 View.OnClickListener의 인스턴스를 참조한다.
                checkAnswer(true);
//                Toast.makeText(QuizActivity.this, R.string.correct_toast, Toast.LENGTH_SHORT).show();
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswer(false);
//                Toast.makeText(QuizActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show();
            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View v){
                if(mCurrentIndex < mQuestionBank.length - 1 ) {
                    mCurrentIndex++;
                    mIsCheater = false;
                    updateQuestion();
                }
            }
        });

        mBeforeButton = (ImageButton) findViewById(R.id.before_button);
        mBeforeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mCurrentIndex > 0) {
                    mCurrentIndex--;
                    updateQuestion();
                }
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = CheatActivity.newIntent(QuizActivity.this, mQuestionBank[mCurrentIndex].isAnswerTrue());
//                startActivity(intent);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        // progress bar
//        addListenerOnButton();


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);    // 현재 인덱스를 저장
        savedInstanceState.putParcelableArray(KEY_QUESTION, mQuestionBank); // Question 객체 저장
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_CODE_CHEAT) {
            if(data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
            mQuestionBank[mCurrentIndex].setCheater(mIsCheater);

            Log.d(TAG, "mIsCheater : " + mIsCheater);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called !!!! ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called !!!!!");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called ..!!");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called !!!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called !!!!!");
    }


    /**
     * 질문 업데이트
     */
    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    /**
     * 질문에 대한 대답 검증
     * @param userPressedTrue
     */
    private void checkAnswer(boolean userPressedTrue){

        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        mIsCheater = mQuestionBank[mCurrentIndex].isCheater();

        int messageResId = 0;

        if(mIsCheater) {
            messageResId = R.string.judgment_toast;
        }else{
            if(userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            }else{
                messageResId = R.string.incorrect_toast;
            }
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

    }


    // progress bar

    Button btnStartProgress;
    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();


    private long fileSize = 0;

    public void addListenerOnButton() {

        btnStartProgress = (Button) findViewById(R.id.btnStartProgress);
        btnStartProgress.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        // prepare for a progress bar dialog
                        progressBar = new ProgressDialog(v.getContext());
                        progressBar.setCancelable(true);
                        progressBar.setMessage("Progress Bar move move!");
                        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressBar.setProgress(0);
                        progressBar.setMax(100);
                        progressBar.show();

                        //reset progress bar status
                        progressBarStatus = 0;

                        //reset filesize
                        fileSize = 0;

                        new Thread(new Runnable() {
                            public void run() {
                                while (progressBarStatus < 100) {

                                    // process some tasks
                                    progressBarStatus = doSomeTasks();

                                    // your computer is too fast, sleep 1 second
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    // Update the progress bar
                                    progressBarHandler.post(new Runnable() {
                                        public void run() {
                                            progressBar.setProgress(progressBarStatus);
                                        }
                                    });
                                }

                                // ok, file is downloaded,
                                if (progressBarStatus >= 100) {

                                    // sleep 2 seconds, so that you can see the 100%
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    // close the progress bar dialog
                                    progressBar.dismiss();
                                }
                            }
                        }).start();

                    }

                });

    }

    // file download simulator... a really simple
    public int doSomeTasks() {

        while (fileSize <= 1000000) {

            fileSize++;

            if (fileSize == 100000) {
                return 10;
            } else if (fileSize == 200000) {
                return 20;
            } else if (fileSize == 300000) {
                return 30;
            }
            // ...add your own

        }

        return 100;

    }

}