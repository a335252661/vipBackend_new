package helps;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 * @author by cld
 * @date 2020/3/22  14:10
 * @description:
 */
public class FileHelp {

    public static void isExistDriLocation(String dirLocation){
        File locationFile = new File(dirLocation);
        if (!locationFile.exists()) {
            locationFile.mkdirs();
            System.out.println("文件夹路径不存在，生成完成。");
        }
    }

    public static File isExistAndCreate(String fullPath){

        File file = null;
        try {
            file = new File(fullPath);
            System.out.println("全路径=="+fullPath);
            if(!file.exists()){
                file.createNewFile();
                System.out.println("文件，生成完成。");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;

    }

    public static File isExistAndCreate(String dirLocation ,String fileName){
        FileHelp.isExistDriLocation(dirLocation);
        return FileHelp.isExistAndCreate(dirLocation.concat(fileName));
    }

    /**
     *  得到文件夹下所有文件全路径名字
     * @param path
     */
    public static ArrayList<String> getCurrentFileAllLocation(String path) {
        ArrayList<String> list = new ArrayList<String>();
        File file = new File(path);
        File[] array = file.listFiles();
        for (int i = 0; i < array.length; i++) {
            if (array[i].isFile()) {
//                System.out.println("^^^^^" + array[i].getName());
                list.add(path+File.separator + array[i].getName());
            } else if (array[i].isDirectory()) {
            }
        }
        return list;
    }

    /**
     *  得到文件夹下所有文件全路径名字 匹配正则
     * @param path
     * @param regex
     * @return
     */
    public static ArrayList<String> getCurrentFileAllLocation(String path , String regex) {

        if(StringUtils.isEmpty(regex)){
           return FileHelp.getCurrentFileAllLocation(path);
        }else {
            ArrayList<String> list = new ArrayList<String>();
            File file = new File(path);
            File[] array = file.listFiles();
            for (int i = 0; i < array.length; i++) {
                if (array[i].isFile() && array[i].getName().matches(regex)) {
                    list.add(path+File.separator + array[i].getName());
                } else if (array[i].isDirectory()) {
                }
            }

            return list;
        }
    }

    public static void getCurrentFileAllLocation2(String path) {
        File file = new File(path);
        File[] array = file.listFiles();
        for (int i = 0; i < array.length; i++) {
            if (array[i].isFile()) {
                System.out.println("#####" + array[i]);
            } else if (array[i].isDirectory()) {
//                getFile(array[i].getPath());
            }
        }
    }

    /**
     *  得到文件夹下所有文件名字
     * @param path
     */
    public static ArrayList<String> getCurrentFileName(String path , String regex) {
        if(StringUtils.isEmpty(regex)){
            return FileHelp.getCurrentFileAllLocation(path);
        }else {
            ArrayList<String> list = new ArrayList<String>();
            File file = new File(path);
            File[] array = file.listFiles();
            for (int i = 0; i < array.length; i++) {
                if (array[i].isFile() && array[i].getName().matches(regex)) {
                    list.add(array[i].getName());
                } else if (array[i].isDirectory()) {
                }
            }

            return list;
        }
    }
    public static void getAllFileAllFileName(String path) {
        File file = new File(path);
        File[] array = file.listFiles();
        for (int i = 0; i < array.length; i++) {
            if (array[i].isFile()) {
                // only take file name
                System.out.println("^^^^^" + array[i].getName());
            } else if (array[i].isDirectory()) {
                getAllFileAllFileName(array[i].getPath());
            }
        }
    }

    public static void getAllFileAllLocation(String path) {
        File file = new File(path);
        File[] array = file.listFiles();

        for (int i = 0; i < array.length; i++) {
            if (array[i].isFile()) {
                System.out.println("#####" + array[i]);
            } else if (array[i].isDirectory()) {
                getAllFileAllLocation(array[i].getPath());
            }
        }
    }

    /**
     *  将文件移动到新的文件夹下
     * @param fileFullPath
     * @param dirPath
     */
    public static void fileToDir(String fileFullPath , String dirPath){
        try {
            File startFile=new File(fileFullPath);
            File endDirection=new File(dirPath);
            if(!endDirection.exists()) {
                endDirection.mkdirs();
            }
            File endFile=new File(endDirection+ File.separator+ startFile.getName());
            if (startFile.renameTo(endFile)) {
                System.out.println("文件移动成功！目标路径：{"+endFile.getAbsolutePath()+"}");
            } else {
                System.out.println("文件移动失败！起始路径：{"+startFile.getAbsolutePath()+"}");
            }
        }catch(Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 移动文件夹下所有文件到另一个文件夹
     * @param currDir
     * @param toDir
     */
    public static void dirToDir(String currDir , String toDir) {
        ArrayList<String> currentFileAllLocation = FileHelp.getCurrentFileAllLocation(currDir);
        for(String fileFullPath :currentFileAllLocation){
            FileHelp.fileToDir(fileFullPath, toDir);

        }
    }

    /**
     * 删除的那个文件
     * @param fileFullPath
     */
    public static void deleteFile(String fileFullPath) {
        File file = new File(fileFullPath);
        file.delete();
    }

    /**
     * 删除文件夹下所有文件
     * @param currDir
     */
    public static void deleteAllFile(String currDir) {
        ArrayList<String> currentFileAllLocation = FileHelp.getCurrentFileAllLocation(currDir);
        for(String fileFullPath :currentFileAllLocation){
            FileHelp.deleteFile(fileFullPath);
        }
    }

    /**
     * 复制全路径的文件到另一个文件夹
     * @param fileFullPuth
     * @param Dir
     */
    public static void copyFile(String fileFullPuth , String Dir){
        File file = new File(fileFullPuth);
        String name = file.getName();
        File fileout = new File(Dir+File.separator+name);
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(file).getChannel();
            outputChannel = new FileOutputStream(fileout).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                inputChannel.close();
                outputChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 赋值文件夹中的文件到另一个文件夹
     * @param fromDir
     * @param toDir
     */
    public void copyDirFileToDir(String fromDir , String toDir){
        ArrayList<String> currentFileAllLocation = FileHelp.getCurrentFileAllLocation(fromDir);
        for(String str :currentFileAllLocation){
            FileHelp.copyFile(str , toDir);
        }
    }


    public static void main(String[] args) {
//        FileHelp.fileToDir("D:\\云中继开账.txt" , "D:\\testDir");

//        FileHelp.dirToDir("D:\\logs" , "D:\\logs\\xxxx");


        System.out.println("你好");

//        FileHelp.deleteAllFile("D:\\bgusr01\\vip_backend\\files");
//
//        ArrayList<String> currentFileAllLocation = FileHelp.getCurrentFileAllLocation("D:\\file_temp\\wlw\\1029", ".*docx$");
        ArrayList<String> currentFileAllLocation = FileHelp.getCurrentFileAllLocation("D:\\file_temp\\wlw\\1029", "BILL.*");
        System.out.println(currentFileAllLocation);
    }

}
