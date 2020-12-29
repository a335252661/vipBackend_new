/**
 * 
 */
package vip.IDCLoad;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Administrator
 *
 */
public abstract class JtInvoiceFileParser {

	protected File theFile;

	protected String errCode;
	
	protected String errDesc;
	
	protected String delimiter;
	
	protected String fileHead;
	
	protected String fileEnd;
	
	protected List<JtBillData> billDatas = new ArrayList<JtBillData>();

	public String getFileHead() {
		return fileHead;
	}

	public void setFileHead(String fileHead) {
		this.fileHead = fileHead;
	}

	public String getFileEnd() {
		return fileEnd;
	}

	public void setFileEnd(String fileEnd) {
		this.fileEnd = fileEnd;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrDesc() {
		return errDesc;
	}

	public void setErrDesc(String errDesc) {
		this.errDesc = errDesc;
	}

	public List<JtBillData> getBillDatas() {
		return billDatas;
	}

	public void setBillDatas(List<JtBillData> billDatas) {
		this.billDatas = billDatas;
	}

	public JtInvoiceFileParser(File theFile, String delimiter) {
		this.theFile = theFile;
		this.delimiter = delimiter;
	}
	
	public File getTheFile() {
		return theFile;
	}

	public void setTheFile(File theFile) {
		this.theFile = theFile;
	}

	public abstract void parser();

}
