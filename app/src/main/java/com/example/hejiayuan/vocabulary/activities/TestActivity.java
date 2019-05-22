package com.example.hejiayuan.vocabulary.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hejiayuan.vocabulary.R;
import com.example.hejiayuan.vocabulary.databases.DataBaseHelperReview;
import com.example.hejiayuan.vocabulary.utils.Log;
import com.example.hejiayuan.vocabulary.utils.MyApplication;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;

public class TestActivity extends AppCompatActivity {

    TextView totalText;
    TextView knowText;
    TextView errorText;
    TextView unknowText;
    TextView testWordText;
    TextView accuracyText;
    TextView testChose1;
    TextView testChose2;
    TextView testChose3;
    TextView testChose4;
    TextView testChose5;

    public  DataBaseHelperReview dbHelper;
    SQLiteDatabase dbR;

    String []mathch = {"a%","b%","c%","d%","e%","f%","g%","h%","i%","j%","k%"
            ,"l%","m%","n%","o%","p%","q%" ,"r%","s%","t%","u%","v%","w%",
            "x%","y%","z%"};

    ArrayList<String> testLists = new ArrayList<>();

    Random random = new Random();
    Random randomChose = new Random();
    int choseInt = 0;
    int trueTiems = 0;
    int falseTimes = 0;
    int unknowTimes = 0;

    Cursor cursorOfTest;;

    ArrayList<String> interpretList = new ArrayList<>();

    public static int COUNT = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        init();
        cursorOfTest = dbR.query("reviewlist", new String[]{"word","interpret"}, null, null
        ,null,null,null);
        COUNT = cursorOfTest.getCount();
        getWord();
        setOnClick();
    }

    public void init() {
        totalText = (TextView)findViewById(R.id.id_text_total);
        knowText = (TextView)findViewById(R.id.id_test_know);
        errorText = (TextView)findViewById(R.id.id_test_error);
        unknowText = (TextView)findViewById(R.id.id_test_unknow);
        testWordText = (TextView) findViewById(R.id.id_test_word_text);
        accuracyText = (TextView) findViewById(R.id.id_test_accuracy_text);
        dbHelper = new DataBaseHelperReview(this, "reviewlist");
        dbR = dbHelper.getReadableDatabase();
        testChose1 = (TextView) findViewById(R.id.id_test_chose_1);
        testChose2 = (TextView) findViewById(R.id.id_test_chose_2);
        testChose3 = (TextView) findViewById(R.id.id_test_chose_3);
        testChose4 = (TextView) findViewById(R.id.id_test_chose_4);
        testChose5 = (TextView) findViewById(R.id.id_test_chose_5);
    }

    public void getWord() {
        String word = null;
        String interpret = null;
        do {
            int index = random.nextInt(COUNT);
            cursorOfTest.moveToPosition(-1);
            cursorOfTest.moveToPosition(index);
            word = cursorOfTest.getString(cursorOfTest.getColumnIndex("word"));
            interpret = cursorOfTest.getString(cursorOfTest.getColumnIndex("interpret"));
            if (testLists.size() >= COUNT - 1) {
                Toast.makeText(this, "单词本单词已测试完，退出测试", Toast.LENGTH_SHORT).show();
                TestActivity.this.finish();
            }
        } while (testLists.contains(word) == true || interpretList.contains(interpret) == true || "".equals(word) || "".equals(interpret));
        testLists.add(word);
        setData(word, interpret);
    }

    public String[] getInterprets() {
        String interpret;
        String splitInterpret_1[] = new String[3];
        String splitInterpret_2[] = new String[3];
        String splitInterpret_3[] = new String[3];
        String[] firstInterpret = new String[3];
        for (int j = 0; j < 3; j++) {
            do {
                int index = random.nextInt(COUNT);
                cursorOfTest.moveToPosition(-1);
                cursorOfTest.moveToPosition(index);
                interpret = cursorOfTest.getString(cursorOfTest.getColumnIndex("interpret"));
            } while (interpretList.contains(interpret) == true || "".equals(interpret));
            interpretList.add(interpret);
        }
            splitInterpret_1 = interpretList.get(0).split("；");
            splitInterpret_2 = interpretList.get(1).split("；");
            splitInterpret_3 = interpretList.get(2).split("；");
            interpretList.clear();
            firstInterpret[0] = splitInterpret_1[0];
            firstInterpret[1] = splitInterpret_2[0];
            firstInterpret[2] = splitInterpret_3[0];
            return firstInterpret;
        }

    public void setData(String word, String interpret) {
        testWordText.setText(word);
        String[] firstInterpret = new String[5];
        try {
            firstInterpret =  interpret.split("；");
        } catch (Exception e) {
            e.printStackTrace();
            firstInterpret = new String[]{""};
        } finally {
            choseInt = randomChose.nextInt(4);
            switch (choseInt) {
                case 0:
                    setFalseChose(getInterprets(), 0);
                    testChose1.setText( "  A  " + firstInterpret[0]);
                    break;
                case 1:
                    setFalseChose(getInterprets(), 1);
                    testChose2.setText("  B  " + firstInterpret[0]);
                    break;
                case 2:
                    setFalseChose(getInterprets(), 2);
                    testChose3.setText("  C  " + firstInterpret[0]);
                    break;
                case 3:
                    setFalseChose(getInterprets(), 3);
                    testChose4.setText("  D  " + firstInterpret[0]);
                    break;
                default:
                    break;
            }
        }
    }

    public void setFalseChose(String []interpret, int trueChose) {
        if (trueChose == 0) {
            testChose2.setText("  B  " + interpret[0] + "");
            testChose3.setText("  C  " + interpret[1] + "");
            testChose4.setText("  D  " + interpret[2] + "");
        } else if (trueChose == 1) {
            testChose1.setText("  A  " + interpret[0] + "");
            testChose3.setText("  C  " + interpret[1] + "");
            testChose4.setText("  D  " + interpret[2] + "");
        } else if (trueChose == 2) {
            testChose1.setText("  A  " + interpret[0] + "");
            testChose2.setText("  B  " + interpret[1] + "");
            testChose4.setText("  D  " + interpret[2] + "");
        } else if (trueChose == 3) {
            testChose1.setText("  A  " + interpret[0] + "");
            testChose2.setText("  B  " + interpret[1] + "");
            testChose3.setText("  C  " + interpret[2] + "");
        }
    }

    public void clearText() {
        testWordText.setText("");
        testChose1.setText("");
        testChose2.setText("");
        testChose3.setText("");
        testChose4.setText("");
    }

    public void setOnClick() {

        testChose1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testTrue(0);
                updateTimes();
                clearText();
                getWord();
            }
        });

        testChose2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testTrue(1);
                updateTimes();
                clearText();
                getWord();
            }
        });

        testChose3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testTrue(2);
                updateTimes();
                clearText();
                getWord();
            }
        });

        testChose4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testTrue(3);
                updateTimes();
                clearText();
                getWord();
            }
        });

        testChose5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testTrue(4);
                updateTimes();
                clearText();
                getWord();
            }
        });
    }

    public void showAddDialog() {

        double accuracy = trueTiems / 13.0 * 100;
        BigDecimal bigDecimal = new BigDecimal(accuracy);
        accuracy = bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        AlertDialog.Builder dialog = new AlertDialog.Builder(TestActivity.this);
        dialog.setIcon(R.mipmap.dialog);
        dialog.setTitle("测试完成");
        dialog.setMessage("测试完成，共测试" + testLists.size() + "个单词，正确" + trueTiems +"个，错误" + falseTimes + "个，不认识"
        + unknowTimes + "个。正确率" +  accuracy + "%");
        dialog.setPositiveButton("退出", new TestDialogConfirmClickLis());
        dialog.setNegativeButton("重新测试", new TestDialogCancelClickLis());
        dialog.setCancelable(false);//设置点击空白区域不消失
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    class TestDialogConfirmClickLis implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            cursorOfTest.close();
            dbR.close();
            dbHelper.close();
            TestActivity.this.finish();
        }
    }

    class TestDialogCancelClickLis implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            cursorOfTest.close();
            dbR.close();
            dbHelper.close();
            Intent intent = new Intent(TestActivity.this, TestActivity.class);
            TestActivity.this.startActivity(intent);
        }
    }
    public void testTrue(int choseId) {
        if (choseId == choseInt) {
            trueTiems ++;
        } else if (choseId != choseInt && choseId != 4){
            falseTimes ++;
        } else {
            unknowTimes ++;
        }
    }

    public void updateTimes() {
        double accuracy = (double) trueTiems / testLists.size() * 100;
        BigDecimal bigDecimal = new BigDecimal(accuracy);
        accuracy = bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        totalText.setText(testLists.size() + "\n" + "总计");
        knowText.setText(trueTiems +  "\n" + "正确");
        errorText.setText(falseTimes +  "\n" + "错误");
        unknowText.setText(unknowTimes +  "\n" + "不认识");
        accuracyText.setText("正确率：" + accuracy + "%");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        showAddDialog();
    }
}
