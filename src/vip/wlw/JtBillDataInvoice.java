/**
 * 
 */
package vip.wlw;

import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 帐单汇总表
 * @author Administrator
 *
 */
public class JtBillDataInvoice {

	private long accountNo;
	
	private String statementDate;
	
	private String billRefNo;
	
	private String acctId;
	
	//0:新建 1:账单生成 2：未销账，3：已销帐，4：返销账
	private int status;
	
	//0：新建，1：处理中，2：已上传
	private int loadFlag;
	private String createDate;
	private String chgDate;
	private String postDate;
	private String annotation;
	
	private String originalData;
	
	private long payInterimSeq;
	
	private long serviceType;
	
	
	public long getServiceType() {
		return serviceType;
	}
	public void setServiceType(long serviceType) {
		this.serviceType = serviceType;
	}
	public long getPayInterimSeq() {
		return payInterimSeq;
	}
	public void setPayInterimSeq(long payInterimSeq) {
		this.payInterimSeq = payInterimSeq;
	}
	public String getOriginalData() {
		return originalData;
	}
	public void setOriginalData(String originalData) {
		this.originalData = originalData;
	}
	public long getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(long accountNo) {
		this.accountNo = accountNo;
	}
	public String getStatementDate() {
		return statementDate;
	}
	public void setStatementDate(String statementDate) {
		this.statementDate = statementDate;
	}
	public String getBillRefNo() {
		return billRefNo;
	}
	public void setBillRefNo(String billRefNo) {
		this.billRefNo = billRefNo;
	}
	public String getAcctId() {
		return acctId;
	}
	public void setAcctId(String acctId) {
		this.acctId = acctId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getLoadFlag() {
		return loadFlag;
	}
	public void setLoadFlag(int loadFlag) {
		this.loadFlag = loadFlag;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getChgDate() {
		return chgDate;
	}
	public void setChgDate(String chgDate) {
		this.chgDate = chgDate;
	}
	public String getPostDate() {
		return postDate;
	}
	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}
	public String getAnnotation() {
		return annotation;
	}
	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
	
	public void insert(Connection iamConn) throws Exception {
    	int index = 0;
        PreparedStatement stmt = null;
        StringBuffer sql = new StringBuffer();
        sql.append(" insert into JT_BILL_DATA_INVOICE( ");
        sql.append("PAY_INTERIM_SEQ,ACCOUNT_NO,STATEMENT_DATE,ACCT_ID,SERVICE_TYPE,STATUS,LOAD_FLAG,CREATE_DATE");
        sql.append(" ) values ( ?,?,?,?,?,0,0,sysdate) ");
        try {
            stmt = iamConn.prepareStatement(sql.toString());
            stmt.setLong(++index, this.getPayInterimSeq());
            stmt.setLong(++index, this.getAccountNo());
            stmt.setString(++index, this.getStatementDate());
            stmt.setString(++index, this.getAcctId());
            stmt.setLong(++index, this.getServiceType());
            stmt.executeUpdate();
        } finally {
        	WlwJdbcUtil.close(stmt);
        }
    }
	
	public static List<JtBillDataInvoice> query(Connection iamConn, String serviceType, String status, String loadFlag) throws Exception {
    	StringBuffer sql = new StringBuffer();
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        int index = 0;
        List<JtBillDataInvoice> list = new ArrayList<JtBillDataInvoice>();
    	sql.append("select PAY_INTERIM_SEQ,ACCOUNT_NO,STATEMENT_DATE,ACCT_ID,STATUS,LOAD_FLAG,SERVICE_TYPE,BILL_REF_NO,to_char(post_date,'yyyyMMddhh24miss') POST_DATE ");
    	sql.append(" from JT_BILL_DATA_INVOICE ");
    	
    	sql.append(" where 1 = 1");
    
    
    	if(StringUtils.isNotEmpty(status)){
    		sql.append(" and STATUS = ? ");
    	}
    	
    	if(StringUtils.isNotEmpty(loadFlag)){
    		sql.append(" and LOAD_FLAG = ? ");
    		sql.append("  and rownum<=1000  ");
    	}
    	if(StringUtils.isNotEmpty(serviceType)){
    		sql.append(" and SERVICE_TYPE = ? ");
    	}
    	
    	try {
    		stmt = iamConn.prepareStatement(sql.toString());
    		if(StringUtils.isNotEmpty(status)){
    			stmt.setInt(++index, Integer.parseInt(status));
        	}
    		
    		if(StringUtils.isNotEmpty(loadFlag)){
    			stmt.setInt(++index, Integer.parseInt(loadFlag));
        	}
    		
    		if(StringUtils.isNotEmpty(serviceType)){
    			stmt.setInt(++index, Integer.parseInt(serviceType));
        	}
            rs = stmt.executeQuery();
            list = fromResultSet(rs);
    	} finally {
    		WlwJdbcUtil.close(rs);
    		WlwJdbcUtil.close(stmt);
        }
    	
    	return list;
    }
	
	public static List<JtBillDataInvoice> queryLoad(Connection iamConn, String serviceType, String status, String loadFlag) throws Exception {
    	StringBuffer sql = new StringBuffer();
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        int index = 0;
        List<JtBillDataInvoice> list = new ArrayList<JtBillDataInvoice>();
    	sql.append("select PAY_INTERIM_SEQ,ACCOUNT_NO,STATEMENT_DATE,ACCT_ID,STATUS,LOAD_FLAG,SERVICE_TYPE,BILL_REF_NO,to_char(post_date,'yyyyMMddhh24miss') POST_DATE ");
    	sql.append(" from JT_BILL_DATA_INVOICE ");
    	
    	sql.append(" where 1 = 1");
    
        sql.append(" and LOAD_FLAG in (0,1) ");
     
    	if(StringUtils.isNotEmpty(status)){
    		sql.append(" and STATUS = ? ");
    	}
    	
    
    	if(StringUtils.isNotEmpty(serviceType)){
    		sql.append(" and SERVICE_TYPE = ? ");
    	}
    	
    	try {
    		stmt = iamConn.prepareStatement(sql.toString());
    		if(StringUtils.isNotEmpty(status)){
    			stmt.setInt(++index, Integer.parseInt(status));
        	}
    		
    		if(StringUtils.isNotEmpty(serviceType)){
    			stmt.setInt(++index, Integer.parseInt(serviceType));
        	}
            rs = stmt.executeQuery();
            list = fromResultSet(rs);
    	} finally {
    		WlwJdbcUtil.close(rs);
    		WlwJdbcUtil.close(stmt);
        }
    	
    	return list;
    }
	
 
	
	private static List<JtBillDataInvoice> fromResultSet(ResultSet rs) throws SQLException {
        List<JtBillDataInvoice> list = new ArrayList<JtBillDataInvoice>();
        JtBillDataInvoice entity = null;
        while (rs.next()) {
        	entity = new JtBillDataInvoice();
        	entity.setPayInterimSeq(rs.getLong("PAY_INTERIM_SEQ"));
        	entity.setAccountNo(rs.getLong("ACCOUNT_NO"));
        	entity.setAcctId(rs.getString("ACCT_ID"));
        	entity.setStatus(rs.getInt("STATUS"));
        	entity.setLoadFlag(rs.getInt("LOAD_FLAG"));
        	entity.setServiceType(rs.getLong("SERVICE_TYPE"));
        	entity.setBillRefNo(rs.getString("BILL_REF_NO"));
        	entity.setPostDate(rs.getString("POST_DATE"));
        	entity.setStatementDate(rs.getString("STATEMENT_DATE"));
            list.add(entity);
        }
        return list;
    }
	
	public void update(Connection iamConn) throws Exception {
		int index = 0;
       PreparedStatement stmt = null;
       StringBuffer sql = new StringBuffer();
       sql.append("update JT_BILL_DATA_INVOICE set STATUS = ?,LOAD_FLAG = ?");
       if(StringUtils.isNotEmpty(this.getBillRefNo())){
    	   sql.append(",BILL_REF_NO = ?");
       }
       if(StringUtils.isNotEmpty(this.getPostDate())){
    	   sql.append(",POST_DATE = to_date(?,'yyyy-MM-dd hh24:mi:ss')");
       }  
       sql.append(",CHG_DATE = sysdate");
       sql.append(" where PAY_INTERIM_SEQ = ?");
       try {
           stmt = iamConn.prepareStatement(sql.toString());
           stmt.setInt(++index, this.getStatus());
           stmt.setInt(++index, this.getLoadFlag());
           if(StringUtils.isNotEmpty(this.getBillRefNo())){
        	   stmt.setString(++index, this.getBillRefNo());
           }
           if(StringUtils.isNotEmpty(this.getPostDate())){
        	   stmt.setString(++index, this.getPostDate());
           }
           stmt.setLong(++index, this.getPayInterimSeq());
           stmt.executeUpdate();
       } finally {
       	WlwJdbcUtil.close(stmt);
       }
	}
}
