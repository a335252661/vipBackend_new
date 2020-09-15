package vip.insert21Table;

import helps.FileHelp;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/6/12
 */
public class InsertFileToTable {



    public static void main(String[] args) {


        try {
            String dir = "D:\\file_temp\\销账";
            ArrayList<String> currentFileAllLocation = FileHelp.getCurrentFileAllLocation(dir);
            System.out.println(currentFileAllLocation);


            for(String fileFullName : currentFileAllLocation){
                List<String> lines = FileUtils.readLines(new File(fileFullName), "UTF-8");
                for (String line : lines) {
                    System.out.println(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
