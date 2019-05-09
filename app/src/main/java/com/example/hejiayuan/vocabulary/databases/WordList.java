package com.example.hejiayuan.vocabulary.databases;


public class WordList {
    private String word;

    private String interpret;

    private int grasp;
    public WordList() {

    }

    public WordList(String word, String interpret) {
        this.word = word;
        this.interpret = interpret;
    }

    public WordList(String word, String interpret, int grasp) {
        this.word = word;
        this.interpret = interpret;
        this.grasp = grasp;
    }

    public int getGrasp() {
        return grasp;
    }

    public void setGrasp(int grasp) {
        this.grasp = grasp;
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

}
