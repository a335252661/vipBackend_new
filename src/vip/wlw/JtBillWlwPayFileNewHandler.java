package vip.wlw;

import com.alibaba.fastjson.JSONArray;
//import com.xxl.job.core.biz.model.ReturnT;
//import com.xxl.job.core.handler.IJobHandler;
//import com.xxl.job.core.handler.annotation.JobHandler;
//import com.xxl.job.core.log.XxlJobLogger;
//import com.xxl.job.executor.core.util.HttpClientHelps;
//import com.xxl.job.executor.core.util.UtilTools;
//import com.xxl.job.executor.dto.BillInvoiceDTO;
//import com.xxl.job.executor.entity.JtBillData;
//import com.xxl.job.executor.entity.JtBillDataInvoice;
//import com.xxl.job.executor.entity.PayInterimBillEntity;
//import com.xxl.job.executor.entity.SystemParameters;
//import com.xxl.job.executor.utils.*;
import helps.FileHelp;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpMethod;
//import org.springframework.stereotype.Component;
import utils.DBConn;
import utils.FtpUtil;

import javax.sql.DataSource;
import java.io.File;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * 物联网销账上传
 * @author guocj
 *
 */
public class JtBillWlwPayFileNewHandler {

	//最后一次程序是否执行完成 true为执行完毕 false为还在执行
	private volatile boolean isFinish = true;

//    @Autowired
//    private HttpClientHelps httpClientUtils;
    
//	@Value("${acctUrls.biz.bill.payInoviceFindBybrn:}")
//    private String acct_biz_bill_payInoviceFindBybrn;

	
//	// 定时线程对象
//	private ScheduledExecutorService scheduler;
//	
//	//首次执行延迟时间(秒)  
//	private long initialDelay;
//	
//	//执行周期(秒) 900s执行一次
//	private long period;
	
//	@Value("${wlwPay.serviceType}")
	private String serviceType ="1";
	
//	@Value("${wlwPay.payFilePath}")
	private String payFilePath ="/acct/acct_other/vip_backend/wlw/";
	
//	@Value("${wlwPay.tmpPayFilePath}")
	private String tmpPayFilePath ="/acct/acct_other/vip_backend/wlw/tmp/";
	
//	@Value("${wlwPay.tmpPayFilePath1}")
	private String tmpPayFilePath1 ="/acct/acct_other/vip_backend/wlw/tmp1/";
	
//	@Value("${wlwPay.delimiter}")
	private String delimiter ="|";
	
//	@Value("${wlwPay.fileCount}")
	private long fileCount=100000;
	

	
	private int module = 1 ;   
	private int modulePart = 8;	
	private int partPayModule = 7;
	
	private String parameterName ;
	private String chkParameterName ;
	private String yyyyMMdd = null;

	public static void main(String[] args) {
		new JtBillWlwPayFileNewHandler().run();
	}
	
	
	public void run(){
		String serviceName = null;

		parameterName = "JTBILL_FILENUM_" + this.serviceType;
		chkParameterName = "JTCHK_FILENUM_" + this.serviceType;
		if(isFinish){
			if("1".equals(serviceType)){
				serviceName = "BILL2IOT";
				yyyyMMdd = BaseUtil.getCurrentDate(BaseUtil.DATE_PATTERN1);
			} else {
				serviceName = "BILL2CRM";
				yyyyMMdd = BaseUtil.addHours(BaseUtil.MONTH_PATTERN1,12);
			}

			isFinish = false;
			Connection iamRepConn = null;
			Connection mysqlConn = null ;
			PayInterimBillEntity interimBill = null;
			BillInvoiceDTO invoice = null;
			List<JtBillData> billDatas = null;
			List<String> contents = null;
			List<String> contents2 = null;
			int index = 0;
			int index2 = 0;

			List<List<String>> files = new ArrayList<List<String>>();
			List<List<String>> files2 = new ArrayList<List<String>>();
			try{
//				iamRepConn = oracleCopyDataSource.getConnection() ;
//				mysqlConn = mysqlDataSource.getConnection() ;

				iamRepConn = DBConn.getCopyProConn();
				mysqlConn = DBConn.getCopyMySQLProConn() ;



//
//
				iamRepConn.setAutoCommit(false);
				mysqlConn.setAutoCommit(false);
//				XxlJobLogger.log("跟新service_type===================start");
//				String queryseq = "select distinct a.pay_interim_seq, b.service_type\n" +
//						"  from JT_BILL_DATA_INVOICE a, jt_bill_data b\n" +
//						" where  a.pay_interim_seq = b.pay_interim_seq and a.service_type=999";
//				ResultSet queryseq_payResult = iamRepConn.createStatement().executeQuery(queryseq);
//				while (queryseq_payResult.next()){
//					String pay_interim_seq = queryseq_payResult.getString("pay_interim_seq");
//					String service_type =queryseq_payResult.getString("service_type");
//					String up = "update JT_BILL_DATA_INVOICE set service_type='"+service_type+"' where pay_interim_seq='"+pay_interim_seq+"'";
//					PreparedStatement pre = iamRepConn.prepareStatement(up);
//					pre.execute();
//					pre.close();
//				}
//				XxlJobLogger.log("跟新service_type===================end");
//
//
//				List<JtBillDataInvoice> invs = JtBillDataInvoice.queryLoad(iamRepConn, serviceType, null, null);
//				if ("4".equals(this.serviceType)) {
//					List<JtBillDataInvoice> invs5 = JtBillDataInvoice.queryLoad(iamRepConn, "5", (String)null, (String)null);//云堤
//					List<JtBillDataInvoice> invs6 = JtBillDataInvoice.queryLoad(iamRepConn, "6", (String)null, (String)null);//云录音
//					invs.addAll(invs5);
//					invs.addAll(invs6);
//				}
//				for(JtBillDataInvoice inv:invs){
//					//0:新建 1:账单生成 2：未销账，3：已销帐，4：返销账
//					if(0 == inv.getStatus()) {
//						if(5==inv.getServiceType() || 6==inv.getServiceType()){
//							if(StringUtils.isNotEmpty(inv.getBillRefNo())){
//								inv.setBillRefNo(inv.getBillRefNo());
//								inv.setStatus(1);
//							}
//						}else{
//							//判断是否生成账单
//							interimBill = PayInterimBillEntity.query(inv.getPayInterimSeq());
//							if(null != interimBill && 13 == interimBill.getStatusCd()){
//								inv.setBillRefNo(interimBill.getBillRefNo());
//								inv.setStatus(1);
//							}
//						}
//					} else if(1 != inv.getStatus() && 2 != inv.getStatus() && 4 != inv.getStatus()) {
//						//判断是否销帐
//						if (3 == inv.getStatus()) {
//							invoice = queryInvoiceByBillRef(inv.getBillRefNo());
//							if (invoice != null && 0L != invoice.getBalanceDue()) {
//								inv.setStatus(4);
//								inv.setLoadFlag(1);
//							}
//						}
//					} else if(StringUtils.isNotEmpty(inv.getBillRefNo())) {
//						//判断是否返销帐
//						invoice = queryInvoiceByBillRef(inv.getBillRefNo());
//						if (invoice != null) {
//							if (0L == invoice.getBalanceDue()) {
//								inv.setStatus(3);
//								inv.setLoadFlag(1);
//								String postDate = BaseUtil.Date2String(invoice.getCloseDate(), BaseUtil.DATETIME_PATTERN) ;
//								inv.setPostDate(postDate);
//							} else if (4 != inv.getStatus()) {
//								inv.setStatus(2);
//							}
//						}
//					}
//					inv.update(iamRepConn);
//				}
//				iamRepConn.commit();

				XxlJobLogger.log("销账表跟新完毕,开始生成销账文件");
				System.out.println("销账表跟新完毕,开始生成销账文件");
				contents = new ArrayList<String>();
				contents2 = new ArrayList<String>();


				String pay = "  select rownum  id ,b.PAY_INTERIM_SEQ, a.acct_id ,'-1' serv_id,a.serv_id as SERV,'021' AS SH , a.BILLING_MONTH ,\n   a.SUB_TYPE_CODE , a.AMOUNT ,to_char(b.post_date , 'yyyymmddhh24miss') postdate,\n   case when b.status =3 then '5JB'\n     else '5JA'  END AS STATUS\n  from jt_bill_data a, JT_BILL_DATA_INVOICE b\n  where a.PAY_INTERIM_SEQ = b.PAY_INTERIM_SEQ\n  and b.load_flag=1\n  and a.EXPENSES_ID  is  null  and b.SERVICE_TYPE = " + this.serviceType;
				String partpay = "  select rownum  id ,b.PAY_INTERIM_SEQ, a.acct_id ,'-1' serv_id,a.serv_id as SERV,'021' AS SH , a.BILLING_MONTH ,\n   a.SUB_TYPE_CODE , a.AMOUNT ,to_char(b.post_date , 'yyyymmddhh24miss') postdate,\n   case when b.status =3 then '5JB'\n     else '5JA'  END AS STATUS\n   , a.EXPENSES_ID , \n   '021' || to_char(sysdate , 'yyyymmddhh24miss') || lpad(rownum,8,'0') || '00' as xznum \n  from jt_bill_data a, JT_BILL_DATA_INVOICE b\n  where a.PAY_INTERIM_SEQ = b.PAY_INTERIM_SEQ\n  and b.load_flag=1\n  and a.EXPENSES_ID  is not null and b.SERVICE_TYPE = " + this.serviceType;
				List<String> payList = Arrays.asList("id", "acct_id", "serv_id", "SERV", "sh", "BILLING_MONTH", "SUB_TYPE_CODE", "AMOUNT", "postdate", "STATUS");
				List<String> partpayList = Arrays.asList("id", "acct_id", "serv_id", "SERV", "sh", "BILLING_MONTH", "SUB_TYPE_CODE", "AMOUNT", "postdate", "STATUS", "EXPENSES_ID", "xznum");
				HashSet<String> hashSet = new HashSet();

				Statement state = iamRepConn.createStatement();
				ResultSet payResult = state.executeQuery(pay);
				while (payResult.next()){
					StringBuffer content = new StringBuffer();
					for(String comment : payList){
						String value  = payResult.getString(comment);
						content.append(value);
						content.append(this.delimiter);
					}
					hashSet.add(payResult.getString("PAY_INTERIM_SEQ"));
					contents.add(content.toString());
				}
				System.out.println("pay  执行结束");
//				if(null!=state){
//					state.close();
//				}
//				if(null!=payResult){
//					payResult.close();
//				}


				Statement state2 = iamRepConn.createStatement();
				ResultSet payResult2 = state2.executeQuery(partpay);
				while (payResult2.next()){
					StringBuffer content2 = new StringBuffer();
					for(String comment : partpayList){
						String value2  = payResult2.getString(comment);
						content2.append(value2);
						content2.append(this.delimiter);
					}
					hashSet.add(payResult2.getString("PAY_INTERIM_SEQ"));
					contents2.add(content2.toString());
				}
				System.out.println("partpay  执行结束");
//				if(null!=state){
//					state2.close();
//				}
//				if(null!=payResult){
//					payResult2.close();
//				}




				iamRepConn.commit();

				if(contents.size() > 0){
					files.add(contents);
				}

				if(contents2.size() > 0){
					files2.add(contents2);
				}

				Long chkParam = SystemParameters.queryIntValue(mysqlConn, module, chkParameterName, yyyyMMdd);
				int chkFileNum = 0;
				if(null != chkParam){
					chkFileNum = chkParam.intValue();
				}
				++chkFileNum;
				String chkFileName = tmpPayFilePath1 + serviceName +".SUM."+yyyyMMdd+"." +BaseUtil.leftPad(Integer.toString(chkFileNum), 3, "0") +".021";
				Long param = SystemParameters.queryIntValue(mysqlConn, module, parameterName, yyyyMMdd);
				int fileNum = 0;
				if(null != param){
					fileNum = param.intValue();
				}

				Long paramPart = SystemParameters.queryIntValue(mysqlConn, modulePart, parameterName, yyyyMMdd);
				int fileNumPart = 0;
				if(null != paramPart){
					fileNumPart = paramPart.intValue();
				}

				if(files.size() > 0 && files2.size()>0) {
					int fs1=files.size();
					int fs2=files2.size();
					int fs=fs1+fs2;
					Tool.writeFile(chkFileName, "STA|"+fs, true);

					for(List<String> ct:files){
						++fileNum;
						//BILL2CRM.PAY.YYYYMM.NNN.ZZZ
						String fileName = serviceName + ".PAY."+yyyyMMdd+"."
								+BaseUtil.leftPad(Integer.toString(fileNum), 3, "0")
								+".021";

						Tool.writeFile(tmpPayFilePath + fileName, "STA|"+ct.size()+"|", true);
						Tool.writeFile(tmpPayFilePath + fileName, ct, true);
						Tool.writeFile(tmpPayFilePath + fileName, "END|", true);
						File fileLengs=new File(tmpPayFilePath+fileName);
						Long ff=fileLengs.length();
						Tool.writeFile(chkFileName, fileName + "|"+ff, true);
					}

					for(List<String> ct:files2){
						++fileNumPart;
						//BILL2CRM.PAY.YYYYMM.NNN.ZZZ
						String fileName = serviceName + ".PARTPAY."+yyyyMMdd+"."
								+BaseUtil.leftPad(Integer.toString(fileNumPart), 3, "0")
								+".021";


						Tool.writeFile(tmpPayFilePath + fileName, "STA|"+ct.size()+"|", true);
						Tool.writeFile(tmpPayFilePath + fileName, ct, true);
						Tool.writeFile(tmpPayFilePath + fileName, "END|", true);
						File fileLengs=new File(tmpPayFilePath+fileName);
						Long ff=fileLengs.length();
						Tool.writeFile(chkFileName, fileName + "|"+ff, true);
					}

				}else if(files.size() > 0 && files2.size()<=0){
					int fs1=files.size();
					int fs2=1;
					int fs=fs1+fs2;
					Tool.writeFile(chkFileName, "STA|"+fs, true);

					for(List<String> ct:files){
						++fileNum;
						//BILL2CRM.PAY.YYYYMM.NNN.ZZZ
						String fileName = serviceName + ".PAY."+yyyyMMdd+"."
								+BaseUtil.leftPad(Integer.toString(fileNum), 3, "0")
								+".021";

						Tool.writeFile(tmpPayFilePath + fileName, "STA|"+ct.size()+"|", true);
						Tool.writeFile(tmpPayFilePath + fileName, ct, true);
						Tool.writeFile(tmpPayFilePath + fileName, "END|", true);

						File fileLengs=new File(tmpPayFilePath+fileName);
						Long ff=fileLengs.length();
						Tool.writeFile(chkFileName, fileName + "|"+ff, true);
					}
					++fileNumPart;
					//BILL2CRM.PAY.YYYYMM.NNN.ZZZ
					String fileName = serviceName + ".PARTPAY."+yyyyMMdd+"."
							+BaseUtil.leftPad(Integer.toString(fileNumPart), 3, "0")
							+".021";

					Tool.writeFile(tmpPayFilePath + fileName, "STA|0|", true);
					Tool.writeFile(tmpPayFilePath + fileName, "END|", true);

					Tool.writeFile(chkFileName, fileName + "|1", true);


				}else if(files.size() <= 0 && files2.size() >0){
					int fs1=1;
					int fs2=files2.size();
					int fs=fs1+fs2;
					Tool.writeFile(chkFileName, "STA|"+fs, true);
					for(List<String> ct:files2){
						++fileNumPart;
						//BILL2CRM.PARTPAY.YYYYMM.NNN.ZZZ
						String fileName = serviceName + ".PARTPAY."+yyyyMMdd+"."
								+BaseUtil.leftPad(Integer.toString(fileNumPart), 3, "0")
								+".021";


						Tool.writeFile(tmpPayFilePath + fileName, "STA|"+ct.size()+"|", true);
						Tool.writeFile(tmpPayFilePath + fileName, ct, true);
						Tool.writeFile(tmpPayFilePath + fileName, "END|", true);

						File fileLengs=new File(tmpPayFilePath+fileName);
						Long ff=fileLengs.length();
						Tool.writeFile(chkFileName, fileName + "|"+ff, true);
					}
					++fileNum;
					//BILL2CRM.PAY.YYYYMM.NNN.ZZZ
					String fileName = serviceName + ".PAY."+yyyyMMdd+"."
							+BaseUtil.leftPad(Integer.toString(fileNum), 3, "0")
							+".021";
					Tool.writeFile(tmpPayFilePath + fileName, "STA|0|", true);
					Tool.writeFile(tmpPayFilePath + fileName, "END|", true);

					Tool.writeFile(chkFileName, fileName + "|1", true);

				}else  {
					++fileNum;
					//BILL2CRM.PAY.YYYYMM.NNN.ZZZ
					String fileName = serviceName + ".PAY."+yyyyMMdd+"."
							+BaseUtil.leftPad(Integer.toString(fileNum), 3, "0")
							+".021";
					Tool.writeFile(chkFileName, "STA|2", true);
					Tool.writeFile(tmpPayFilePath + fileName, "STA|0|", true);
					Tool.writeFile(tmpPayFilePath + fileName, "END|", true);
					Tool.writeFile(chkFileName, fileName + "|1", true);


					++fileNumPart;
					String fileName2 = serviceName + ".PARTPAY."+yyyyMMdd+"."
							+BaseUtil.leftPad(Integer.toString(fileNumPart), 3, "0")
							+".021";
					Tool.writeFile(tmpPayFilePath + fileName2, "STA|0|", true);
					Tool.writeFile(tmpPayFilePath + fileName2, "END|", true);

					Tool.writeFile(chkFileName, fileName2 + "|1", true);
				}



				Tool.writeFile(chkFileName, "END", true);
				System.out.println("hashSet.size() ===" + hashSet.size());
				XxlJobLogger.log("hashSet.size() ===" + hashSet.size());
				if (hashSet.size() > 0) {
					String update = "update  JT_BILL_DATA_INVOICE set load_flag ='2' , CHG_DATE = sysdate where PAY_INTERIM_SEQ in (?)";
					String newSql = String.format(update.replace("?", "%s"), toSQLin(hashSet.toArray()));
					this.updateSQL(iamRepConn, newSql);
					System.out.println("跟新  load_flag  结束");
					XxlJobLogger.log("跟新  load_flag  结束");
				}
				//将pay文件移到上传目录中
//				FileUtil.moveAllFile(tmpPayFilePath, payFilePath);
				FileHelp.dirToDir(tmpPayFilePath, payFilePath);

				//程序休眠30秒
				Thread.sleep(10000);

				//将sum文件移到上传目录中
//				FileUtil.moveAllFile(tmpPayFilePath1, payFilePath);
				FileHelp.dirToDir(tmpPayFilePath1, payFilePath);

				//更新帐单文件序号
				updateParam(mysqlConn,module,parameterName,String.valueOf(fileNum),yyyyMMdd,param);
				updateParam(mysqlConn,modulePart,parameterName,String.valueOf(fileNumPart),yyyyMMdd,paramPart);

				//更新稽核单文件序号
				updateParam(mysqlConn,module,chkParameterName,String.valueOf(chkFileNum),yyyyMMdd,chkParam);
				mysqlConn.commit();


//				System.out.println("77---》75  移动文件");
//				FtpUtil ftp = FtpUtil.connect("10.145.195.75","acct_pay","Pay!3#we",
//						"/acct/acct_payment/JtBill/data/wlw/pay");
//				ArrayList<String> currentFileAllLocation = FileHelp.getCurrentFileAllLocation("/acct/acct_other/vip_backend/wlw", "BILL.*");
//				for(String name :currentFileAllLocation){
//					File currfile = new File(name);
//					System.out.println(currfile.getName());
//					ftp.uploadFile(currfile, currfile.getName());
//				}
//				System.out.println("77---》75  移动完成");

			} catch(Exception ex) {
				System.out.println(ex.getMessage());
				XxlJobLogger.log("XxlJobLogger.log  err:"+ex.getMessage());
				ex.printStackTrace();
				try {
					mysqlConn.rollback();
					iamRepConn.rollback();
				} catch (SQLException e) {}
			} finally {
				isFinish = true;
				WlwJdbcUtil.close(mysqlConn);
				WlwJdbcUtil.close(iamRepConn);
			}
		}

	}

	private void updateParam(Connection mysqlConn, int module, String parameterName, String fileNum, String yyyyMM, Long param) throws Exception {
		if(null == param){
			SystemParameters.insert(mysqlConn, module, parameterName, fileNum, yyyyMM);
		} else {
			SystemParameters.update(mysqlConn, module, parameterName, fileNum, yyyyMM);
		}
	}

	private String getXZNum(Connection mysqlConn) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar calendar = Calendar.getInstance();
		String dateString = df.format(calendar.getTime());
		String randStr = "";
		Long param = SystemParameters.queryIntValue(mysqlConn, partPayModule, parameterName, yyyyMMdd);
		int partNum = 0;
		if (null != param) {
			partNum = param.intValue();
		}
		randStr = String.format("%08d", partNum);
		++partNum;
		updateParam(mysqlConn, partPayModule, parameterName, String.valueOf(partNum), yyyyMMdd, param);

		return "021" + dateString + randStr + "00";
	}
	
	
//	public BillInvoiceDTO queryInvoiceByBillRef(String billRefNo) {
//		   Map<String, String> map = new HashMap<String, String>();
//	        map.put("billRefNo", billRefNo);
//	        map.put("requestSeq", UtilTools.generateRequestId());
//	        map.put("requestTime", BaseUtil.format(new Date(), BaseUtil.DATETIME_PATTERN));
//
//	        String jsonArrayStr = httpClientUtils.exchange(acct_biz_bill_payInoviceFindBybrn, HttpMethod.GET, null, String.class, map);
//	        List<BillInvoiceDTO> tbPrdList = JSONArray.parseArray(jsonArrayStr, BillInvoiceDTO.class);
//	        if(tbPrdList != null && tbPrdList.size() > 0) {
//	        	return tbPrdList.get(0) ;
//	        }
//	        return null ;
//	}
	
	
	/**
	 * 获取批次号
	 */
	public static String getAuditRecordNum() {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(Calendar.HOUR_OF_DAY, 0);
		calendar1.set(Calendar.MINUTE, 0);
		calendar1.set(Calendar.SECOND, 0);
		long timeLong1 = calendar1.getTimeInMillis();
		
		long timeLong2 = Calendar.getInstance().getTimeInMillis();
		long rs = (timeLong2 - timeLong1) / 900000;
		String recordNum = String.valueOf(rs);
		if (rs == 0) {
			recordNum = "96";
		}
		int strLenth = recordNum.length();
		for (int i = 0; i < (8 - strLenth); i++) {
			recordNum = "0" + recordNum;
		}
		return recordNum;
	}

	public static String toSQLin(Object... objects) {
		StringBuilder sb = new StringBuilder();

		for(int i = 0; i < objects.length; ++i) {
			if (i == objects.length - 1) {
				sb.append("'");
				sb.append(objects[i]);
				sb.append("'");
			} else {
				sb.append("'");
				sb.append(objects[i]);
				sb.append("'");
				sb.append(",");
			}
		}

		return sb.toString().trim();
	}

	public void updateSQL(Connection conn, String sqlStr) {
		PreparedStatement pre = null;

		try {
			pre = conn.prepareStatement(sqlStr);
			System.out.println(sqlStr);
			pre.execute();
			conn.commit();
		} catch (Exception var13) {
			var13.printStackTrace();
		} finally {
			try {
				if (pre != null) {
					pre.close();
				}
			} catch (SQLException var12) {
				var12.printStackTrace();
			}

		}

	}
	    
	
//	public void startService(){
//		this.scheduler.scheduleAtFixedRate(this,this.initialDelay,this.period,TimeUnit.SECONDS);
//	}
//	
	





}
