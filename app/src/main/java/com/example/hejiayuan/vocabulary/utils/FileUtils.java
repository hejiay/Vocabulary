package com.example.hejiayuan.vocabulary.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FileUtils {
    private String PATH;

    public FileUtils() {

        PATH = Environment.getExternalStorageDirectory() + "/";
    }

    /**
     * path直接创建文件，若已存在返回null
     */
    public File createFile(String path, String fileName) {
        File file = null;
        createDir(path);
        try {
            file = new File(PATH + path + fileName);
            if (file.exists() && file.isFile()) {
                return null;
            }
            file.createNewFile();//创建文件
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 创建目录，如果存在同名文件夹返回该文件夹，否则创建文件
     * @param dirName
     * @return
     */
    public File createDir(String dirName) {
        File dir = new File(PATH + dirName);
        if (dir.exists() && dir.isDirectory()) {
            return  dir;
        }
        dir.mkdirs();//可创建多级文件夹
        return dir;
    }

    /**
     * 相对目录
     * @param path
     * @return
     */
    public ArrayList<String> lisContentsOfFile(String path) {
        ArrayList<String> list = new ArrayList<String>();
        File file = new File(PATH + path);
        File[] fileList = file.listFiles();
        if (fileList == null) {
            return list;
        }
        for (int i = 0; i < fileList.length; i++) {
            System.out.println(fileList[i].getName());
        }
        return list;
    }

    /**
     * 判断文件夹是否存在
     * @param path
     * @param fileName
     * @return
     */
    public boolean isFileExist(String path, String fileName) {
        File file = new File(PATH + path + fileName);
        return file.exists();
    }

    /**
     * 获得文件输入流
     * @param path
     * @param fileName
     * @return
     */
    public InputStream getInputStreamFromFile(String path, String fileName) {
        InputStream inputStream = null;
        File file = new File(PATH + path + fileName);
        if (file.exists() == false) {
            return null;
        }
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return inputStream;
    }

    /**
     *
     * @param in
     * @param path 文件存储的相对路劲
     * @param fileName
     * @return
     */
    public boolean saveInputStreamToFile(InputStream in, String path, String fileName) {
        File file = createFile(path, fileName); //相对路径
        int length = 0;
        Log.d("DictAvtivity.this", PATH);
        if (file == null) {
            return  true; //文件已存在
        }
        byte[] buffer = new byte[1024];
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            while((length = in.read(buffer)) != -1) {
                //用read返回的实际成功读取的字节数将buffer写入文件
                fileOutputStream.write(buffer, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fileOutputStream != null)
                    fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public String getRootPath() {
        Log.d("DictAvtivity.this", Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    }

    public String getPATH() {

        return PATH;
    }

    public void setPATH(String PATH) {

        this.PATH = PATH;
    }
}
