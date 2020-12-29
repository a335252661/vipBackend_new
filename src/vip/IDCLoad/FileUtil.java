package vip.IDCLoad;

import org.apache.commons.io.FileUtils;
import vip.wlw.BaseUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


public class FileUtil {
    /**
     * 得到指定路径下的全部文件
     * @param path        文件路径
     * @param fileRegExp  正则表达式 如为空则获取全部文件
     * @return
     */
    public static List<File> getFiles(String path,String fileRegExp){
        File filePath = new File(path);
        List<File> fileList = new ArrayList<File>();
        if(filePath.exists()){
            File[] fileArr = filePath.listFiles(new FileRegexFilter(fileRegExp));
            for(File theFile:fileArr){
                if(theFile.exists() && theFile.isFile()){
                    fileList.add(theFile);
                }
            }
        }
        return fileList;
    }

    public static List<String> getFileNames(String path,String fileRegExp){
        File filePath = new File(path);
        List<String> fileList = new ArrayList<String>();
        if(filePath.exists()){
            File[] fileArr = filePath.listFiles(new FileRegexFilter(fileRegExp));
            for(File theFile:fileArr){
                if(theFile.exists() && theFile.isFile()){
                    fileList.add(theFile.getName());
                }
            }
        }
        return fileList;
    }
    /**
     * 将源文件内容移动到指定目标下
     * @param srcFile       源文件
     * @param destFileName  目标文件名
     * @throws IOException
     */
    public static boolean moveFile(File srcFile, String destFileName){
        return srcFile.renameTo(new File(destFileName));
    }
    /**
     * 将源文件内容移动到指定目标下
     * @param srcFileName       源文件名
     * @param destFileName  目标文件名
     * @throws IOException
     */
    public static boolean moveFile(String srcFileName, String destFileName){
        return FileUtil.moveFile(new File(srcFileName), destFileName);
    }
    /**
     * 将源文件内容移动到指定目标下
     * @param srcFilePath   源文件路径
     * @param destFilePath  目标文件路径
     * @throws IOException
     */
    public static void moveAllFile(String srcFilePath, String destFilePath){
        List<File> files = FileUtil.getFiles(srcFilePath, null);
        for(File theFile:files){
            FileUtil.moveFile(theFile,destFilePath + theFile.getName());
        }
    }
}
