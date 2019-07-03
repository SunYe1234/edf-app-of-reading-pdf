package cn.xiaolong.pdfiumpdfviewer.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * Created by yezi on 2019/7/3.
 */

public class FileInfoUtils {
    /**
       * 获得文件类型(文件后缀),要么是文件夹，要么是文件后缀
       *
       * @param path
       * @return
       */
    public static String getFileSuffix(String path) {
        File file = new File(path);
        String info = null;
        if (file.isFile())
            info = path.substring(path.lastIndexOf(".") + 1, path.length()) + "文件";
        if (file.isDirectory())
            info = "文件夹";
        return info;
    }

    /**
   * 获得文件的大小
   *
   * @param path
   * @return
   */
    public static long getFileSize(String path)
    {
        File f = new File(path);
        long size = 0;
        try {
            if (f.exists())
            {
                if (f.isDirectory())
                {
                    File flist[] = f.listFiles();
                    for (int i = 0; i < flist.length; i++)
                        size = size + getFileSize(flist[i].getPath());

                }
                else
                    {
                        FileInputStream fis = null;
                        fis = new FileInputStream(f);
                        size = fis.available();
                    }
            }
            else
                {
                    f.createNewFile();
                    System.out.println("文件不存在");
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
}



    // get the number of documents under a file path。
    public static long getFileSize(File f) {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++)
        {
            if (flist[i].isDirectory())
                size = size + getFileSize(flist[i]);
            else
                size = size + 1;
        }
        return size;
    }

    //get all of the document names under a file path
    public static ArrayList<String> getFilesName(File f)
    {
        File flist[] = f.listFiles();
        ArrayList<String> names=new ArrayList<String>();
        for (int i = 0; i < flist.length; i++)
        {
            names.add(flist[i].getName());
        }
        return names;
    }

}
