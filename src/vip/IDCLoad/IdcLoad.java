package vip.IDCLoad;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import utils.DBConn;
import vip.wlw.*;
import vip.wlw.WlwJdbcUtil;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/12/29
 */
public class IdcLoad {
//    //@Value("${idcLoad.bakFilePath}")
    private String bakFilePath = "/acct/acct_payment/JtBill/data/IDC/chk/bak/";

    //@Value("${idcLoad.billFilePath}")
    private String billFilePath = "/acct/acct_payment/JtBill/data/IDC/invoice/";

    //@Value("${idcLoad.delimiter}")
    private String delimiter ="\\|";

    //@Value("${idcLoad.serviceType}")
    private String serviceType ="4";

    //@Value("${idcLoad.createStaff}")
    private Long createStaff = 365895214L;

    //@Value("${idcLoad.chgWho}")
    private String chgWho = "system";

    //@Value("${idcCheck.chkFilePath}")
    private String chkFilePath ="/acct/acct_payment/JtBill/data/IDC/chk/";

    //@Value("${idcCheck.chkFileRegExp}")
    private String chkFileRegExp =".*SUM.*.021";

    //@Value("${idcCheck.billFileRegExp}")
    private String billFileRegExp =".*";

    //@Value("${idcCheck.origFilePath}")
    private String origFilePath ="/acct/acct_payment/JtBill/data/IDC/chk/orig/";

    //@Value("${idcCheck.rejectFilePath}")
    private String rejectFilePath ="/acct/acct_payment/JtBill/data/IDC/chk/rej/";

    //@Value("${idcCheck.errFilePath}")
    private String errFilePath ="/acct/acct_payment/JtBill/data/IDC/chk/err/";
    public static void main(String[] args) {
        IdcLoad idcLoad = new IdcLoad();
        idcLoad.check();
        idcLoad.load();
    }

    public void check() {
        Connection iamRepConn = null;

        try {
            //根据正规表达式匹配取得指定路径下的稽核文件
            List<File> chkFiles = FileUtil.getFiles(chkFilePath, chkFileRegExp);
            IdcCheckFileParser idcCheckFileParser = null;
            String checkFileName = null;
            String chkErrCode = null;
            //if(chkFiles.size() == 1){
            for(File theFile:chkFiles){
                //File theFile = chkFiles.get(0);
                try {
//                    iamRepConn = oracleCopyDataSource.getConnection() ; //复制库
                    iamRepConn = DBConn.getCopyProConn();
                    long batchNo = JtBillLog.getSeq(iamRepConn);
                    iamRepConn.setAutoCommit(false);

                    //解析稽核文件
                    idcCheckFileParser = new IdcCheckFileParser(theFile,delimiter,chkFilePath,billFileRegExp,serviceType);
                    idcCheckFileParser.parser();

                    chkErrCode = idcCheckFileParser.getCheckFile().getErrCode();
                    if (StringUtils.isEmpty(chkErrCode)) {

                        File invFile = null;
                        for(JtBillLog invoice:idcCheckFileParser.getFiles()){
                            invFile = new File(chkFilePath + invoice.getFileName());
                            if(StringUtils.isEmpty(invoice.getErrCode())){
                                IdcInvoiceFileParser invParser = null;
                                if("4".equals(serviceType)){
                                    invParser = new IdcInvoiceFileParser(invFile,delimiter);
                                }

                                invParser.parser();
                                if(StringUtils.isEmpty(invParser.getErrCode())){

                                } else {
                                    createErrFile(invParser);
                                    invoice.setErrCode(invParser.getErrCode());
                                    //invoice.setStatus(-1);
                                }
                                invoice.setStatus(0);

                                invoice.setServiceType(Long.parseLong(serviceType));
                                invoice.setChgWho(chgWho);
                                invoice.setBatchNo(batchNo);
                                invoice.insert(iamRepConn);
                                //将校验成功的账单文件移到加载文件路径下
                                FileUtil.moveFile(invFile, billFilePath + invFile.getName());
                            } else {
                                //生成拒收账单文件
                                FileUtil.moveFile(origFilePath + invFile.getName(), rejectFilePath + invoice.getErrCode() + invFile.getName());
                            }
                        }

                        idcCheckFileParser.getCheckFile().setStatus(2);
                    } else {
                        //生成拒收稽核文件
                        FileUtil.moveFile(origFilePath + theFile.getName(), rejectFilePath + chkErrCode + theFile.getName());
                        //if ("F8007".equals(chkErrCode)) {
                        for(JtBillLog bLog:idcCheckFileParser.getFiles()) {
                            //if ("F8007".equals(bLog.getErrCode())) {
                            //生成拒收账单文件
                            FileUtil.moveFile(origFilePath + bLog.getFileName(), rejectFilePath + chkErrCode + bLog.getFileName());
                            //}
                        }
                        //}
                        idcCheckFileParser.getCheckFile().setStatus(-1);
                    }

                    idcCheckFileParser.getCheckFile().setServiceType(Long.parseLong(serviceType));
                    idcCheckFileParser.getCheckFile().setChgWho(chgWho);
                    idcCheckFileParser.getCheckFile().setBatchNo(batchNo);
                    idcCheckFileParser.getCheckFile().insert(iamRepConn);

                    iamRepConn.commit();
                } catch(Exception ex) {
                    iamRepConn.rollback();
                    ex.printStackTrace();
                    XxlJobLogger.log( "IdcBillLoad error msg:" + ex.getLocalizedMessage() + " fileName=" + checkFileName);
                }

            }
            //将全部文件移到备份目录
            FileUtil.moveAllFile(chkFilePath, bakFilePath);
        } catch(Exception ex) {
            ex.printStackTrace();
            XxlJobLogger.log("IdcBillLoad error msg:" + ex.getLocalizedMessage());
        } finally {
            WlwJdbcUtil.close(iamRepConn);
        }
    }
    private void createErrFile(JtInvoiceFileParser parser) throws Exception{
        List<String> ct = new ArrayList<String>();
        String errFileName = errFilePath  + "E" + parser.getTheFile().getName();

        for(JtBillData billData:parser.getBillDatas()){
            if(StringUtils.isNotEmpty(billData.getErrCode())){
                ct.add( "1|" + BaseUtil.nullToStr(billData.getErrCode(),"") + "|" + billData.getFileLineId()
                        + "|" + billData.getOriginalData()+ "|");
//				Tool.writeFile(errFileName, "1|" + BaseUtil.nullToStr(billData.getErrCode(),"") + "|" + billData.getFileLineId()
//						+ "|" + billData.getOriginalData()+ "|", true);
            }

        }
        Tool.writeFile(errFileName, "STA|" + Long.toString(ct.size())+ "|", true);
        Tool.writeFile(errFileName, ct, true);
        Tool.writeFile(errFileName, parser.getFileEnd()+ "|", true);
    }
    public void load() {
        Connection iamConn = null;
        Connection hssConn = null;
        Connection iamRepConn = null ;
        IdcInvoiceFileParser billFileParser = null;
        File invFile = null;
        PreparedStatement ps = null;

        long seq;
        String yyyyMM = BaseUtil.getCurrentDate(BaseUtil.MONTH_PATTERN1);
        ExecutorService service = null;
        try {
//            iamRepConn = oracleCopyDataSource.getConnection() ;
            iamRepConn = DBConn.getCopyProConn();
            List<JtBillLog> jtBills = JtBillLog.query(iamRepConn, "0","2,8",serviceType);

            StringBuffer sql = new StringBuffer();
            sql.append(" insert into JT_BILL_DATA( ");
            sql.append("FILE_LINE_ID,");
            sql.append("ACCT_ID,");
            sql.append("SERV_ID,");
            sql.append("AMOUNT,");
            sql.append("BILLING_MONTH,");
            sql.append("CITY,");
            sql.append("AERA_CODE,");
            sql.append("SUB_TYPE_CODE,");
            sql.append("TAX_AMOUNT, ");
            sql.append("AFTER_TAX_AMOUNT, ");
            sql.append("TAX,");
            sql.append("VAT_TYPE_CODE,");
            sql.append("STATUS,");
            sql.append("SERVICE_TYPE,");
            sql.append("FILE_TYPE,");
            sql.append("FILE_NAME,");
            sql.append("ANNOTATION,");
            sql.append("SEQ,");
            sql.append("PAY_INTERIM_SEQ,");
            sql.append("PRE_ADJ_MONTH,");
            sql.append("SERV_TYPE,");
            sql.append("TAX_CODE,");
            sql.append("PRODUCT_ID,");
            sql.append("GROUP_NO,");
            sql.append("ACCRUAL_DATE,");
            sql.append("CONTRACT_NO,");
            sql.append("PROJECT_CODE,");
            sql.append("SPARE_FIELD1,");
            sql.append("SPARE_FIELD2,");
            sql.append("SPARE_FIELD3,");
            sql.append("CREATE_DATE");
            sql.append(" ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate) ");


            iamRepConn.setAutoCommit(false);
            iamConn.setAutoCommit(false);
            ps = iamRepConn.prepareStatement(sql.toString());
            //AccountManager acctMgr = new AccountManager(iamConn,hssConn);
            for(JtBillLog jtBill:jtBills){
                try{

                    int n = 0;
                    invFile = new File(billFilePath + jtBill.getFileName());
                    if("4".equals(serviceType)){
                        billFileParser = new IdcInvoiceFileParser(invFile,delimiter);
                    }
					/* else {
						billFileParser = new YdInvoiceFileParser(invFile,delimiter);
					}*/
                    billFileParser.parser();

                    for(JtBillData billData:billFileParser.getBillDatas()){
                        //ֻ只处理正常话单
                        if(StringUtils.isEmpty(billData.getErrCode())){
                            seq = WlwJdbcUtil.getSeqNextVal(iamRepConn, "SEQ_JT_BILL_NO");
//							billData.setServiceType(Long.parseLong(serviceType));

                            if(billData.getProductId().equals("13411525")){
                                billData.setServiceType(6);//云录音业务
                            }else if(billData.getProductId().equals("13410985")){
                                billData.setServiceType(7);//云间高速
                            }else if(billData.getProductId().equals("13412685")){
                                billData.setServiceType(8);//云中继
                            }else if(billData.getProductId().equals("13412686")){
                                billData.setServiceType(9);//工作号
                            }else{
                                billData.setServiceType(Long.parseLong(serviceType));
                            }

                            billData.setStatus(0);
                            billData.setSeq(seq);
                            billData.idcDataInsert(ps);
                            n++;
                            if (n % 200 == 0) {
                                ps.executeBatch();
                                iamRepConn.commit();
                                n = 0 ;
                            }
                        }else{
                            XxlJobLogger.log("error: " + billData.getFileName()+"error msg:"+billData.getOriginalData());
                        }
                    }

                    try{
                        ps.executeBatch();
                        iamRepConn.commit();
                    }catch(Exception e1){
                        e1.printStackTrace();
                        XxlJobLogger.log("J批处理异常:" + e1.getLocalizedMessage());
                    }finally {
                        WlwJdbcUtil.close(ps);
                    }


                    jtBill.setStatus(2);
                    jtBill.update(iamRepConn);
                    iamRepConn.commit();
                    //处理完成后将文件移动备份目录
                    FileUtil.moveFile(invFile, bakFilePath + invFile.getName());
                } catch(Exception ex1){
                    try {
                        iamRepConn.rollback();
                    } catch (SQLException e) {}
                    ex1.printStackTrace();
                    XxlJobLogger.log( "JtBillLoad error msg:" + ex1.getLocalizedMessage() + ",file_name =" + jtBill.getFileName());
                }

            }

        } catch(Exception ex) {
            ex.printStackTrace();
            XxlJobLogger.log( "IdcBillLoad error msg:" + ex.getLocalizedMessage());
        }  finally {
            WlwJdbcUtil.close(iamConn);
            WlwJdbcUtil.close(hssConn);
            WlwJdbcUtil.close(iamRepConn);
            if(service != null){
                service.shutdown();
            }
        }
    }
}
