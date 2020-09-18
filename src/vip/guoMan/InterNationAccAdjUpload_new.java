package vip.guoMan;

import Pro.JdbcUtil;
import Pro.ProcUtil;
import helps.Properties2Help;
import helps.SQLHelp;
import org.apache.commons.lang.StringUtils;
import utils.DBConn;
import javax.tools.Tool;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 生成上月国漫调账(调增、调减)文件
 *
 * @author LGF
 * @date 2019/10
 */
public class InterNationAccAdjUpload_new {

    private static String adjFilePath = null;

    private static int fileDatasize = 0;

    Connection iamConn = null;

    private static Properties Tool =null;

    public InterNationAccAdjUpload_new() {
        adjFilePath = Tool.getProperty("adjFilePath");
        fileDatasize = Integer.parseInt(Tool.getProperty("fileDatasize"));
    }

    public void load(String[] args) {

        Statement stat = null;
        PreparedStatement ps = null;

        ResultSet iamRs = null;
        ResultSet detailRs = null;
        try {
            //Tool.setIamDsPool("jdbc");
//            iamConn = Tool.getIamConn();
            iamConn = DBConn.getDbusr01TestConn();
            //获取上月费用调减明细数据
//            String adjSql = "create table gm_adi as\n" +
//                    "      select adi.tracking_id,'0' as service_nbr,adi.product_line_id,adi.effective_date,adi.statement_date,adi.account_no,\n" +
//                    "      adi.bill_ref_no,adi.subscr_no,adi.expect_adj_fee\n" +
//                    "       from adj_detail_info@Zwdb_Prod adi \n" +
//                    "       where adi.product_line_id in ('8060','8059','8055','8047','8046','17764',\n" +
//                    "      '17763','17762','17761','17760','17759','13331','13329') \n" +
//                    "      and adi.exec_state=2 and adi.create_date >= TRUNC(add_months(trunc(sysdate),-1),'MM')\n" +
//                    "       and adi.create_date < TRUNC(trunc(sysdate),'MM')";
//            //表存在则删除    cld_serv_acc
//            SQLHelp.dropTable(iamConn,"gm_adi");
//            try{
//                ProcUtil.callProc(iamConn,"sql_procedure",  new Object[]{adjSql});
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//
//
//            //获取上月费用调增明细数据  //nrc_detail_info
//            String nrcSql = "create table gm_nrc as\n" +
//                    "      select adi.tracking_id,'0' as service_nbr,adi.product_line_id,adi.effective_date,adi.statement_date,adi.account_no,\n" +
//                    "      adi.bill_ref_no,adi.subscr_no,adi.expect_adj_fee\n" +
//                    "       from nrc_detail_info@Zwdb_Prod adi \n" +
//                    "       where adi.product_line_id in ('8060','8059','8055','8047','8046','17764',\n" +
//                    "      '17763','17762','17761','17760','17759','13331','13329') \n" +
//                    "      and adi.exec_state=2 and adi.create_date >= TRUNC(add_months(trunc(sysdate),-1),'MM')\n" +
//                    "       and adi.create_date < TRUNC(trunc(sysdate),'MM')";
//            SQLHelp.dropTable(iamConn,"gm_nrc");
//            try{
//                ProcUtil.callProc(iamConn,"sql_procedure",  new Object[]{nrcSql});
//            }catch (Exception e){
//                e.printStackTrace();
//            }


            String sql_1 = "select adi.tracking_id,b.service_nbr ,adi.product_line_id,adi.effective_date,adi.statement_date,adi.account_no,\n" +
                    "        adi.bill_ref_no,adi.subscr_no,adi.expect_adj_fee\n" +
                    "        from gm_adi adi left join tb_prd_prd_inst_21@hss_prod  b on adi.subscr_no = b.prd_inst_id\n" +
                    "        union\n" +
                    "        select adi.tracking_id,b.service_nbr ,adi.product_line_id,adi.effective_date,adi.statement_date,adi.account_no,\n" +
                    "        adi.bill_ref_no,adi.subscr_no,adi.expect_adj_fee\n" +
                    "        from gm_nrc adi left join tb_prd_prd_inst_21@hss_prod  b on adi.subscr_no = b.prd_inst_id";
            stat = iamConn.createStatement();
            System.out.println("sql_1>>>>>>>>>>>>" + sql_1);
            iamRs = stat.executeQuery(sql_1);
            List<InterNationAccAdjEntity> interList = new ArrayList<InterNationAccAdjEntity>();
            List<InterNationAccAdjEntity> interListDetail = new ArrayList<InterNationAccAdjEntity>();
            InterNationAccAdjEntity interNationAdjUploadEntity1 = null;
            while (iamRs.next()) {
                String trackingId = iamRs.getString("tracking_id");
                String serviceNbr = iamRs.getString("service_nbr");
                String productLineId = iamRs.getString("product_line_id");
                String effectiveDate = BaseUtils.Date2String(iamRs.getDate("effective_date"), "yyyyMMddHHmmss");
                String statementDate = BaseUtils.Date2String(iamRs.getDate("statement_date"), "yyyyMM");
                String accountNo = iamRs.getString("account_no");
                String billRefNo = iamRs.getString("bill_ref_no");
                String subscrNo = iamRs.getString("subscr_no");
                BigDecimal expectAdjFee = new BigDecimal(iamRs.getString("expect_adj_fee"));
                interNationAdjUploadEntity1 = new InterNationAccAdjEntity("PRVADJ" + getSerialNum(58, trackingId), serviceNbr, productLineId, effectiveDate, statementDate, accountNo, billRefNo, subscrNo, expectAdjFee);
                interList.add(interNationAdjUploadEntity1);
            }



            //根据分账序号、账单流水号、设备内部编号、账单显示项ID查询账单明细
            String sql = "select bid.sub_type_code,bid.amount,bill_invoice_row from bill_invoice_detail@Zwdb_Prod bid where bid.account_no=?" +
                    " and bid.bill_ref_no=? and bid.subscr_no=? and bid.product_line_id=?";
            System.out.println("bill_invoice_detail>>>>>>>>>>>>>" + sql);
            ps = iamConn.prepareStatement(sql);
            for (int i = 0; i < interList.size(); i++) {
                interNationAdjUploadEntity1 = interList.get(i);
                //System.out.println("))))))))))))))))))"+interNationAdjUploadEntity1.getExpectAdjFee());
                String productLineId = interNationAdjUploadEntity1.getProductLineId();
                //对于相重复的费用项ID需要查询账单明细表进行判断生成数据
                //调整判断和调减判断一致
                if (productLineId.equals("13329") || productLineId.equals("13331") || productLineId.equals("8046")
                        || productLineId.equals("8047") || productLineId.equals("8059") || productLineId.equals("8060")) {
                    ps.setString(1, interNationAdjUploadEntity1.getAccountNo());
                    ps.setString(2, interNationAdjUploadEntity1.getBillRefNo());
                    ps.setString(3, interNationAdjUploadEntity1.getSubscr_no());
                    ps.setString(4, interNationAdjUploadEntity1.getProductLineId());

                    detailRs = ps.executeQuery();
                    List<String> dlist = new ArrayList<String>();
                    while (detailRs.next()) {
                        String subTypeCode = detailRs.getString("sub_type_code");
                        String amount = detailRs.getString("amount");
                        String billInvoiceRow = detailRs.getString("bill_invoice_row");//取到账单行号避免数据重复
                        dlist.add(subTypeCode + "&" + amount + "&" + billInvoiceRow);
                    }
                    //这里只是对于增减判断
                    boolean flag = interNationAdjUploadEntity1.getExpectAdjFee().intValue() > 0 ? true : false;
                    //int expectAdjFee = Math.abs(Integer.parseInt(interNationAdjUploadEntity1.getExpectAdjFee()));
                    BigDecimal expectAdjFee = interNationAdjUploadEntity1.getExpectAdjFee();
                    int listSize = dlist.size();
                    if (listSize > 0) {
                        for (int j = 0; j < listSize; j++) {
                            /**
                             * 1、账单明细表金额大于费用表金额的话直接取费用表金额
                             * 2、账单明细表金额小于费用表金额则取账单明细表金额并依次比对，直到费用表金额等于账单明细表金额
                             * 3、流水号必须唯一
                             * */

                            String maxValue = getMaxValue(dlist);
                            System.out.println(maxValue);
                            String[] strArr = maxValue.split("&");
                            System.out.println("arr:" + Arrays.toString(strArr));
                            if (strArr.length >= 5) {
                                String CostId = strArr[0].trim();
                                //int amount = Math.abs(Integer.parseInt(strArr[1]));
                                BigDecimal amount = new BigDecimal(strArr[1]);
                                int index = Integer.parseInt(strArr[3]);

                                InterNationAccAdjEntity interNationAccAdj = new InterNationAccAdjEntity();
                                interNationAccAdj.setTrackingId(interNationAdjUploadEntity1.getTrackingId());
                                interNationAccAdj.setServiceNbr(interNationAdjUploadEntity1.getServiceNbr());
                                interNationAccAdj.setProductLineId(Tool.getProperty(CostId));
                                interNationAccAdj.setEffectiveDate(interNationAdjUploadEntity1.getEffectiveDate());
                                interNationAccAdj.setStatementDate(interNationAdjUploadEntity1.getStatementDate());
                                //进入二次比对  此时是同一列数据且ExpectAdjFee调账金额不一致
                                if (j >= 1) {
                                    //随机数生成两位+序列    之后减去最后三位就能查出TrackingId
                                    Random random = new Random(99);
                                    int rdm = random.nextInt(99);
                                    BigDecimal trackingId = new BigDecimal(interNationAccAdj.getTrackingId().substring(6));
                                    String str = trackingId.toString() + rdm + j;
                                    String trackid = getSerialNum(58, str);
                                    interNationAccAdj.setTrackingId("PRVADJ" + trackid);
                                }
                                if (amount.abs().compareTo(expectAdjFee.abs()) == 1) {//大于
                                    //interNationAdjUploadEntity2 = new InterNationAdjUploadEntity(trackingId,serviceNbr,"1200",Tool.getProperty(CostId),"-"+expectAdjFee,effectiveDate,statementDate,"1","");
                                    if (!flag) {
                                        interNationAccAdj.setExpectAdjFee(expectAdjFee.negate());
                                    } else {
                                        interNationAccAdj.setExpectAdjFee(expectAdjFee);
                                    }

                                    interListDetail.add(interNationAccAdj);
                                    break;
                                } else {
                                    if (amount.abs().compareTo(expectAdjFee.abs()) == 0) {//等于
                                        //interNationAdjUploadEntity2 = new InterNationAdjUploadEntity(trackingId,serviceNbr,"1200",Tool.getProperty(CostId),"-"+amount,effectiveDate,statementDate,"1","");
                                        if (!flag) {
                                            interNationAccAdj.setExpectAdjFee(amount.negate());
                                        } else {
                                            interNationAccAdj.setExpectAdjFee(amount);
                                        }
                                        interListDetail.add(interNationAccAdj);
                                        break;
                                    } else {//小于
                                        //interNationAdjUploadEntity2 = new InterNationAdjUploadEntity(trackingId,serviceNbr,"1200",Tool.getProperty(CostId),"-"+amount+"",effectiveDate,statementDate,"1","");
                                        if (!flag) {
                                            interNationAccAdj.setExpectAdjFee(amount.negate());
                                        } else {
                                            interNationAccAdj.setExpectAdjFee(amount);
                                        }
                                        interListDetail.add(interNationAccAdj);
                                        expectAdjFee = expectAdjFee.subtract(amount);
                                        dlist.remove(index);
                                    }

                                }
                            }
                        }
                    }

                } else {
                    //interNationAdjUploadEntity2 = new InterNationAdjUploadEntity(trackingId,serviceNbr,"1200",Tool.getProperty(productLineId),expectAdjFee+"",effectiveDate,statementDate,"1","");
                    interNationAdjUploadEntity1.setProductLineId(Tool.getProperty(productLineId));
                    interListDetail.add(interNationAdjUploadEntity1);
                }

            }

            List<String> fileList = writerFile(interListDetail);
//            for (int i = 0; i < fileList.size(); i++) {
//                Tool.writeInfoLog("interNationAccAdjUpload_info", "成功生成调账文件：" + fileList.get(i));
//            }

        } catch (Exception e) {
            e.printStackTrace();
//            Tool.writeInfoLog("interNationAccAdjUpload_error", "InterNationAdjUpload error msg:" + e.getLocalizedMessage());
        } finally {
            JdbcUtil.close(iamRs);
            JdbcUtil.close(stat);
            JdbcUtil.close(iamConn);
        }
    }


    /**
     * 把数据写入文件，通过列表数据和单个文件数据量判断依次生成文件
     */
    public List<String> writerFile(List<InterNationAccAdjEntity> Interlist) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = null;
        List<String> list = new ArrayList<String>();

        String date = BaseUtils.Date2String(new Date(), "yyyyMMdd");
		/*String year = BaseUtils.Date2String(new Date(), "yyyy");
		String month = BaseUtils.Date2String(new Date(), "MM");*/

        int filecount = Interlist.size() / fileDatasize;
        int surplus = Interlist.size() - (filecount * fileDatasize);
        if (surplus != 0) {
            filecount += 1;
        }

        int init = 0;
        int len = 0;
        //String ctxPath = adjFilePath+year+File.separator+month+File.separator;
        String ctxPath = adjFilePath;
        File file = new File(ctxPath);
        if (!file.exists()) {  //判断文件是否存在
            file.mkdirs();
        }
        System.out.println("file Path>>>>>>" + ctxPath);
        for (int i = 0; i < filecount; i++) {
            try {
                String num = getSerialNum(4, i + 1 + "");
                String wfile = ctxPath + "PRV_IOADJ_" + date + "021." + num + ".txt";
                writer = new PrintWriter(new File(wfile), "UTF-8");
                writer.println("10,46011021,46011000," + num + "," + BaseUtils.Date2String(new Date(), "yyyyMMddHHmmss") + ",10,");
                init = i * fileDatasize;
                len = i * fileDatasize + (fileDatasize - 1);
                list.add(wfile);
                BigDecimal sum = new BigDecimal(0);
                InterNationAccAdjEntity inter = null;
                if (i != (filecount - 1)) {
                    for (int j = init; j <= len; j++) {
                        inter = Interlist.get(j);
                        sum = sum.add(inter.getExpectAdjFee());
                        writer.println(inter.getTrackingId() + "," + inter.getServiceNbr() + ",1200," + inter.getProductLineId() +
                                "," + new DecimalFormat("0.000").format(inter.getExpectAdjFee()) + "," + inter.getEffectiveDate() + "," + inter.getStatementDate() + ",1,,");
                    }
                    writer.println("90,46011021,46011000," + num + "," + (len + 1 - init) + "," + new DecimalFormat("0.00").format(sum));
                } else {
                    for (int j = init; j < Interlist.size(); j++) {
                        inter = Interlist.get(j);
                        sum = sum.add(inter.getExpectAdjFee());
                        writer.println(inter.getTrackingId() + "," + inter.getServiceNbr() + ",1200," + inter.getProductLineId() + "," +
                                new DecimalFormat("0.000").format(inter.getExpectAdjFee()) + "," + inter.getEffectiveDate() + "," + inter.getStatementDate() + ",1,,");
                    }
                    writer.println("90,46011021,46011000," + num + "," + (Interlist.size() - init) + "," + new DecimalFormat("0.00").format(sum) + ",");
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                writer.close();
            }
        }
        return list;
    }

    //获取list中最大的值
    public String getMaxValue(List<String> list) {
        BigDecimal sum1 = new BigDecimal(0);
        String result = "";
        for (int i = 0; i < list.size(); i++) {
            String[] str = list.get(i).split("&");
            BigDecimal amount = new BigDecimal(str[1]);
            if (sum1.abs().compareTo(amount.abs()) == -1) {
                sum1 = amount;
                result = list.get(i) + "&" + i;
            }
        }
        return result;
    }

    //计算文件序号
    public String getSerialNum(int len, String cnum) {
        StringBuffer str = new StringBuffer();
        for (int i = len; i > cnum.length(); i--) {
            str.append("0");
        }
        return str.toString() + cnum;
    }

    public String getDate(String format, Object date) {
        if (format != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } else {
            return null;
        }
    }

    /**
     * 递归获取某路径下的所有文件，文件夹，并输出
     */
    public static void getFiles(String path) {
        File file = new File(path);
        // 如果这个路径是文件夹
        if (file.isDirectory()) {
            // 获取路径下的所有文件
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                // 如果还是文件夹 递归获取里面的文件 文件夹
                if (files[i].isDirectory()) {
                    System.out.println("目录：" + files[i].getPath());
                    getFiles(files[i].getPath());
                } else {
                    System.out.println("文件：" + files[i].getPath());
                }

            }
        } else {
            System.out.println("文件：" + file.getPath());
        }
    }

    public static void main(String[] args) {
        try {
             Tool = Properties2Help.init("vip/guoMan/interNationRoamingFile.properties");
            adjFilePath = Tool.getProperty("adjFilePath");
            fileDatasize = Integer.parseInt(Tool.getProperty("fileDatasize"));
//            String encoding = init.getProperty("encoding");
//            Tool.init("interNationRoamingFile");
//            Tool.writeInfoLog("interNationAccAdjUpload_info", "InterNationAccAdjUpload begin.......");
//            System.out.println("InterNationAccAdjUpload begin.......");
            new InterNationAccAdjUpload_new().load(args);
//            System.out.println("InterNationAccAdjUpload end.......");
//            Tool.writeInfoLog("interNationAccAdjUpload_info", "InterNationAccAdjUpload end.......");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }

    }



}
