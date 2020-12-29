package helps;

import com.csvreader.CsvReader;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/12/25
 */
public class ReadCsvHelp {
    public static void main(String[] args) {
        String fileDir = "D:\\file_temp\\wlw\\bu";
        String filename="bubu2.csv";
        String fileFullName = fileDir+ File.separator+filename;
        List<List<String>> lists = ReadCsvHelp.readCsv(fileFullName);
        System.out.println(lists);

    }
    public static  List<List<String>> readCsv(String filePath) {
        List<List<String>> listdata = null;
        try {
            CsvReader reader = new CsvReader(filePath,',',Charset.forName("UTF-8"));
          reader.readHeaders(); //跳过表头,不跳可以注释掉
            listdata = new LinkedList();
            while(reader.readRecord()){
                List<String> list = Arrays.asList(reader.getValues());
                listdata.add(list);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listdata;
    }
}
