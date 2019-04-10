package com.example.hejiayuan.vocabulary.entities;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

public class WordValue {
    public String word = null;
    public String psE = null;
    public String pronE = null;
    public String psA = null;
    public String pronA = null;
    public String interpret = null;
    public String sentOrig = null;
    public String sentTrans = null;

    public WordValue(String word, String psE, String pronE, String psA, String pronA,
                     String interpret, String sentOrig, String sentTrans) {
        this.word = word;
        this.psE = psE;
        this.pronE = pronE;
        this.psA = psA;
        this.pronA = pronA;
        this.interpret = interpret;
        this.sentOrig = sentOrig;
        this.sentTrans = sentTrans;
    }

    public WordValue() {
        super();
        this.word = "";
        this.psE = "";
        this.pronE = "";
        this.psA = "";
        this.pronA = "";
        this.interpret = "";
        this.sentOrig = "";
        this.sentTrans = "";
    }

    public ArrayList<String> getOrigList(){
        ArrayList<String> list=new ArrayList<String>();
        BufferedReader br=new BufferedReader(new StringReader(this.sentOrig));
        String str=null;
        try{
            while((str=br.readLine())!=null){
                list.add(str);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<String> getTransList(){
        ArrayList<String> list=new ArrayList<String>();
        BufferedReader br=new BufferedReader(new StringReader(this.sentTrans));
        String str=null;
        try{
            while((str=br.readLine())!=null){
                list.add(str);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public String getWord() {

        return word;
    }

    public void setWord(String word) {

        this.word = word;
    }

    public String getPsE() {

        return psE;
    }

    public void setPsE(String psE) {

        this.psE = psE;
    }

    public String getPronE() {

        return pronE;
    }

    public void setPronE(String pronE) {

        this.pronE = pronE;
    }

    public String getPsA() {

        return psA;
    }

    public void setPsA(String psA) {

        this.psA = psA;
    }

    public String getPronA() {

        return pronA;
    }

    public void setPronA(String pronA) {

        this.pronA = pronA;
    }

    public String getInterpret() {
        return interpret;
    }

    public void setInterpret(String interpret) {
        this.interpret = interpret;
    }

    public String getSentOrig() {
        return sentOrig;
    }

    public void setSentOrig(String sentOrig) {
        this.sentOrig = sentOrig;
    }

    public String getSentTrans() {
        return sentTrans;
    }

    public void setSentTrans(String sentTrans) {
        this.sentTrans = sentTrans;
    }
}
