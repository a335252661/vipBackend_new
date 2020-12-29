/**
 * 
 */
package vip.IDCLoad;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 *
 */
public class IdcCheckFileParser {
	
	private File aFile;
	
	private String filePath;
	
	private String delimiter;
	
	private String billFileRegExp;
	
	private JtBillLog checkFile = new JtBillLog();
	
	private List<JtBillLog> files = new ArrayList<JtBillLog>();

	private String serviceType;
	
	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public JtBillLog getCheckFile() {
		return checkFile;
	}

	public void setCheckFile(JtBillLog checkFile) {
		this.checkFile = checkFile;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	
	public List<JtBillLog> getFiles() {
		return files;
	}

	public void setFiles(List<JtBillLog> files) {
		this.files = files;
	}

	public IdcCheckFileParser(File aFile, String delimiter, String filePath, String billFileRegExp, String serviceType){
		this.aFile = aFile;
		this.delimiter = delimiter;
		this.filePath = filePath;
		this.billFileRegExp = billFileRegExp;
		this.serviceType = serviceType;
	}
	
//	表格 26：	STA|1
//	表格 27：	WXTXBILL.ZD.001.551|400
//	表格 28：	WXTXAPPORTION.ZD.002.551|400
//	表格 29：	END
	
	public void parser() {
		BufferedReader reader = null;
		int idx = 0;
		String[] lineArr = null;
		JtBillLog log = null;
		String fileName = null;
		long fileSize = 0;
		String errCode = "0";
		String errDesc = null;
		boolean isEnd = false;
		try{
			reader = new BufferedReader(new FileReader(aFile));
			String line = null;
//			String line = reader.readLine();
//			this.setHeadData(line);
//			
//			//文件头定义 TOTAL_ AMOUNT | TOTAL_COUNT | BEGIN_OPER_ID | END_OPER_ID
//			String[] data = line.split(this.delimiter);
//			this.setTotalAmount(Long.parseLong(data[0]));
//			this.setTotalCount(Long.parseLong(data[1]));
			checkFile.setFileName(aFile.getName());
			checkFile.setFileType(1);
			
			List<String> fileNames = FileUtil.getFileNames(filePath,billFileRegExp);
			while((line = reader.readLine()) != null) {
				idx++;
				lineArr = line.split(delimiter);
				if (1 == idx) {
					if(!"STA".equals(lineArr[0])){
						checkFile.setErrCode("F8008");
						//throw new Exception("稽核文件和帐单文件不匹配!");
					}
					checkFile.setFileSize(Integer.parseInt(lineArr[1].trim()));
				} else if ("END".equals(line)) {
					isEnd = true;
					break;
				} else {
					if(lineArr.length < 2){
						continue;
					}
					//CRM2BILL.ZD.YYYYMM.NNN.ZZZ
					fileName = lineArr[0];
					fileSize = Long.parseLong(lineArr[1].trim());
					String[] tmp = fileName.split("\\.");
					
					log = new JtBillLog();
					//log.setStatus(0);
					log.setFileName(fileName);
					//log.setFileSize(fileSize);
					try{
						
						/*if(fileName.lastIndexOf("CRM2BILL.ZD") != 0){
							log.setErrCode("F8001");
							throw new Exception("文件名格式错误!");
						}*/
						if(fileName.lastIndexOf("CRM2BILL.ZD") != 0
								&& fileName.lastIndexOf("CRM2BILL.ADJ") != 0){
							log.setErrCode("F8001");
							throw new Exception("文件名格式错误!");
						}
						
						if(!tmp[2].matches("\\d{4}[0-1]{1}\\d{1}")){
							log.setErrCode("F8002");
							throw new Exception("文件名中的日期不合法!");
						}
						
						if(tmp[3].length() != 3){
							log.setErrCode("F8003");
							throw new Exception("文件名序号不正确!");
						} else {
							try {
								Long.parseLong(tmp[3]);
							} catch(Exception ex1) {
								log.setErrCode("F8003");
								throw new Exception("文件名序号不正确!");
							}
						}
						
						if(!"021".equals(tmp[4])){
							log.setErrCode("F8004");
							throw new Exception("文件不属于上传省!");
						}
						
						if(fileName.lastIndexOf("CRM2BILL.ZD") == 0){
							log.setFileType(2);
						} 
						
						if(!fileNames.contains(fileName)){
							log.setErrCode("F8007");
							errCode = "F8007";
							errDesc = "未获取到文件（文件不存在）";
						} else {
							parserInvFile(log);
						}
//						if(!fileName.matches(this.getBillFileRegExp())){
//							this.setErrorCode("F8008");
//							throw new Exception("第一行没有STA!");
//						}
						
						
					} catch(Exception ex2) {
						log.setErrDesc(ex2.getMessage());
					}
					files.add(log);
				}
				
			}
			
			if(files.size() != checkFile.getFileSize()){
				checkFile.setErrCode("F8008");
				throw new Exception("记录数不一致!");
			}
			
			if(!"0".equals(errCode)){
				checkFile.setErrCode(errCode);
				checkFile.setErrDesc(errDesc);
			}
			
			if(!isEnd){
				checkFile.setErrCode("F8008");
				checkFile.setErrDesc("文件尾没有END!");
			}
		} catch(Exception ex) {
			checkFile.setErrCode("F9999");
			checkFile.setErrDesc(ex.getMessage());
			ex.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void parserInvFile(JtBillLog invoice) {
		BufferedReader reader = null;
		int idx = 0;
		String[] lineArr = null;
		boolean isEnd = false;
		try{
			reader = new BufferedReader(new FileReader(filePath+invoice.getFileName()));
			String line = null;
			while((line = reader.readLine()) != null) {
				idx++;
				lineArr = line.split(delimiter);
				if (1 == idx) {
					if(!"STA".equals(lineArr[0])){
						invoice.setErrCode("F1001");
						throw new Exception("头记录格式不正确!");
					}
					invoice.setFileSize(Integer.parseInt(lineArr[1]));
				} else if ("END".equals(line)) {
					isEnd = true;
					break;
				} else {
					
					
				}
				
			}
			
			if(!isEnd){
				invoice.setErrCode("F1002");
				throw new Exception("尾记录格式不正确!");
			}
			
			if(idx  - 2 != invoice.getFileSize()){
				invoice.setErrCode("F1003");
				throw new Exception("头记录中的总记录数与文件中的帐单总数不等!");
			}
		} catch(Exception ex) {
			invoice.setErrCode("F9999");
			invoice.setErrDesc(ex.getMessage());
			ex.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getBillFileRegExp() {
		return billFileRegExp;
	}

	public void setBillFileRegExp(String billFileRegExp) {
		this.billFileRegExp = billFileRegExp;
	}
	
	public static void main(String args[]) {
//		String str = "a.b.c";
//		String[] strArr = str.split("\\.");
//		for(String s:strArr){
//			System.out.println(s);
//		}
//		System.out.println(BaseUtil.formatDate("205555",BaseUtil.MONTH_PATTERN1));
//		System.out.println("C1RM2BILL.ZD.201505.123.123".lastIndexOf("CRM2BILL.ZD"));
//		
//		System.out.println("CRM2BILL.ZD.201505.123.123".matches("CRM2BILL.ZD.\\d{6}.\\d{3}.\\d{3}"));
		
//		List<String> list = new ArrayList<String>();
//		list.add("abc");
//		list.add("123");
//		if(list.contains("1231")){
//			System.out.println("ok!");
//		}
		System.out.println("201511".matches("\\d{4}[0-1]{1}\\d{1}"));
	}
}
