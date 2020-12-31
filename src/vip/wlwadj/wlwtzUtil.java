package vip.wlwadj;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import utils.DBConn;
import vip.IDCLoad.FileUtil;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/12/31
 */
public class wlwtzUtil {

    static String filePath="/acct/acct_payment/JtBill/data/wlw/adj/file/";
    static String excelDataPath="/acct/acct_payment/JtBill/data/wlw/adj/excel/";
    static String filePathbak="/acct/acct_payment/JtBill/data/wlw/adj/filebak";

//    static String filePath="D:\\file_temp\\wlw\\调账\\file\\";
//    static String excelDataPath="D:\\file_temp\\wlw\\调账\\excel\\";
//    static String filePathbak="D:\\file_temp\\wlw\\调账\\filebak";

    private static Connection conn = DBConn.getCopyProConn();
//    private static Connection conn = DBConn.getCopy235ProConn();
//    private static Connection conn = DBConn.getDbusr06ProConn();
//    private static Connection conn = DBConn.getDbusr01TestConn();
        

    public wlwtzUtil() {
    }

    public String adjustAccount() throws IOException {
        new SimpleDateFormat("yyyyMMdd");
        InputStreamReader isr = null;
        FileInputStream fis = null;
        BufferedReader br = null;
        String message = "";

        try {
            deleteTempTable();
            deleteTempTablewZxAndTZ1();
            String str = "";
            List<File> fileList = FileUtil.getFiles(filePath, (String)null);
            System.out.println("get file size num : " + fileList.size());
            List<String[]> list = new ArrayList();
            if (fileList.size() <= 0) {
                message = "没有获取到集团下发文件";
                System.out.println("没有获取到集团下发文件");
                String var14 = message;
                return var14;
            }

            Iterator var10 = fileList.iterator();

            while(var10.hasNext()) {
                File theFile = (File)var10.next();
                System.out.println("解析文件：" + theFile.getName() + " 开始");
                fis = new FileInputStream(theFile);
                isr = new InputStreamReader(fis);
                br = new BufferedReader(isr);

                while((str = br.readLine()) != null) {
                    String[] arrayStr = splitCrm(str);
                    if (arrayStr.length > 5) {
                        String[] copyarrayStr = new String[arrayStr.length + 1];
                        System.arraycopy(arrayStr, 0, copyarrayStr, 0, arrayStr.length);
                        copyarrayStr[16] = theFile.getName();
                        list.add(copyarrayStr);
                    }
                }

//                FileUtil.copyFileToDirectory(theFile, filePathbak);
                FileUtil.moveFile(theFile,filePathbak);
                System.out.println("移动文件+" + theFile.delete());
            }

            addAdjAccList(list);
            checkAccNum();
            addTmpWzxTz1();
            newCounts();
        } catch (Exception var17) {
            var17.printStackTrace();
        } finally {
            if (isr != null) {
                isr.close();
            }

            if (fis != null) {
                fis.close();
            }

            if (br != null) {
                br.close();
            }

        }

        return message;
    }

    public static void checkAccNum() throws SQLException, ParseException {
//        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        ResultSet rs4 = null;
        ResultSet rs5 = null;

        try {
//            conn = JdbcUtil.getConnection();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
            String strDate = simpleDateFormat.format(new Date());
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT ACC_NUM,REC_DATE FROM tmp_wzx_" + strDate + "  GROUP BY REC_DATE,ACC_NUM");
            ps = conn.prepareStatement(sql.toString());
            System.out.println("sql-- tmp_wzx_" + strDate + "::-" + sql.toString());
            rs = ps.executeQuery();
            int var10 = 0;

            while(rs.next()) {
                String accNum = rs.getString("ACC_NUM");
                String recDate = rs.getString("REC_DATE");
                String recDatePlus = null;
                recDatePlus = getDatePlus(recDate, 1);
                ++var10;
                StringBuffer sql2 = new StringBuffer();
                sql2.append("SELECT PAY_INTERIM_SEQ FROM jt_bill_data WHERE  ");
                sql2.append(" SERV_CODE=?");
                sql2.append(" AND BILLING_MONTH=? ");
                sql2.append(" GROUP BY PAY_INTERIM_SEQ");
                ps.clearBatch();
                ps = conn.prepareStatement(sql2.toString());
                ps.setString(1, accNum);
                ps.setString(2, recDate);
                rs2 = ps.executeQuery();
                boolean flag = true;

                while(rs2.next()) {
                    if (rs2.getLong("PAY_INTERIM_SEQ") != 0L) {
                        flag = false;
                        sql2.setLength(0);
                        sql2.append("SELECT ACCOUNT_NO,BILL_REF_NO FROM JT_BILL_DATA_INVOICE WHERE ");
                        sql2.append(" PAY_INTERIM_SEQ=?");
                        ps.clearBatch();
                        ps = conn.prepareStatement(sql2.toString());
                        ps.setLong(1, rs2.getLong("PAY_INTERIM_SEQ"));
                        rs3 = ps.executeQuery();
                        if (rs3.next()) {
                            sql2.setLength(0);
                            sql2.append("SELECT  status_cd as STATUS  FROM BILL_INVOICE WHERE ");
                            sql2.append(" BILL_REF_NO=?");
                            ps.clearBatch();
                            ps = conn.prepareStatement(sql2.toString());
                            ps.setString(1, rs3.getString("BILL_REF_NO"));
                            rs4 = ps.executeQuery();
                            if (rs4.next()) {
                                if (rs4.getLong("STATUS") == 1L) {
                                    sql2.setLength(0);
                                    sql2.append("SELECT USER_ID FROM BILL_INVOICE_DETAIL WHERE ");
                                    sql2.append(" BILL_REF_NO=?");
                                    sql2.append(" AND ROWNUM<=1");
                                    ps.clearBatch();
                                    ps = conn.prepareStatement(sql2.toString());
                                    ps.setString(1, rs3.getString("BILL_REF_NO"));
                                    rs5 = ps.executeQuery();
                                    if (rs5.next()) {
                                        updateAccountNoAndBillNo(strDate, accNum, recDate, rs3.getString("BILL_REF_NO"), rs3.getLong("ACCOUNT_NO"));
                                    } else {
                                        commonExceptionInfo(strDate, accNum, "该合帐号码" + accNum + "在BILL_INVOICE_DETAIL里没有user_id", recDate, (String)null);
                                    }
                                } else {
                                    commonExceptionInfo(strDate, accNum, "该合帐号码" + accNum + " 的账单在BILL_INVOICE表里status不为1", recDate, (String)null);
                                }
                            } else {
                                commonExceptionInfo(strDate, accNum, "该合帐号码" + accNum + " 在BILL_INVOICE表里未找到账单", recDate, (String)null);
                            }
                        } else {
                            commonExceptionInfo(strDate, accNum, "根据PAY_INTERIM_SEQ去JT_BILL_DATA_INVOICE查为空", recDate, (String)null);
                        }
                    }
                }

                if (flag) {
                    commonExceptionInfo(strDate, accNum, "没有开账，PAY_INTERIM_SEQ为空", recDate, (String)null);
                }
            }
        } catch (SQLException var19) {
            var19.printStackTrace();
        } finally {
            if (rs5 != null) {
                rs.close();
            }

            if (rs4 != null) {
                rs.close();
            }

            if (rs3 != null) {
                rs.close();
            }

            if (rs2 != null) {
                rs.close();
            }

            if (rs != null) {
                rs.close();
            }

            if (ps != null) {
                ps.close();
            }

        }

    }

    public static void updateAccountNoAndBillNo(String strDate, String accNum, String recDate, String billRefNo, Long acctId) {
//        Connection conn = null;
        PreparedStatement ps = null;

        try {
//            conn = JdbcUtil.getConnection();
            StringBuffer sql = new StringBuffer();
            sql.append("UPDATE TMP_WZX_" + strDate + "  SET BILL_REF_NO='" + billRefNo + "' , ACCT_ID=" + acctId + "   WHERE ACC_NUM='" + accNum + "' AND REC_DATE= '" + recDate + "'");
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql.toString());
            ps.execute();
            conn.commit();
            System.out.println("UPDATE TMP_WZX-sql-:" + sql.toString());
        } catch (Exception var16) {
            var16.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException var15) {
                    var15.printStackTrace();
                }
            }

        }

    }

    public static void commonExceptionInfo(String strDate, String accNum, String remark, String recDate, String flag) {
//        Connection conn = null;
        PreparedStatement ps = null;

        try {
//            conn = JdbcUtil.getConnection();
            StringBuffer sql = new StringBuffer();
            if (recDate != null) {
                sql.append("UPDATE TMP_WZX_" + strDate + "  SET STATUS=1 , REMARK='" + remark + "'   WHERE ACC_NUM='" + accNum + "' AND REC_DATE= '" + recDate + "'");
            } else {
                sql.append("UPDATE TMP_WZX_" + strDate + "  SET STATUS=1 , REMARK='" + remark + "'   WHERE ACC_NUM='" + accNum + "'");
            }

            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql.toString());
            ps.execute();
            conn.commit();
            System.out.println("UPDATE TMP_WZX-sql-:" + sql.toString());
        } catch (Exception var16) {
            var16.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException var15) {
                    var15.printStackTrace();
                }
            }

        }

    }

    public static void updateWzxTz(String accNum, String remark, String recDate) {
//        Connection conn = null;
        PreparedStatement ps = null;

        try {
//            conn = JdbcUtil.getConnection();
            StringBuffer sql = new StringBuffer();
            if (recDate != null) {
                sql.append("UPDATE TMP_WZX_TZ1 SET REMARK='" + remark + "'   WHERE ACC_NUM='" + accNum + "' AND REC_DATE= '" + recDate + "'");
            } else {
                sql.append("UPDATE TMP_WZX_TZ1 SET REMARK='" + remark + "'   WHERE STATUS=0");
            }

            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql.toString());
            ps.execute();
            conn.commit();
            System.out.println("UPDATE TMP_WZX_TZ1-sql-:" + sql.toString());
        } catch (Exception var14) {
            var14.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException var13) {
                    var13.printStackTrace();
                }
            }

        }

    }

    public static void deleteTempTable() {
//        Connection conn = null;
        PreparedStatement ps = null;

        try {
//            conn = JdbcUtil.getConnection();
            StringBuffer sql4 = new StringBuffer();
            sql4.append("insert into TEMP_ACCOUNT_INFOR_BAK  select * from TEMP_ACCOUNT_INFOR");
            System.out.println(sql4.toString());
            ps = conn.prepareStatement(sql4.toString());
            System.out.println("-----------------------------------------------------");

            ps.execute();
            conn.commit();
            StringBuffer sql3 = new StringBuffer();
            sql3.append("delete from TEMP_ACCOUNT_INFOR");
            System.out.println(sql3.toString());
            System.out.println("delete tempTable TEMP_ACCOUNT_INFOR sql is --:" + sql3.toString());
            conn.setAutoCommit(false);
            ps.clearBatch();
            ps = conn.prepareStatement(sql3.toString());
            ps.execute();
            conn.commit();
        } catch (Exception var12) {
            var12.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException var11) {
                    var11.printStackTrace();
                }
            }

        }

    }

    public static void deleteTempTablewZxAndTZ1() {
//        Connection conn = null;
        PreparedStatement ps = null;
        PreparedStatement ps3 = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String strDate = simpleDateFormat.format(new Date());

        try {
//            conn = JdbcUtil.getConnection();
            StringBuffer sql = new StringBuffer();
            sql.append("delete from tmp_wzx_" + strDate);
            conn.setAutoCommit(false);
            System.out.println("delete tempTable sql is --:" + sql.toString());
            ps = conn.prepareStatement(sql.toString());
            ps.execute();
            conn.commit();
            StringBuffer sql2 = new StringBuffer();
            sql2.append("delete from TMP_WZX_TZ1");
            conn.setAutoCommit(false);
            System.out.println("delete tempTable sql is --:" + sql2.toString());
            ps.clearBatch();
            ps = conn.prepareStatement(sql2.toString());
            ps.execute();
            conn.commit();
        } catch (Exception var15) {
            var15.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException var14) {
                    var14.printStackTrace();
                }
            }

        }

    }

    public static void addAdjAccList(List<String[]> list) throws Exception {
//        Connection conn = null;
        PreparedStatement ps = null;
        java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
        try {
//            conn = JdbcUtil.getConnection();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
            String strDate = simpleDateFormat.format(new Date());
            System.out.println("data insert tmp_wzx_" + strDate);
            StringBuffer sql = new StringBuffer();
            sql.append("INSERT INTO tmp_wzx_" + strDate);
            sql.append("(file_name, record_id, acc_id, acc_nbr, adj_amount, rec_date, bel_date, ser_code, local_code, account_type, expen_tax, adj_not_amount, rate_tax, account_vat, taxable, acc_num_type, acc_num,CREATETIME) VALUES");
            sql.append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)");
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql.toString());
            int num = 0;
            Iterator var8 = list.iterator();

            while(var8.hasNext()) {
                String[] listStr = (String[])var8.next();
                ps.setString(1, listStr[16]);
                ps.setInt(2, Integer.parseInt(listStr[0]));
                ps.setString(3, listStr[1]);
                ps.setString(4, listStr[2]);
                ps.setString(5, listStr[3]);
                ps.setString(6, listStr[4]);
                ps.setString(7, listStr[5]);
                ps.setString(8, listStr[6]);
                ps.setString(9, listStr[7]);
                ps.setString(10, listStr[8]);
                ps.setInt(11, Integer.parseInt(listStr[9]));
                ps.setString(12, listStr[10]);
                ps.setString(13, listStr[11]);
                ps.setString(14, listStr[12]);
                ps.setInt(15, Integer.parseInt(listStr[13]));
                ps.setString(16, listStr[14]);
                ps.setString(17, listStr[15]);
                ps.setDate(18, currentDate);
                ps.addBatch();
                ++num;
                if (num >= 500) {
                    ps.executeBatch();
                    conn.commit();
                    num = 0;
                }
            }

            ps.executeBatch();
            conn.commit();
        } catch (SQLException var12) {
            var12.printStackTrace();
        } finally {
            if (ps != null) {
                ps.close();
            }

        }

    }

    public static void addTmpWzxTz1() throws Exception {
//        Connection conn = null;
        PreparedStatement ps = null;

        try {
//            conn = JdbcUtil.getConnection();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
            String strDate = simpleDateFormat.format(new Date());
            StringBuffer sql = new StringBuffer();
            sql.append("INSERT INTO TMP_WZX_TZ1");
            sql.append("  ( FILE_NAME,RECORD_ID,Acc_Id, ACC_NBR,ADJ_AMOUNT,REC_DATE,BEL_DATE,SER_CODE,LOCAL_CODE,ACCOUNT_TYPE,EXPEN_TAX,ADJ_NOT_AMOUNT,RATE_TAX,ACCOUNT_VAT,TAXABLE,ACC_NUM_TYPE,ACC_NUM,ACCT_ID,ACCT_ITEM_TYPE_ID,STATUS,REMARK,BILL_REF_NO  )");
            sql.append("  SELECT T.FILE_NAME,T.RECORD_ID,T.Acc_Id, T.ACC_NBR,T.ADJ_AMOUNT,TO_CHAR(ADD_MONTHS(TO_DATE(T.REC_DATE, 'yyyyMM'), 1), 'yyyyMM') REC_DATE,T.BEL_DATE,T.SER_CODE,T.LOCAL_CODE,T.ACCOUNT_TYPE,T.EXPEN_TAX,");
            sql.append("  T.ADJ_NOT_AMOUNT,T.RATE_TAX,T.ACCOUNT_VAT,T.TAXABLE,T.ACC_NUM_TYPE,T.ACC_NUM,T.ACCT_ID,A.ACCT_ITEM_TYPE_ID,T.STATUS,T.REMARK,T.BILL_REF_NO ");
            sql.append("  FROM tmp_wzx_" + strDate + "  T");
            sql.append("  ,TB_BIL_ACCT_ITEM_TYPE@hss  A");
            sql.append("  WHERE ");
            sql.append("   T.ACCOUNT_TYPE = A.JT_ACCT_ITEM_TYPE_ID_BD");
            conn.setAutoCommit(false);
            System.out.println("hss table Trans sql--:" + sql.toString());
            ps = conn.prepareStatement(sql.toString());
            ps.execute();
            conn.commit();
        } catch (Exception var13) {
            var13.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException var12) {
                    var12.printStackTrace();
                }
            }

        }

    }

    public static boolean newCounts() throws Exception {
//        Connection conn = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        boolean countFlag = false;

        try {
//            conn = JdbcUtil.getConnection();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            simpleDateFormat.format(new Date());
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT   A.REC_DATE,A.BILL_REF_NO,A.ACC_NUM,I.INT_PRODUCT_LINE,sum(A.ADJ_AMOUNT) ");
            sql.append("   FROM TMP_WZX_TZ1 A,   BILL_T_ITEM_TYPE_ABP  I");
            sql.append("   WHERE ");
            sql.append("    A.ACCT_ITEM_TYPE_ID = I.ACCT_ITEM_TYPE_ID");
            sql.append("    AND A.STATUS is null ");
            sql.append(" group by A.REC_DATE,A.BILL_REF_NO,A.ACC_NUM,I.INT_PRODUCT_LINE  ");
            sql.append(" ORDER BY sum(A.ADJ_AMOUNT)");
            System.out.println("product tongji sql--:" + sql.toString());
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            List<List<String>> body = new ArrayList();
            StringBuffer sql2 = new StringBuffer();
            sql2.append("INSERT INTO TEMP_ACCOUNT_INFOR");
            sql2.append("(REC_DATE, BILL_REF_NO,ACC_NUM,INT_PRODUCT_LINE, ADJ_AMOUNT,STATUS,REMARK ) VALUES");
            sql2.append("( ?, ?, ?,?,?,?,?)");
            conn.setAutoCommit(false);
            ps2 = conn.prepareStatement(sql2.toString());
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyyMMddHHmmss");
            String exceName = simpleDateFormat2.format(new Date());

            while(rs.next()) {
                List<String> data = new ArrayList();
                data.add(rs.getString("REC_DATE"));
                data.add(rs.getString("BILL_REF_NO"));
                String accNum = rs.getString("ACC_NUM");
                if (accNum.startsWith("021")) {
                    accNum = accNum.substring(3);
                }

                data.add(accNum);
                data.add(rs.getString("INT_PRODUCT_LINE"));
                data.add(rs.getString("sum(A.ADJ_AMOUNT)"));
                body.add(data);
                ps2.setString(1, rs.getString("REC_DATE"));
                ps2.setString(2, rs.getString("BILL_REF_NO"));
                ps2.setString(3, rs.getString("ACC_NUM"));
                ps2.setString(4, rs.getString("INT_PRODUCT_LINE"));
                ps2.setString(5, rs.getString("sum(A.ADJ_AMOUNT)"));
                ps2.setString(6, "0");
                ps2.setString(7, "");
                ps2.execute();
                conn.commit();
                updateWzxTz(rs.getString("ACC_NUM"), "统计数据在" + exceName + ".xls,文件内", rs.getString("REC_DATE"));
            }

            if (body != null && body.size() > 0) {
                createExcelData(body, exceName);
                System.out.println("数据生成成功，已经入临时表TEMP_ACCOUNT_INFOR");
                System.out.println("excel文件已经生成");
                countFlag = true;
                boolean var15 = countFlag;
                return var15;
            }

            System.out.println("没有找到统计信息");
            updateWzxTz((String)null, "该合帐号码没有找到调账账单", (String)null);
        } catch (Exception var29) {
            var29.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException var28) {
                    var28.printStackTrace();
                }
            }

            if (ps2 != null) {
                ps2.close();
            }

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException var27) {
                    var27.printStackTrace();
                }
            }

        }

        return countFlag;
    }

    public static void createExcelData(List<List<String>> body, String exceName) {
        try {
            List<String> header = new ArrayList();
            header.add("账单月份");
            header.add("账单编号");
            header.add("设备号");
            header.add("费用类型");
            header.add("调整金额");
            OutputStream out = new FileOutputStream(excelDataPath + exceName + ".xls");
            generateExcel("调帐", header, body, out);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public static String[] splitCrm(String str) {
        String[] strArray = str.split("\\|");
        return strArray;
    }

    public static void main(String[] args) {
        System.out.println("main--start");
        Connection bssConn = DBConn.getBssConn();
        System.out.println(bssConn+"-------连接成功！！");
        int mm = 1/0;
        wlwtzUtil accountChange = new wlwtzUtil();
        try {
            accountChange.adjustAccount();
            System.out.println("main--end");
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }

    public static String getDatePlus(String date, int months) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Date dt = sdf.parse(date);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(2, months);
        Date dt1 = rightNow.getTime();
        String reStr = sdf.format(dt1);
        return reStr;
    }

    public static void generateExcel(String sheetName, List<String> header, List<List<String>> body, OutputStream out) {
        HSSFWorkbook excel = new HSSFWorkbook();
        HSSFSheet hssfSheet = excel.createSheet(sheetName);
        hssfSheet.setColumnWidth(0, 6656);
        hssfSheet.setColumnWidth(1, 5120);
        hssfSheet.setColumnWidth(2, 6144);
        hssfSheet.setColumnWidth(3, 6144);
        hssfSheet.setColumnWidth(4, 6144);
        hssfSheet.setColumnWidth(5, 6144);
        HSSFRow firstRow = hssfSheet.createRow(0);

        int rowNum;
        for(rowNum = 0; rowNum < header.size(); ++rowNum) {
            HSSFCell hssfCell = firstRow.createCell(rowNum);
            hssfCell.setCellValue(header.size() < rowNum ? "-" : (String)header.get(rowNum));
        }

        for(rowNum = 0; rowNum < body.size(); ++rowNum) {
            HSSFRow hssfRow = hssfSheet.createRow(rowNum + 1);
            List<String> data = (List)body.get(rowNum);

            for(int columnNum = 0; columnNum < data.size(); ++columnNum) {
                HSSFCell hssfCell = hssfRow.createCell(columnNum);
                hssfCell.setCellValue(data.size() < columnNum ? "-" : (String)data.get(columnNum));
            }
        }

        try {
            excel.write(out);
            System.out.println("excel文件已经生成!");
        } catch (IOException var12) {
            var12.printStackTrace();
        }

    }
}
