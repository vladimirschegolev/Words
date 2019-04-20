package com.gmail.randzjx.words.database;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import androidx.annotation.NonNull;

public class Word implements Comparable<Word>, Parcelable {

    private String word, firstDescription, secondDescription;
    private int numTries = 0;
    private Date date;
    private int numGuessed = 0;

    public Word(String word) {
        this.word = word;
        date = new Date();
    }

    public Word() {
        word = "";
        date = new Date();
    }

    public Word(String sWord, Date date) {
        word = sWord;
        this.date = date;
    }

    protected Word(Parcel in) {
        word = in.readString();
        firstDescription = in.readString();
        secondDescription = in.readString();
        numTries = in.readInt();
        date = new Date(in.readLong());
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
        date.setTime(System.currentTimeMillis());
    }

    public String getFirstDescription() {
        return firstDescription;
    }

    public void setFirstDescription(String firstDescription) {
        this.firstDescription = firstDescription;
    }

    public void updateFirstDescription(String firstDescription) {
        this.firstDescription = firstDescription;
        date.setTime(System.currentTimeMillis());
    }

    public String getSecondDescription() {
        return secondDescription;
    }

    public void setSecondDescription(String secondDescription) {
        this.secondDescription = secondDescription;
    }

    public void updateSecondDescription(String secondDescription) {
        this.secondDescription = secondDescription;
        date.setTime(System.currentTimeMillis());
    }


    @Override
    public int compareTo(Word o) {
        return word.compareTo(o.word);
    }

    public int getNumTries() {
        return numTries;
    }

    public void addTry() {
        numTries++;
        date.setTime(System.currentTimeMillis());
    }

    public void guessed() {
        numTries++;
        numGuessed++;
        date.setTime(System.currentTimeMillis());
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDate(long time) {
        date.setTime(time);
    }

    public void setNumTries(int numTries) {
        this.numTries = numTries;
    }


    @Override
    public int describeContents() {
        return word.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(word);
        dest.writeString(firstDescription);
        dest.writeString(secondDescription);
        dest.writeInt(numTries);
        dest.writeLong(date.getTime());
    }

    public static final Creator<Word> CREATOR = new Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

    public void upDate() {
        date.setTime(System.currentTimeMillis());
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(word).append(" ").append(firstDescription).append(" ")
                .append(secondDescription).append(" ")
                .append("tries ").append(numTries).append(" ")
                .append("guessed ").append(numGuessed).append(" ")
                .append(date);
        return sb.toString();
    }

    public void setNumGuessed(int numGuessed) {
        this.numGuessed = numGuessed;
    }

    public int getNumGuessed() {
        return numGuessed;
    }
}
