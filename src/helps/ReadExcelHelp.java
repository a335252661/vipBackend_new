package helps;

import jxl.CellType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/12/25
 */
public class ReadExcelHelp {


    public static void main(String[] args) {
        String fileDir = "D:\\file_temp\\wlw\\bu";
        String filename = "bubu2.csv";
        String fileFullName = fileDir + File.separator + filename;
        List<List<String>> dataLists = ReadExcelHelp.readExcel(fileFullName, null);
        System.out.println(dataLists);
    }

    /**
     * 读取Excel文件的内容
     *
     * @param ，以InputStream的形式传入
     * @param sheetName          sheet名字
     * @return 以List返回excel中内容
     */
    public static List<List<String>> readExcel(String fileFullName, String sheetName) {


        //定义工作簿
        XSSFWorkbook xssfWorkbook = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(fileFullName);
            xssfWorkbook = new XSSFWorkbook(fileInputStream);
        } catch (Exception e) {
            System.out.println("Excel data file cannot be found!");
        }

        //定义工作表
        XSSFSheet xssfSheet;
        if (null == sheetName || "".equals(sheetName)) {
            // 默认取第一个子表
            xssfSheet = xssfWorkbook.getSheetAt(0);
        } else {
            xssfSheet = xssfWorkbook.getSheet(sheetName);
        }

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        //定义行
        //默认第一行为标题行，index = 0
        XSSFRow titleRow = xssfSheet.getRow(0);

        //循环取每行的数据
        List<List<String>> alllist = new LinkedList<List<String>>();
        for (int rowIndex = 1; rowIndex < xssfSheet.getPhysicalNumberOfRows(); rowIndex++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowIndex);
            if (xssfRow == null) {
                continue;
            }

            Map<String, String> map = new LinkedHashMap<String, String>();
            List<String> datalist = new LinkedList<String>();
            //循环取每个单元格(cell)的数据
//            for (int cellIndex = 0; cellIndex < xssfRow.getPhysicalNumberOfCells(); cellIndex++) {
            for (int cellIndex = 0; cellIndex < xssfRow.getLastCellNum(); cellIndex++) {
                XSSFCell titleCell = titleRow.getCell(cellIndex);
                XSSFCell xssfCell = xssfRow.getCell(cellIndex);
                map.put(getString(titleCell), getString(xssfCell));
                datalist.add(getString(xssfCell).trim());
            }
            list.add(map);
            alllist.add(datalist);
        }
        return alllist;
    }

    /**
     * 把单元格的内容转为字符串
     *
     * @param xssfCell 单元格
     * @return 字符串
     */
    public static String getString(XSSFCell xssfCell) {
        if (xssfCell == null) {
            return "";
        }
        xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
        return xssfCell.getStringCellValue();
    }

    public static String getString2(XSSFCell cell) {
        String cellValue = "";
        //判断数据的类型
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: //数字
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING: //字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA: //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_BLANK: //空值
                cellValue = "";
                break;
            case Cell.CELL_TYPE_ERROR: //故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }

}
