package vip.financialCheckHandler;

import helps.FileHelp;
import helps.SQLHelp;
import org.springframework.transaction.annotation.Transactional;
import sun.nio.cs.StandardCharsets;
import utils.DBConn;
import vip.wlw.XxlJobLogger;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2021/1/22
 */
public class FinancialCheckHandler {

    private static String financialCheckBasePath="/acct_ftp/payment/financialCheck";
    private static  Connection conn = DBConn.getCopyProConn();

    private static String cdmaPreOpen = financialCheckBasePath + "/cdmaPreOpen/cdma_pre_open.txt";

    private static String paymentToBl = financialCheckBasePath + "/paymentToBl/payment_to_bl.txt";

    private static String nblQianfei = financialCheckBasePath + "/nblQianfei/nbl_qianfei.txt";
//    private static String nblQianfei = "D:\\file_temp\\11111\\nblQianfei\\nbl_qianfei.txt";


    public static void main(String[] args) {


        FinancialCheckHandler fina = new FinancialCheckHandler();



        Map<String, List<String>> dataMap = new HashMap<String, List<String>>(8);

        List<String> lines = fina.getLines(cdmaPreOpen);
        if(lines.isEmpty()) {
            XxlJobLogger.log("数据文件--------cdma_pre_open.txt--------没有任何记录!");
        }else {
            dataMap.put("cdma_pre_open", lines);
        }
        lines = fina.getLines(paymentToBl);
        if(lines.isEmpty()) {
            XxlJobLogger.log("数据文件--------payment_to_bl.txt--------没有任何记录!");
        }else {
            dataMap.put("payment_to_bl", lines);
        }
        lines = fina.getLines(nblQianfei);
        if(lines.isEmpty()) {
            XxlJobLogger.log("数据文件--------nbl_qianfei.txt--------没有任何记录!");
        }else {
            dataMap.put("nbl_qianfei", lines);
        }
        fina.parseAndInsert2DB(dataMap);
    }

    void parseAndInsert2DB(Map<String, List<String>> map) {
        String regex = "\\|@\\|";
        List<Object[]> batchCpo = new ArrayList<Object[]>();
        List<Object[]> batchPtb = new ArrayList<Object[]>();
        List<Object[]> batchNq = new ArrayList<Object[]>();
        String cpoSql = null;
        String ptbSql = null;
        String nqSql = null;


        try {
            if(map.containsKey("cdma_pre_open")) {
                cpoSql = "INSERT INTO CDMA_PRE_OPEN_TEMP VALUES(?,?,?,?,?)";
                List<String> cpo_list = map.get("cdma_pre_open");
                for(String line : cpo_list) {
                    String[] lineData = line.split(regex);
                    Object[] param = {lineData[0].trim(),lineData[1].trim(),lineData[2].trim(),lineData[3].trim(),new BigDecimal(lineData[4].trim())};
                    batchCpo.add(param);
                }
            }

            if(map.containsKey("payment_to_bl")) {
                ptbSql = "INSERT INTO PAYMENT_TO_BL_TEMP VALUES(?,?,?,?,to_date(?,'yyyy-mm-dd'),?,?,?)";
                List<String> ptb_list = map.get("payment_to_bl");
                for(String line : ptb_list) {
                    String[] lineData = line.split(regex);
                    Object[] param = {lineData[0].trim(),Long.parseLong(lineData[1].trim()),Long.parseLong(lineData[2].trim()),
                            lineData[3].trim(),lineData[4].trim(), lineData[5].trim(), new BigDecimal(lineData[6].trim()), lineData[7].trim()};
                    batchPtb.add(param);
                }
            }

            if(map.containsKey("nbl_qianfei")) {
                nqSql = "INSERT INTO NBL_QIANFEI_TEMP VALUES(?,?,?,?,?)";
                List<String> nq_list = map.get("nbl_qianfei");
                for(String line : nq_list) {
                    String[] lineData = line.split(regex);
                    Object[] param = {lineData[0].trim(), lineData[1].trim(), lineData[2].trim(), lineData[3].trim(),new BigDecimal(lineData[4].trim())};
                    batchNq.add(param);
                }
            }

            //检查插入记录是否有误，有误则回滚
//            boolean flag = true;
//            int [] cpoCount = cpoSql==null ? new int[0] : icsJdbcTemplate.batchUpdate(cpoSql,batchCpo);
//            int [] ptbCount = ptbSql==null ? new int[0] : icsJdbcTemplate.batchUpdate(ptbSql,batchPtb);
//            int [] nqCount = nqSql==null ? new int[0] : icsJdbcTemplate.batchUpdate(nqSql,batchNq);
//            int [] destResultArr = new int[cpoCount.length + ptbCount.length + nqCount.length];
//            System.arraycopy(cpoCount, 0, destResultArr, 0, cpoCount.length);
//            System.arraycopy(ptbCount, 0, destResultArr, cpoCount.length, ptbCount.length);
//            System.arraycopy(nqCount, 0, destResultArr, cpoCount.length + ptbCount.length, nqCount.length);
//            for(int i : destResultArr) {
//                if(i == 0) {
//                    flag = false;
//                    break;
//                }
//            }
//            if(flag) {
//                XxlJobLogger.log("======执行成功=========");
//                XxlJobLogger.log("info", "======执行成功=========");
//            }else {
//                XxlJobLogger.log("======出现异常，回滚=========");
//                throw new Exception();
//            }


            SQLHelp.executeBatch(conn,cpoSql,batchCpo,200);
            SQLHelp.executeBatch(conn,ptbSql,batchPtb,200);
            SQLHelp.executeBatch(conn,nqSql,batchNq,200);


            //移动文件到temp文件夹
            FileHelp.fileToDir(cdmaPreOpen,financialCheckBasePath+File.separator+"cdmaPreOpen"+File.separator+"temp");
            FileHelp.fileToDir(paymentToBl,financialCheckBasePath+File.separator+"paymentToBl"+File.separator+"temp");
            FileHelp.fileToDir(nblQianfei,financialCheckBasePath+File.separator+"nblQianfei"+File.separator+"temp");

        }catch (Exception e) {
            XxlJobLogger.log("======出现异常，回滚=========");
            XxlJobLogger.log(e);

        }
    }

    private List<String> getLines(String ftpFilePath){
        List<String> lines = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(ftpFilePath), "utf-8"));
            String line;
            while((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }catch(IOException e) {
            e.printStackTrace();
        }finally {
            closeIO(reader);
        }
        return lines;
    }

    private void closeIO(Reader reader) {
        try {

            if(reader!=null) {
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
