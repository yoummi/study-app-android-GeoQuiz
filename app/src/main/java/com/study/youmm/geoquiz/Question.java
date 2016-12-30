package com.study.youmm.geoquiz;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by youmm on 2016-12-29.
 */

public class Question implements  Parcelable{

    private int mTextResId;
    private boolean mAnswerTrue;        // 질문 정답
    private boolean mIsCheater;     // 컨닝 여부

    public Question(int textResId, boolean answerTrue){
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
        mIsCheater = false;
    }

    protected Question(Parcel in) {
        mTextResId = in.readInt();
        mAnswerTrue = in.readByte() != 0;
        mIsCheater = in.readByte() != 0;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public boolean isCheater() {
        return mIsCheater;
    }

    public void setCheater(boolean cheater) {
        mIsCheater = cheater;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mTextResId);
        parcel.writeByte((byte) (mAnswerTrue ? 1 : 0));
        parcel.writeByte((byte) (mIsCheater ? 1 : 0));
    }
}
