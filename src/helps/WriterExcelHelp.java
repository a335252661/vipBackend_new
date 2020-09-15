package helps;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/4/15
 * jar  poi-3.10-FINAL.jar
 * poi-ooxml-3.10-FINAL-3.10.jar
 * poi-ooxml-schemas-3.10-FINAL-3.10.jar
 * xmlbeans-2.6.0.jar
 */
public class WriterExcelHelp {

    private static XSSFWorkbook wb = new XSSFWorkbook();
    private static XSSFSheet sheet=null;
    private static int rownum = 0;
    private static int cellnum = 0;

    public static void generateExcelFile(String fileLocation, String fileName, List<HashMap<String , Object>> data,String sheetName) {
        String fullPath = fileLocation+"\\"+fileName;
        FileHelp.isExistAndCreate(fileLocation,fileName);
        //只有一个sheet页
        try {
            //获取sheet
            if(null==sheetName){
                sheetName = "sheet1";
            }
            sheet = wb.createSheet(sheetName);

            int line = 1;
            for(HashMap<String , Object> map:data){
                if(line==1){    //第一行处理
                    Set<String> strings = map.keySet();
                    ArrayList<Object> ls=new ArrayList(strings);
                    //写字段名
                    WriterExcelHelp.writeRow(ls);

                    //写第一列值
                    ArrayList<Object> mapValues = WriterExcelHelp.getMapValues(map);
                    WriterExcelHelp.writeRow(mapValues);
                }else {
                    ArrayList<Object> mapValues = WriterExcelHelp.getMapValues(map);
                    WriterExcelHelp.writeRow(mapValues);
                }
                line++;
            }




            wb.setForceFormulaRecalculation(true);
            FileOutputStream out = new FileOutputStream(new File(fullPath));

            wb.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    public static void generateExcelFileWithResponse(HttpServletResponse response,
                                                     String fileName,
                                                     List<HashMap<String , Object>> data,
                                                     String sheetName) {
        //只有一个sheet页
        try {
            //获取sheet
            if(null==sheetName){
                sheetName = "sheet1";
            }
            sheet = wb.createSheet(sheetName);

            int line = 1;
            for(HashMap<String , Object> map:data){
                if(line==1){    //第一行处理
                    Set<String> strings = map.keySet();
                    ArrayList<Object> ls=new ArrayList(strings);
                    //写字段名
                    WriterExcelHelp.writeRow(ls);

                    //写第一列值
                    ArrayList<Object> mapValues = WriterExcelHelp.getMapValues(map);
                    WriterExcelHelp.writeRow(mapValues);
                }else {
                    ArrayList<Object> mapValues = WriterExcelHelp.getMapValues(map);
                    WriterExcelHelp.writeRow(mapValues);
                }
                line++;
            }
            wb.setForceFormulaRecalculation(true);


            String filename = fileName + ".xls";
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename="
                    .concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
            OutputStream out = response.getOutputStream();



            wb.write(out);// 将数据写出去
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }


    public static void generateExcelFileWithResponseEasy(HttpServletResponse response,
                                                         String fileName,
                                                         List<LinkedHashMap<String , Object>> data,
                                                         String sheetName) {

        StringBuffer buffer =null;
        int i=0;
        try {
            response.setContentType("application/x-msdownload;charset=gb2312");
            response.setHeader("Content-Disposition", "attachment;filename="
                    .concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));

            PrintWriter writer = response.getWriter();
            if(data.size()>0){
                for(LinkedHashMap<String , Object> maps : data){
                    i++;
                    if(i==1){  //
                        buffer = new StringBuffer();
                        for(Map.Entry<String, Object>  enmap : maps.entrySet()){
                            String key = enmap.getKey();
                            buffer.append(key);
                            buffer.append(",");
                        }
                        buffer.deleteCharAt(buffer.length()-1);
                        //写头部数据
                        writer.print(buffer.toString() + '\n');
                    }else {
                        buffer = new StringBuffer();
                        for(Map.Entry<String, Object>  enmap : maps.entrySet()){
                            String key = enmap.getValue().toString();
                            buffer.append(key);
                            buffer.append(",");
                        }
                        //写数据
                        buffer.deleteCharAt(buffer.length()-1);
                        writer.print(buffer.toString() + '\n');
                    }
                }
            }
            if (writer != null) {
                writer.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void writeRow(ArrayList<Object> lists) {
        XSSFRow row = sheet.createRow(rownum);
        for(Object cellStr:lists){
            XSSFCell cell = row.createCell(cellnum);
            if(null==cellStr){
                cell.setCellValue("");
            }else {
                cell.setCellValue(cellStr.toString());
            }
            cellnum++;
        }
        cellnum=0;
        rownum++;
    }

    public static ArrayList<Object> getMapValues(Map<String ,Object> map) {
        ArrayList<Object> list = new ArrayList<Object>();
        for(String key:map.keySet()){
            Object s = map.get(key);
            list.add(s);
        }
        return list;
    }

    /**
     * HttpServletResponse  需要  j2ee.jar
     * @param path
     * @param response
     */
    public static void download(String path, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename="
                    + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(
                    response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=gb2312");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        //List<HashMap<String , String>> data
        List<HashMap<String , Object>> list = new ArrayList<HashMap<String , Object>>();
        for(int i=0;i<100;i++){
            LinkedHashMap map = new LinkedHashMap();
            map.put("data1" , "1");
            map.put("data2" , "2");
            map.put("data3" , "3");
            map.put("data4" , "4");
            map.put("data5" , "5");
            map.put("data6" , "6");
            list.add(map);
        }

        WriterExcelHelp.generateExcelFile("D:\\tmp" , "111.xlsx",list,null);


//        HttpServletResponse response =null;
//        WriterExcelHelp.generateExcelFileWithResponse(response , "111.xlsx",list,null);
    }

}
