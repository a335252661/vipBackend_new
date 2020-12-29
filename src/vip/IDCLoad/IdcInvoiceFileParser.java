/**
 * 
 */
package vip.IDCLoad;

import org.apache.commons.lang.StringUtils;
import vip.wlw.XxlJobLogger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Administrator
 *
 */
public class IdcInvoiceFileParser extends JtInvoiceFileParser {

	public IdcInvoiceFileParser(File theFile, String delimiter) {
		super(theFile,delimiter);
	}
	
	public void parser() {
		BufferedReader reader = null;
		int idx = 1;
		String[] lineArr = null;
		String line = null;
		JtBillData billData = null;
		try{
			reader = new BufferedReader(new FileReader(theFile));
			
			
			
			while((line = reader.readLine()) != null) {
				idx++;
				
				lineArr = line.split(delimiter,-1);
				if ("STA".equals(lineArr[0])) {
					fileHead = line;
				} else if ("END".equals(line)) {
					fileEnd = line;
					break;
				} else {
					billData = new JtBillData();
					billData.setOriginalData(line);
					billData.setFileName(theFile.getName());
					try {
						
						//1=记录ID 2=产品实例ID 3=设备号码 4=应收金额（含税） 5=归属帐期 6=业务省 7=本地网代码 8=帐目类型
						// 9=税金 10=应收金额（不含税）11= 税率 12= 增值税帐目（产品id） 13=税目 14= 权责发生年月 15= 群号 
						//16= 合同号 17= 项目编码 18= 备用字段1 19=备用字段2 20备用字段3

						if(theFile.getName().lastIndexOf("CRM2BILL.ZD") == 0){
							billData.setFileType(1);
						} else if(theFile.getName().lastIndexOf("CRM2BILL.ADJ") == 0) {
							billData.setFileType(2);
						}
						
						billData.setFileLineId(Long.parseLong(lineArr[0]));
						
						//2=产品实例ID
						if(StringUtils.isEmpty(lineArr[1])) {
							errCode = "1002";
							billData.setErrCode("1002");
							throw new Exception("");
						} else {
							billData.setAcctId(lineArr[1]);
						}
						
						//3=设备号码
						if(StringUtils.isEmpty(lineArr[2])) {
							errCode = "1003";
							billData.setErrCode("1003");
							throw new Exception("");
						} else {
							billData.setServId(lineArr[2]);
						}
						
						// 4=应收金额（含税）
						if(StringUtils.isEmpty(lineArr[3])
								|| Long.parseLong(lineArr[3]) <= 0) {
							errCode = "1004";
							billData.setErrCode("1004");
							throw new Exception("");
						} else {
							billData.setAmount(Long.parseLong(lineArr[3]));
						}
						int index = 3;
						//调帐前帐期
						if(2 == billData.getFileType()) {
							index++;
							
							if(StringUtils.isEmpty(lineArr[index])
									||!lineArr[index].matches("\\d{4}[0-1]{1}\\d{1}")) {
								errCode = "1014";
								billData.setErrCode("1014");
								throw new Exception("");
							} else {
								billData.setPreAdjMonth(lineArr[index]);
							}
						}
						
						//5=归属帐期
						index++;
						if(StringUtils.isEmpty(lineArr[index])
								||!lineArr[index].matches("\\d{4}[0-1]{1}\\d{1}")) {
							errCode = "1005";
							billData.setErrCode("1005");
							throw new Exception("");
						} else {
							billData.setBillingMonth(lineArr[index]);
//							try {
//								BaseUtil.formatDate(lineArr[4],BaseUtil.MONTH_PATTERN1);
//								billData.setBillingMonth(lineArr[4]);
//							} catch(Exception ex1) {
//								errCode = "1005";
//								billData.setErrCode("1005");
//							}
						}
						
						
						
						//6=业务省
						index++;
						if(!"021".equals(lineArr[index])) {
							errCode = "1006";
							billData.setErrCode("1006");
							throw new Exception("");
						} else {
							billData.setCity(lineArr[index]);
						}
						
						//7=本地网代码
						index++;
						if(!"021".equals(lineArr[index])) {
							errCode = "1007";
							billData.setErrCode("1007");
							throw new Exception("");
						} else {
							billData.setAeraCode(lineArr[index]);
						}
						
						//8=帐目类型
						index++;
						
							billData.setSubTypeCode(lineArr[index]);
						
						
						//9=税金
						index++;
						if(StringUtils.isEmpty(lineArr[index])
								|| Long.parseLong(lineArr[index]) <= 0) {
							//只有帐单文件判断
							if(1 == billData.getFileType()) {
								errCode = "1009";
								billData.setErrCode("1009");
								throw new Exception("");
							}
							
						} else {
							billData.setTaxAmount(Long.parseLong(lineArr[index]));
						}
						
						//10=应收金额（不含税）
						index++;
						if(StringUtils.isEmpty(lineArr[index])
								|| Long.parseLong(lineArr[index]) <= 0) {
							errCode = "1010";
							billData.setErrCode("1010");
							throw new Exception("");
						} else {
							billData.setAfterTaxAmount(Long.parseLong(lineArr[index]));
						}
						
						//11= 税率
						index++;
						if(StringUtils.isEmpty(lineArr[index])
								|| Long.parseLong(lineArr[index]) <= 0) {
							errCode = "1011";
							billData.setErrCode("1011");
							throw new Exception("");
						} else {
							billData.setTax(Long.parseLong(lineArr[index]));
						}
						
						//12=增值税帐目（产品id）
						index++;
//						if(!"13409782".equals(lineArr[index])	//云堤
//							&& !"13409980".equals(lineArr[index])   //医疗影像云
//						 	&& !"13410344".equals(lineArr[index])	//机架出租
//						 	&& !"13410345".equals(lineArr[index])	//机位出租
//						 	&& !"13410346".equals(lineArr[index])	//VIP 机房出租
//						 	&& !"13410347".equals(lineArr[index])	//电力出租
//						 	&& !"13410348".equals(lineArr[index])	//工作附属区出租
//						 	&& !"13410349".equals(lineArr[index])	//数据中心互联网带宽出租
//						 	&& !"13410353".equals(lineArr[index])	//IP 地址出租
//							 	) {
//							errCode = "1012";
//							billData.setErrCode("1012");
//							throw new Exception("产品ID不存在");
//						} else {
							billData.setProductId(lineArr[index]);
//						}
						//13=税目
						index++;
						if("".equals(lineArr[index]) || lineArr[index]==null) {
							errCode = "1013";
							billData.setErrCode("1013");
							throw new Exception("");
						} else {
							billData.setTaxCode(Long.parseLong(lineArr[index]));
						}
						//14= 权责发生年月
						index++;
						if("".equals(lineArr[index]) || lineArr[index]==null) {
							billData.setAccrualDate("");
						} else {
							billData.setAccrualDate(lineArr[index]);
						}
						//15=群组号
						index++;
//						if("".equals(lineArr[index]) || lineArr[index]==null) {
//							errCode = "1015";
//							billData.setErrCode("1015");
//							throw new Exception("");
//						} else {
							billData.setGroupNo(lineArr[index]);
//						}
						//16=合同号
						index++;
//						if("".equals(lineArr[index]) || lineArr[index]==null) {
//							billData.setContractNo("");
//						} else {
							billData.setContractNo(lineArr[index]);
//						}
						//17=项目编码
						index++;
						if("".equals(lineArr[index]) || lineArr[index]==null) {
							billData.setProjectCode("");
						} else {
							billData.setProjectCode(lineArr[index]);
						}
						//18=备用字段1
						index++;
						if("".equals(lineArr[index]) || lineArr[index]==null) {
							billData.setSpareField1("");
						} else {
							billData.setSpareField1(lineArr[index]);
						}
						//19=备用字段2
						index++;
						if("".equals(lineArr[index]) || lineArr[index]==null) {
							billData.setSpareField2("");
						} else {
							billData.setSpareField2(lineArr[index]);
						}
						//20=备用字段3
						index++;
						if("".equals(lineArr[index]) || lineArr[index]==null) {
							billData.setSpareField3("");
						} else {
							billData.setSpareField3(lineArr[index]);
						}
						
					} catch(Exception ex1){
						ex1.printStackTrace();
						errCode = "1001";
						billData.setErrCode("1001");
					}
					billDatas.add(billData);
				}
			}
			
		} catch(Exception ex) {
			this.setErrCode("1001");
			this.setErrDesc(ex.getMessage());
			ex.printStackTrace();
			XxlJobLogger.log("JtInvoiceFileParser error msg:" + ex.getLocalizedMessage() + " fileName=" + theFile.getName());
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
