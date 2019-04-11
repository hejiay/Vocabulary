package com.example.hejiayuan.vocabulary.utils;

import android.util.Log;

import com.example.hejiayuan.vocabulary.entities.WordValue;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ContentHandler extends DefaultHandler {

    public WordValue wordValue = null;
    private String tagName = null;
    private String interpret = ""; //防止空指针异常
    private String orig = "";
    private String trans = "";
    private boolean isChinese = false;

    public ContentHandler() {
        wordValue = new WordValue();
        isChinese = false;
    }

    public WordValue getWordValue() {
        return wordValue;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        if (isChinese)
            return;
        String interpret = wordValue.getInterpret();
        if (interpret != null &&interpret.length() > 0) {
            char[] strArray = interpret.toCharArray();
            wordValue.setInterpret(new String(strArray, 0, interpret.length()-1));
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        tagName = localName;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        tagName = null;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if (length <= 0)
            return;
        //去除换行
        for (int i = start; i<start+length; i++) {
            if (ch[i] == '\n') {
                return;
            }
        }

        String str = new String(ch, start,length);
        if ("key".equals(tagName)) {
            wordValue.setWord(str);
            //Log.d("DictAvtivity.this", 1 + str);
        } else if ("ps".equals(tagName)) {
            if (wordValue.getPsE().length() <= 0) {
                wordValue.setPsE(str);
               // Log.d("DictAvtivity.this", 2 + str);
            } else {
                wordValue.setPsA(str);
                //Log.d("DictAvtivity.this", 3 + str);
            }
        } else if ("pron".equals(tagName)) {
            if (wordValue.getPronE().length() <= 0) {
                wordValue.setPronE(str);
               // Log.d("DictAvtivity.this", 4 + str);
            } else {
                wordValue.setPronA(str);
                //Log.d("DictAvtivity.this", 5 + str);
            }
        } else if ("pos".equals(tagName)) {
            isChinese = false;
            interpret = interpret + str + " ";
            //Log.d("DictAvtivity.this", 6 + interpret);
        } else if ("acceptation".equals(tagName)) {
            interpret = interpret + str + "\n";
            interpret = wordValue.getInterpret() + interpret;
            wordValue.setInterpret(interpret);
            Log.d("DictAvtivity.this", 7 + interpret);
            interpret = "";//初始化操作，预防有多个释义
        } else if ("orig".equals(tagName)) {
            orig = wordValue.getSentOrig();
            wordValue.setSentOrig(orig + str + "\n");
        }  else if ("trans".equals(tagName)) {
            String temp = wordValue.getSentTrans() + str + "\n";
            wordValue.setSentTrans(temp);
            //Log.d("DictAvtivity.this", 8 + temp);
        } else if ("fy".equals(tagName)) {
            isChinese = true;
            wordValue.setInterpret(str);
        }
    }
}
