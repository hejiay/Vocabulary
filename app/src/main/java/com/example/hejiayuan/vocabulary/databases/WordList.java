package com.example.hejiayuan.vocabulary.databases;

import org.litepal.crud.LitePalSupport;

public class WordList extends LitePalSupport {
    private String word;

    private String interpret;

    private int learnTimes;

    private int unfamiliarTimes;

    public WordList() {

    }

    public WordList(String word, String interpret) {
        this.word = word;
        this.interpret = interpret;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getInterpret() {
        return interpret;
    }

    public void setInterpret(String interpret) {
        this.interpret = interpret;
    }

    public int getLearnTimes() {
        return learnTimes;
    }

    public void setLearnTimes(int learnTimes) {
        this.learnTimes = learnTimes;
    }

    public int getUnfamiliarTimes() {
        return unfamiliarTimes;
    }

    public void setUnfamiliarTimes(int unfamiliarTimes) {
        this.unfamiliarTimes = unfamiliarTimes;
    }
}
