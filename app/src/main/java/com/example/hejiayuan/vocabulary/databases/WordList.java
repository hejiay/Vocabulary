package com.example.hejiayuan.vocabulary.databases;

import org.litepal.crud.LitePalSupport;

public class WordList extends LitePalSupport {
    private String word;

    private String interpret;

    private int right;

    private int wrong;

    private int grasp;

    private int learned;

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

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getWrong() {
        return wrong;
    }

    public void setWrong(int wrong) {
        this.wrong = wrong;
    }

    public int getGrasp() {
        return grasp;
    }

    public void setGrasp(int grasp) {
        this.grasp = grasp;
    }

    public int getLearned() {
        return learned;
    }

    public void setLearned(int learned) {
        this.learned = learned;
    }
}
