package com.example.hejiayuan.vocabulary.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.hejiayuan.vocabulary.entities.Dict;
import com.example.hejiayuan.vocabulary.entities.WordValue;
import com.example.hejiayuan.vocabulary.network.NetOperator;

import java.io.InputStream;

public class Mp3Player {
    public final static String MUSIC_ENG_RELATIVE_PATH = "yueci/sounds/sounds_EN";
    public final static String MUSIC_USA_RELATIVE_PATH = "yueci/sounds/sounds_US";
    public final static int ENGLISH_ACCENT = 0;
    public final static int USA_ACCENT = 1;

    public Context context = null;
    public String tableName = null;
    public MediaPlayer mediaPlayer = null;
    FileUtils fileUtils = null;
    Dict dict = null;
    public boolean isMusicPermitted = true;//用于对音乐播放进行保护性设置，当该变量为fasle时
    //阻止一次播放

    public Mp3Player(Context context, String tableName) {
        this.context = context;
        this.tableName = tableName;
        fileUtils = new FileUtils();
        dict = new Dict(context, tableName);
        isMusicPermitted = true;
    }

    /**
     * 首先看手机里有没有，若有则播放，没有执行下一步
     * 看一下dict表中有没有单词的记录，若有看一下发音字段是不是有美式发音或英式发音若无则退出
     * 若没有字段记录，访问网络Mp3然后播放
     * 一个Activity中一般只能有一个Voice成员变量，对应也只有MediaPlayer对象
     * 这样才能对播放状态进行有效控制
     *
     * @param word
     * @param accent
     * @param isAllowedToUseInternet
     * @param isPlayRightNow
     */
    public void playMusicByWord(String word, int accent,
                                boolean isAllowedToUseInternet, boolean isPlayRightNow) {
        if (word == null || word.length() <= 0) {
            return;
        }
        char[] wordArray = word.toCharArray();
        char initialCharacter = wordArray[0];

        String path = null;
        String pronUrl = null;
        WordValue w = null;

        if (accent == ENGLISH_ACCENT) {
            path = MUSIC_ENG_RELATIVE_PATH;
        } else {
            path = MUSIC_USA_RELATIVE_PATH;
        }

        if (fileUtils.isFileExist(path + initialCharacter + "/", "-$-" + word + ".mp3") == false) {
            //为了避免多次多个线程同时访问网络下载同一个文件，这里加了这么一个控制变量
            if (isAllowedToUseInternet == false) {
                return;
            }
            if (dict.isWordExist(word) == false) { //数据库中没有单词记录，从网络上进行同步
                if ((w = dict.getWordFromIntrenet(word)) == null) {
                    return;
                }
                dict.insertWordToDict(w, true);
            }//走到这一步说明从网上同步成功，数据库一定存在单词记录

            if (accent == ENGLISH_ACCENT) {
                pronUrl = dict.getPronEngUrl(word);
            } else {
                pronUrl = dict.getPronUSAUrl(word);
            }
            if (pronUrl == null || pronUrl == "null" || pronUrl.length() <= 0)
                return;//这说明网络上也没有对应发音，故退出
            //得到了Mp3地址后下载到文件夹中然后播放
            Log.d("DictActivity.this", "播放失败1");
            InputStream in = null;
            in = NetOperator.getInputStreamByUrl(pronUrl);
//            new Thread(new Runnable() {
//                HttpURLConnection connection = null;
//                @Override
//                public void run() {
//                    try {
//                        URL url = new URL(pronUrl);
//                        connection = (HttpURLConnection) url.openConnection();
//                        connection.setRequestMethod("GET");
//                        connection.setConnectTimeout(8000);
//                        connection.setReadTimeout(8000);
//                        in = connection.getInputStream();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    } finally {
//                        if (connection != null) {
//                            connection.disconnect();
//                        }
//                    }
//                }
//            }).start();

            if (in == null) {
                Log.d("DictActivity.this", "播放失败2");
                return;
            }
            if (fileUtils.saveInputStreamToFile(in, path + initialCharacter + "/",
                    "-$-" + word + ".mp3") == false) {

                return;
            }
        }
            //走到这里说明文件夹里一定有响应的音乐文件，故在这里播放
            if (isPlayRightNow == false) {
                Log.d("DictActivity.this", "播放失败5");
                return;
            }

            /**
             * 这个方法存在缺点，可能因为同时new了多个MediaPlayer对象，导致start方法失败
             * 因此解决的办法是使用同一个MediaPlayer对象，若一次播放发现对象非空
             * 那么先调用release方法释放资源，再重新create
             *
             */
            if (isMusicPermitted == false) {
                return;
            }
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                try {
                    if (mediaPlayer != null) {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                        }
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    Log.d("DictActivity.this", "播放失败3");
                    mediaPlayer = MediaPlayer.create(context,
                            Uri.parse("file://" + fileUtils.getRootPath() +
                                    path + initialCharacter + "/-$-" + word + ".mp3"));
                    mediaPlayer.start();
                    // Log.d("DictActivity.this", "播放失败");
                } catch (Exception e) {
                    if (mediaPlayer != null)
                        mediaPlayer.release();
                    Log.d("DictActivity.this", "播放失败4");
                    e.printStackTrace();
                }
            }
    }
}
