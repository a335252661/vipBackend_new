package vip.IDCLoad;

import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * 帐单明细表
 * @author Administrator
 *
 */
public class JtBillData {

	//记录ID 每个文件，从1开始递增；是“字节数长度”为4位
	private long fileLineId;
	
	//帐户ID，填写ACCT_ID
	private String acctId;
	
	//客户ID，填写cust_ID
	private String custId;
	
	//设备号码，填写SERV_ID
	private String servId;
	
	//应收金额（含税金额），单位分，不含小数
	private long amount;
	
	//调帐前的帐期YYYYMM
	private String preAdjMonth;
	
	//收费年月,YYYYMM
	private String billingMonth;
	
	//收费省代码，不足三位左补零
	private String city;
	
	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	//区号，如果为4位去掉前面的0
	private String aeraCode;
	
	//帐目类型
	private String subTypeCode;
	
	//税金单位分，不含小数;物联网填0
	private long taxAmount;
	
	//应收金额（不含税），单位分，不含小数；物联网填0
	private long afterTaxAmount;
	
	//税率
	private long tax;
	
	//具体税率对应帐目；物联网填0
	private String vatTypeCode;
	
	//税目
	private long taxCode;
	
	
	private String createDate;
	private String chgDate;
	
	//状态 0：新建，1：账单生成成功/调整账单成功，-1:失败
	private long status;
	
	//1：物联网，2：云提
	private long serviceType;
	
	//1：帐单文件，2：调整文件
	private long fileType;
	
	//文件名
	private String fileName;
	
	//备注
	private String annotation;

	private String originalData;
	
	private String errCode;
	
	private long seq;
	
	private long payAcctId;
	
	private long prdInstId;
	
	private Long payInterimSeq;

	//合帐号码类型
	private String servType;
	
	//合帐号码编码
	private String servCode;
	
	private long acctItemTypeId;
	
	//银行托收标识
	private String bankIdentifier;
	
	//产品ID，填写productId
	private String productId;

	//群组号，填写groupNo
	private String groupNo;
	
	//权责发生年月，填写accrualDate
	private String accrualDate;
	
	//合同号，填写contractNo
	private String contractNo;
	
	//项目编码，填写projectCode
	private String projectCode;
	
	//备用字段1，填写spareField1
	private String spareField1;
	
	//备用字段2，填写spareField2
	private String spareField2;
	
	//备用字段3，填写spareField3
	private String spareField3;
	
	//费用标识
	private String expensesId;
	
	
	public String getExpensesId() {
		return expensesId;
	}

	public void setExpensesId(String expensesId) {
		this.expensesId = expensesId;
	}

	public String getAccrualDate() {
		return accrualDate;
	}

	public void setAccrualDate(String accrualDate) {
		this.accrualDate = accrualDate;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getSpareField1() {
		return spareField1;
	}

	public void setSpareField1(String spareField1) {
		this.spareField1 = spareField1;
	}

	public String getSpareField2() {
		return spareField2;
	}

	public void setSpareField2(String spareField2) {
		this.spareField2 = spareField2;
	}

	public String getSpareField3() {
		return spareField3;
	}

	public void setSpareField3(String spareField3) {
		this.spareField3 = spareField3;
	}
	
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	public long getAcctItemTypeId() {
		return acctItemTypeId;
	}

	public void setAcctItemTypeId(long acctItemTypeId) {
		this.acctItemTypeId = acctItemTypeId;
	}

	public String getServType() {
		return servType;
	}

	public void setServType(String servType) {
		this.servType = servType;
	}

	public String getServCode() {
		return servCode;
	}

	public void setServCode(String servCode) {
		this.servCode = servCode;
	}

	public Long getPayInterimSeq() {
		return payInterimSeq;
	}
	
	public void setPayInterimSeq(Long payInterimSeq) {
		this.payInterimSeq = payInterimSeq;
	}
	
	public long getPrdInstId() {
		return prdInstId;
	}

	public void setPrdInstId(long prdInstId) {
		this.prdInstId = prdInstId;
	}

	public long getPayAcctId() {
		return payAcctId;
	}

	public void setPayAcctId(long payAcctId) {
		this.payAcctId = payAcctId;
	}

	public long getSeq() {
		return seq;
	}

	public void setSeq(long seq) {
		this.seq = seq;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getOriginalData() {
		return originalData;
	}

	public void setOriginalData(String originalData) {
		this.originalData = originalData;
	}

	public long getFileLineId() {
		return fileLineId;
	}

	public void setFileLineId(long fileLineId) {
		this.fileLineId = fileLineId;
	}

	public String getAcctId() {
		return acctId;
	}

	public void setAcctId(String acctId) {
		this.acctId = acctId;
	}

	public String getServId() {
		return servId;
	}

	public void setServId(String servId) {
		this.servId = servId;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public String getPreAdjMonth() {
		return preAdjMonth;
	}

	public void setPreAdjMonth(String preAdjMonth) {
		this.preAdjMonth = preAdjMonth;
	}

	public String getBillingMonth() {
		return billingMonth;
	}

	public void setBillingMonth(String billingMonth) {
		this.billingMonth = billingMonth;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAeraCode() {
		return aeraCode;
	}

	public void setAeraCode(String aeraCode) {
		this.aeraCode = aeraCode;
	}

	public String getSubTypeCode() {
		return subTypeCode;
	}

	public void setSubTypeCode(String subTypeCode) {
		this.subTypeCode = subTypeCode;
	}

	public long getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(long taxAmount) {
		this.taxAmount = taxAmount;
	}

	public long getAfterTaxAmount() {
		return afterTaxAmount;
	}

	public void setAfterTaxAmount(long afterTaxAmount) {
		this.afterTaxAmount = afterTaxAmount;
	}

	public long getTax() {
		return tax;
	}

	public void setTax(long tax) {
		this.tax = tax;
	}

	public String getVatTypeCode() {
		return vatTypeCode;
	}

	public void setVatTypeCode(String vatTypeCode) {
		this.vatTypeCode = vatTypeCode;
	}

	public long getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(long taxCode) {
		this.taxCode = taxCode;
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

	public long getStatus() {
		return status;
	}

	public void setStatus(long status) {
		this.status = status;
	}

	public long getServiceType() {
		return serviceType;
	}

	public void setServiceType(long serviceType) {
		this.serviceType = serviceType;
	}

	public long getFileType() {
		return fileType;
	}

	public void setFileType(long fileType) {
		this.fileType = fileType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
	
	public String getBankIdentifier() {
		return bankIdentifier;
	}

	public void setBankIdentifier(String bankIdentifier) {
		this.bankIdentifier = bankIdentifier;
	}

	public static long getSequence(Connection iamConn) throws Exception {
    	return WlwJdbcUtil.getSeqNextVal(iamConn,"SEQ_JT_BILL_NO");
    }
	
	public void insert(Connection iamConn) throws Exception {
    	int index = 0;
        PreparedStatement stmt = null;
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
        sql.append("TAX_CODE,");
        sql.append("STATUS,");
        sql.append("SERVICE_TYPE,");
        sql.append("FILE_TYPE,");
        sql.append("FILE_NAME,");
        sql.append("ANNOTATION,");
        sql.append("SEQ,");
        sql.append("PAY_INTERIM_SEQ,");
        sql.append("PRE_ADJ_MONTH,");
        sql.append("SERV_CODE,");
        sql.append("SERV_TYPE,");
        sql.append("CREATE_DATE");
        
        sql.append(" ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate) ");
        try {
        	this.seq = getSequence(iamConn);
        	
            stmt = iamConn.prepareStatement(sql.toString());
            stmt.setLong(++index, this.getFileLineId());
            stmt.setString(++index, this.getAcctId());
            stmt.setString(++index, this.getServId());
            stmt.setLong(++index, this.getAmount());
            stmt.setString(++index, this.getBillingMonth());
            stmt.setString(++index, this.getCity());
            stmt.setString(++index, this.getAeraCode());
            stmt.setString(++index, this.getSubTypeCode());
            stmt.setLong(++index, this.getTaxAmount());
            stmt.setLong(++index, this.getAfterTaxAmount());
            stmt.setLong(++index, this.getTax());
            stmt.setString(++index, this.getVatTypeCode());
            stmt.setLong(++index, this.getTaxCode());
            stmt.setLong(++index, this.getStatus());
            stmt.setLong(++index, this.getServiceType());
            stmt.setLong(++index, this.getFileType());
            stmt.setString(++index, this.getFileName());
            stmt.setString(++index, this.getAnnotation());
            stmt.setLong(++index, this.getSeq());
            if(null == this.getPayInterimSeq()){
            	 stmt.setNull(++index,java.sql.Types.INTEGER);
            } else {
            	stmt.setLong(++index,this.getPayInterimSeq());
            }
            stmt.setString(++index, this.getPreAdjMonth());
            stmt.setString(++index, this.getServCode());
            stmt.setString(++index, this.getServType());
            stmt.executeUpdate();
        } finally {
        	WlwJdbcUtil.close(stmt);
        }
    }

	public void insertWxtx(Connection iamConn) throws Exception {
    	int index = 0;
        PreparedStatement stmt = null;
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
        sql.append("TAX_CODE,");
        sql.append("STATUS,");
        sql.append("SERVICE_TYPE,");
        sql.append("FILE_TYPE,");
        sql.append("FILE_NAME,");
        sql.append("ANNOTATION,");
        sql.append("SEQ,");
        sql.append("PAY_INTERIM_SEQ,");
        sql.append("PRE_ADJ_MONTH,");
        sql.append("SERV_CODE,");
        sql.append("SERV_TYPE,");
        sql.append("BANK_IDENTIFIER,");
        sql.append("CREATE_DATE");
        sql.append(" ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate) ");
        try {
        	this.seq = getSequence(iamConn);
        	
            stmt = iamConn.prepareStatement(sql.toString());
            stmt.setLong(++index, this.getFileLineId());
            stmt.setString(++index, this.getAcctId());
            stmt.setString(++index, this.getServId());
            stmt.setLong(++index, this.getAmount());
            stmt.setString(++index, this.getBillingMonth());
            stmt.setString(++index, this.getCity());
            stmt.setString(++index, this.getAeraCode());
            stmt.setString(++index, this.getSubTypeCode());
            stmt.setLong(++index, this.getTaxAmount());
            stmt.setLong(++index, this.getAfterTaxAmount());
            stmt.setLong(++index, this.getTax());
            stmt.setString(++index, this.getVatTypeCode());
            stmt.setLong(++index, this.getTaxCode());
            stmt.setLong(++index, this.getStatus());
            stmt.setLong(++index, this.getServiceType());
            stmt.setLong(++index, this.getFileType());
            stmt.setString(++index, this.getFileName());
            stmt.setString(++index, this.getAnnotation());
            stmt.setLong(++index, this.getSeq());
            if(null == this.getPayInterimSeq()){
            	 stmt.setNull(++index,java.sql.Types.INTEGER);
            } else {
            	stmt.setLong(++index,this.getPayInterimSeq());
            }
            stmt.setString(++index, this.getPreAdjMonth());
            stmt.setString(++index, this.getServCode());
            stmt.setString(++index, this.getServType());
            stmt.setString(++index, this.getBankIdentifier());
            stmt.executeUpdate();
        } finally {
        	WlwJdbcUtil.close(stmt);
        }
    }
	
	
//	public void insertDetail(Connection iamConn) throws Exception {
//    	int index = 0;
//        PreparedStatement stmt = null;
//        StringBuffer sql = new StringBuffer();
//        sql.append(" insert into BILL_T_DETAIL_IAM( ");
//        sql.append("ACCT_ID,");
//        sql.append("PRD_INST_ID,");
//        sql.append("ACCT_ITEM_TYPE_ID,");
//        sql.append("CHARGE,");
//        sql.append("BILLING_CYCLE_ID,");
//        sql.append("FEE_CYCLE_ID,");
//        sql.append("PAY_ACCT_ID");
//        
//        sql.append(" ) values ( ?,?,?,?,?,to_char(sysdate,'yyyyMM'),?) ");
//        try {
//        	
//            stmt = iamConn.prepareStatement(sql.toString());
//            //stmt.setString(++index, this.getAcctId());
//            stmt.setString(++index, Long.toString(this.getPayAcctId()));
//            stmt.setLong(++index, this.getPrdInstId());
//            
//            //stmt.setLong(++index, Long.parseLong(this.getSubTypeCode()));
//            stmt.setLong(++index, this.getAcctItemTypeId());
//            
//            stmt.setLong(++index, this.getAmount());
//            stmt.setString(++index, this.getBillingMonth());
//            stmt.setLong(++index, this.getPayAcctId());
//            stmt.executeUpdate();
//        } finally {
//        	WlwJdbcUtil.close(stmt);
//        }
//    }
	
	public static List<JtBillData> queryPart(Connection iamConn, Long payInterimSeq, String status, String serviceType) throws Exception {
    	StringBuffer sql = new StringBuffer();
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        int index = 0;
        List<JtBillData> list = new ArrayList<JtBillData>();
    	sql.append("select * from JT_BILL_DATA");
    	
    	sql.append(" where 1 = 1 ");
    	
    	if(StringUtils.isNotEmpty(serviceType)){
    		sql.append(" and SERVICE_TYPE = ? ");
    	}
    	if(null != payInterimSeq){
    		sql.append(" and PAY_INTERIM_SEQ = ? ");
    	}
    	
    	if(StringUtils.isNotEmpty(status)){
    		sql.append(" and STATUS = ? ");
    		if("0".equals(status)){
    			sql.append(" and PAY_INTERIM_SEQ is null ");
    		}
    	}
    	sql.append(" and EXPENSES_ID  is not null ");
    	
    	try {
    		stmt = iamConn.prepareStatement(sql.toString());
    		if(StringUtils.isNotEmpty(serviceType)){
    			stmt.setLong(++index, Long.parseLong(serviceType));
        	}
    		
    		if(null != payInterimSeq){
    			stmt.setLong(++index, payInterimSeq);
        	}
    		
    		if(StringUtils.isNotEmpty(status)){
    			stmt.setLong(++index, Long.parseLong(status));
        	}
            rs = stmt.executeQuery();
            list = fromResultSet(rs);
    	} finally {
    		WlwJdbcUtil.close(rs);
    		WlwJdbcUtil.close(stmt);
        }
    	
    	return list;
    }
	
	public static List<JtBillData> query(Connection iamConn, Long payInterimSeq, String status, String serviceType) throws Exception {
    	StringBuffer sql = new StringBuffer();
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        int index = 0;
        List<JtBillData> list = new ArrayList<JtBillData>();
    	sql.append("select * from JT_BILL_DATA");
    	
    	sql.append(" where 1 = 1 ");
    	
    	if(StringUtils.isNotEmpty(serviceType)){
    		sql.append(" and SERVICE_TYPE = ? ");
    	}
    	if(null != payInterimSeq){
    		sql.append(" and PAY_INTERIM_SEQ = ? ");
    	}
    	
    	if(StringUtils.isNotEmpty(status)){
    		sql.append(" and STATUS = ? ");
    		if("0".equals(status)){
    			sql.append(" and PAY_INTERIM_SEQ is null ");
    		}
    	}

    	
    	try {
    		stmt = iamConn.prepareStatement(sql.toString());
    		if(StringUtils.isNotEmpty(serviceType)){
    			stmt.setLong(++index, Long.parseLong(serviceType));
        	}
    		
    		if(null != payInterimSeq){
    			stmt.setLong(++index, payInterimSeq);
        	}
    		
    		if(StringUtils.isNotEmpty(status)){
    			stmt.setLong(++index, Long.parseLong(status));
        	}
            rs = stmt.executeQuery();
            list = fromResultSet(rs);
    	} finally {
    		WlwJdbcUtil.close(rs);
    		WlwJdbcUtil.close(stmt);
        }
    	
    	return list;
    }
	
	public static List<JtBillData> queryWhole(Connection iamConn, Long payInterimSeq, String status, String serviceType) throws Exception {
    	StringBuffer sql = new StringBuffer();
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        int index = 0;
        List<JtBillData> list = new ArrayList<JtBillData>();
    	sql.append("select * from JT_BILL_DATA");
    	
    	sql.append(" where 1 = 1 ");
    	
    	if(StringUtils.isNotEmpty(serviceType)){
    		sql.append(" and SERVICE_TYPE = ? ");
    	}
    	if(null != payInterimSeq){
    		sql.append(" and PAY_INTERIM_SEQ = ? ");
    	}
    	
    	if(StringUtils.isNotEmpty(status)){
    		sql.append(" and STATUS = ? ");
    		if("0".equals(status)){
    			sql.append(" and PAY_INTERIM_SEQ is null ");
    		}
    	}
    	sql.append(" and EXPENSES_ID  is null ");
    	
    	try {
    		stmt = iamConn.prepareStatement(sql.toString());
    		if(StringUtils.isNotEmpty(serviceType)){
    			stmt.setLong(++index, Long.parseLong(serviceType));
        	}
    		
    		if(null != payInterimSeq){
    			stmt.setLong(++index, payInterimSeq);
        	}
    		
    		if(StringUtils.isNotEmpty(status)){
    			stmt.setLong(++index, Long.parseLong(status));
        	}
            rs = stmt.executeQuery();
            list = fromResultSet(rs);
    	} finally {
    		WlwJdbcUtil.close(rs);
    		WlwJdbcUtil.close(stmt);
        }
    	
    	return list;
    }
	
	
	public void updateYD(Connection iamConn) throws Exception {
		int index = 0;
       PreparedStatement stmt = null;
       StringBuffer sql = new StringBuffer();
       sql.append("update JT_BILL_DATA set STATUS = ?");
       if(null != this.getPayInterimSeq()) {
    	   sql.append(",PAY_INTERIM_SEQ = ? ");
       }
       if(null != this.getAnnotation()) {
    	   sql.append(",ANNOTATION = ? ");
       }
       sql.append(" where SEQ = ?");
       try {
           stmt = iamConn.prepareStatement(sql.toString());
           stmt.setLong(++index, this.getStatus());
           if(null != this.getPayInterimSeq()) {
        	  stmt.setLong(++index, this.getPayInterimSeq());
           }
           if(null != this.getAnnotation()) {
        	   stmt.setString(++index, this.getAnnotation());
           }
           stmt.setLong(++index, this.getSeq());
           stmt.executeUpdate();
       } finally {
       	WlwJdbcUtil.close(stmt);
       }
	}
	
	public void update(Connection iamConn) throws Exception {
		int index = 0;
       PreparedStatement stmt = null;
       StringBuffer sql = new StringBuffer();
       sql.append("update JT_BILL_DATA set STATUS = ?");
       if(null != this.getPayInterimSeq()) {
    	   sql.append(",PAY_INTERIM_SEQ = ? ");
       }
       if(null != this.getAnnotation()) {
    	   sql.append(",ANNOTATION = ? ");
       }
       sql.append(" where SEQ = ?");
       try {
           stmt = iamConn.prepareStatement(sql.toString());
           stmt.setLong(++index, this.getStatus());
           if(null != this.getPayInterimSeq()) {
        	  stmt.setLong(++index, this.getPayInterimSeq());
           }
           if(null != this.getAnnotation()) {
        	   stmt.setString(++index, this.getAnnotation());
           }
           stmt.setLong(++index, this.getSeq());
           stmt.executeUpdate();
       } finally {
       	WlwJdbcUtil.close(stmt);
       }
	}
	
	
	public void batchUpdate(PreparedStatement stmt) throws Exception {
		int index = 0;

		stmt.setLong(++index, this.getStatus());

		 if(null == this.getPayInterimSeq()){
        	 stmt.setNull(++index,java.sql.Types.INTEGER);
        } else {
        	stmt.setLong(++index,this.getPayInterimSeq());
        }
		
		stmt.setString(++index, this.getAnnotation());

		stmt.setLong(++index, this.getSeq());
		
		stmt.addBatch();

	}
	
	private static List<JtBillData> fromResultSet(ResultSet rs) throws SQLException {
        List<JtBillData> list = new ArrayList<JtBillData>();
        JtBillData entity = null;
        while (rs.next()) {
        	entity = new JtBillData();
        	entity.setFileLineId(rs.getLong("FILE_LINE_ID"));
        	entity.setAcctId(rs.getString("ACCT_ID"));
        	entity.setServId(rs.getString("SERV_ID"));
        	entity.setAmount(rs.getLong("AMOUNT"));
        	entity.setBillingMonth(rs.getString("BILLING_MONTH"));
        	entity.setCity(rs.getString("CITY"));
        	entity.setSubTypeCode(rs.getString("SUB_TYPE_CODE"));
        	entity.setSeq(rs.getLong("SEQ"));
        	//entity.setPreAdjMonth(rs.getString(""));
        	entity.setFileType(rs.getLong("FILE_TYPE"));
        	entity.setServCode(rs.getString("SERV_CODE"));
        	entity.setProductId(rs.getString("PRODUCT_ID"));
        	entity.setGroupNo(rs.getString("GROUP_NO"));
        	entity.setServiceType(rs.getLong("SERVICE_TYPE"));
        	entity.setExpensesId(rs.getString("EXPENSES_ID"));
            list.add(entity);
        }
        return list;
    }
	
	
	
	public static List<JtBillData> query(Connection iamConn, Long payInterimSeq, String status, String serviceType, String serv_code) throws Exception {
    	StringBuffer sql = new StringBuffer();
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        int index = 0;
        List<JtBillData> list = new ArrayList<JtBillData>();
    	sql.append("select * from JT_BILL_DATA");
    	
    	sql.append(" where 1 = 1 ");
    	
    	if(StringUtils.isNotEmpty(serviceType)){
    		sql.append(" and SERVICE_TYPE = ? ");
    	}
    	
    	
    	if(null != payInterimSeq){
    		sql.append(" and PAY_INTERIM_SEQ = ? ");
    	}
    	
    	if(StringUtils.isNotEmpty(status)){
    		sql.append(" and STATUS = ? ");
    		if("0".equals(status)){
    			sql.append(" and PAY_INTERIM_SEQ is null ");
    		}
    	}
    	
    	if(null != serv_code){
    		sql.append(" and serv_code = ? ");
    	}
    	
    	try {
    		stmt = iamConn.prepareStatement(sql.toString());
    		if(StringUtils.isNotEmpty(serviceType)){
    			stmt.setLong(++index, Long.parseLong(serviceType));
        	}
    		
    		if(null != payInterimSeq){
    			stmt.setLong(++index, payInterimSeq);
        	}
    		
    		if(StringUtils.isNotEmpty(status)){
    			stmt.setLong(++index, Long.parseLong(status));
        	}
    		
    		if(StringUtils.isNotEmpty(serv_code)){
    			stmt.setString(++index, serv_code);
        	}
    		
            rs = stmt.executeQuery();
            list = fromResultSet(rs);
    	} finally {
    		WlwJdbcUtil.close(rs);
    		WlwJdbcUtil.close(stmt);
        }
    	
    	return list;
    }
	
	
	
	public void insertDetailNew(PreparedStatement stmt) throws Exception {
    	int index = 0;
        try {
            stmt.setString(++index, this.getCustId());
            stmt.setString(++index, Long.toString(this.getPayAcctId()));
            stmt.setLong(++index, this.getPrdInstId());
            
            //stmt.setLong(++index, Long.parseLong(this.getSubTypeCode()));
            stmt.setLong(++index, this.getAcctItemTypeId());
            
            stmt.setLong(++index, this.getAmount());
            stmt.setString(++index, this.getBillingMonth());
            stmt.setLong(++index, this.getPayAcctId());
            stmt.addBatch();
        } finally {

        }
    }
	
	public void insertJTJYDetailNew(PreparedStatement stmt) throws Exception {
    	int index = 0;
        try {
        	 //stmt.setString(++index, this.getAcctId());
            stmt.setString(++index, Long.toString(this.getPayAcctId()));
            stmt.setLong(++index, this.getPrdInstId());
            
            //stmt.setLong(++index, Long.parseLong(this.getSubTypeCode()));
            stmt.setLong(++index, this.getAcctItemTypeId());
            
            stmt.setLong(++index, this.getAmount());
            stmt.setString(++index, this.getBillingMonth());
            stmt.setLong(++index, this.getPayAcctId());
            stmt.addBatch();
        } finally {

        }
    }
	
	
	
	public void newDataInsert(PreparedStatement stmt) throws Exception {
    	int index = 0;
       
        try {
            stmt.setLong(++index, this.getFileLineId());
            stmt.setString(++index, this.getAcctId());
            stmt.setString(++index, this.getServId());
            stmt.setLong(++index, this.getAmount());
            stmt.setString(++index, this.getBillingMonth());
            stmt.setString(++index, this.getCity());
            stmt.setString(++index, this.getAeraCode());
            stmt.setString(++index, this.getSubTypeCode());
            stmt.setLong(++index, this.getTaxAmount());
            stmt.setLong(++index, this.getAfterTaxAmount());
            stmt.setLong(++index, this.getTax());
            stmt.setString(++index, this.getVatTypeCode());
            stmt.setLong(++index, this.getTaxCode());
            stmt.setLong(++index, this.getStatus());
            stmt.setLong(++index, this.getServiceType());
            stmt.setLong(++index, this.getFileType());
            stmt.setString(++index, this.getFileName());
            stmt.setString(++index, this.getAnnotation());
            stmt.setLong(++index, this.getSeq());
            if(null == this.getPayInterimSeq()){
            	 stmt.setNull(++index,java.sql.Types.INTEGER);
            } else {
            	stmt.setLong(++index,this.getPayInterimSeq());
            }
            stmt.setString(++index, this.getPreAdjMonth());
            stmt.setString(++index, this.getServCode());
            stmt.setString(++index, this.getServType());
            stmt.setString(++index, this.getExpensesId());
            stmt.addBatch();
        } finally {
        }
    }
	
	public void idcDataInsert(PreparedStatement stmt) throws Exception {
    	int index = 0;
       
        try {
            stmt.setLong(++index, this.getFileLineId());
            stmt.setString(++index, this.getAcctId());
            stmt.setString(++index, this.getServId());
            stmt.setLong(++index, this.getAmount());
            stmt.setString(++index, this.getBillingMonth());
            stmt.setString(++index, this.getCity());
            stmt.setString(++index, this.getAeraCode());
            stmt.setString(++index, this.getSubTypeCode());
            stmt.setLong(++index, this.getTaxAmount());
            stmt.setLong(++index, this.getAfterTaxAmount());
            stmt.setLong(++index, this.getTax());
            stmt.setString(++index, this.getVatTypeCode());
//            stmt.setLong(++index, this.getTaxCode());
            stmt.setLong(++index, this.getStatus());
            stmt.setLong(++index, this.getServiceType());
            stmt.setLong(++index, this.getFileType());
            stmt.setString(++index, this.getFileName());
            stmt.setString(++index, this.getAnnotation());
            stmt.setLong(++index, this.getSeq());
            if(null == this.getPayInterimSeq()){
            	 stmt.setNull(++index,java.sql.Types.INTEGER);
            } else {
            	stmt.setLong(++index,this.getPayInterimSeq());
            }
            stmt.setString(++index, this.getPreAdjMonth());
            stmt.setString(++index, this.getServType());
            stmt.setLong(++index, this.getTaxCode());
            stmt.setString(++index, this.getProductId());
            stmt.setString(++index, this.getGroupNo());
            stmt.setString(++index, this.getAccrualDate());
            stmt.setString(++index, this.getContractNo());
            stmt.setString(++index, this.getProjectCode());
            stmt.setString(++index, this.getSpareField1());
            stmt.setString(++index, this.getSpareField2());
            stmt.setString(++index, this.getSpareField3());
            stmt.addBatch();
        } finally {
        }
    }
	
	public void idcPreDataInsert(PreparedStatement stmt) throws Exception {
    	int index = 0;
       Date date=null;
       SimpleDateFormat format=new SimpleDateFormat("yyyyMM");
       date=format.parse(this.getBillingMonth());
       Calendar c = Calendar.getInstance();
       c.setTime(date);  
       c.add(Calendar.MONTH, 1);  //账期加一个月
       String billingMonthPlus=format.format(c.getTime());
        try {
            stmt.setLong(++index, this.getFileLineId());
            stmt.setString(++index, this.getAcctId());
            stmt.setString(++index, this.getServId());
            stmt.setLong(++index, this.getAmount());
            stmt.setString(++index, billingMonthPlus);
            stmt.setString(++index, this.getCity());
            stmt.setString(++index, this.getAeraCode());
            stmt.setString(++index, this.getSubTypeCode());
            stmt.setLong(++index, this.getTaxAmount());
            stmt.setLong(++index, this.getAfterTaxAmount());
            stmt.setLong(++index, this.getTax());
            stmt.setString(++index, this.getVatTypeCode());
//            stmt.setLong(++index, this.getTaxCode());
            stmt.setLong(++index, this.getStatus());
            stmt.setLong(++index, this.getServiceType());
            stmt.setLong(++index, this.getFileType());
            stmt.setString(++index, this.getFileName());
            stmt.setString(++index, this.getAnnotation());
            stmt.setLong(++index, this.getSeq());
            if(null == this.getPayInterimSeq()){
            	 stmt.setNull(++index,java.sql.Types.INTEGER);
            } else {
            	stmt.setLong(++index,this.getPayInterimSeq());
            }
            stmt.setString(++index, this.getPreAdjMonth());
            stmt.setString(++index, this.getServType());
            stmt.setLong(++index, this.getTaxCode());
            stmt.setString(++index, this.getProductId());
            stmt.setString(++index, this.getGroupNo());
            stmt.setString(++index, this.getAccrualDate());
            stmt.setString(++index, this.getContractNo());
            stmt.setString(++index, this.getProjectCode());
            stmt.setString(++index, this.getSpareField1());
            stmt.setString(++index, this.getSpareField2());
            stmt.setString(++index, this.getSpareField3());
            stmt.addBatch();
        } finally {
        }
    }
	public static List<JtBillData> queryGroupNoYD(Connection iamConn, Long payInterimSeq, String status, String serviceType, String serv_code) throws Exception {
    	StringBuffer sql = new StringBuffer();
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        int index = 0;
        List<JtBillData> list = new ArrayList<JtBillData>();
    	sql.append("select * from JT_BILL_DATA");
    	
    	sql.append(" where 1 = 1 ");
    	
    	if(StringUtils.isNotEmpty(serviceType)){
    		sql.append(" and SERVICE_TYPE in (5,6,7,8,9) ");
    	}
    	
    	
    	if(null != payInterimSeq){
    		sql.append(" and PAY_INTERIM_SEQ = ? ");
    	}
    	
    	if(StringUtils.isNotEmpty(status)){
    		sql.append(" and STATUS = ? ");
    		if("0".equals(status)){
    			sql.append(" and PAY_INTERIM_SEQ is null ");
    		}
    	}
    	
    	if(null != serv_code){
    		sql.append(" and serv_id = ? ");
    	}
    	
    	try {
    		stmt = iamConn.prepareStatement(sql.toString());
//    		if(StringUtils.isNotEmpty(serviceType)){
//    			stmt.setLong(++index, Long.parseLong(serviceType));
//        	}
    		
    		if(null != payInterimSeq){
    			stmt.setLong(++index, payInterimSeq);
        	}
    		
    		if(StringUtils.isNotEmpty(status)){
    			stmt.setLong(++index, Long.parseLong(status));
        	}
    		
    		if(StringUtils.isNotEmpty(serv_code)){
    			stmt.setString(++index, serv_code);
        	}
    		
            rs = stmt.executeQuery();
            list = fromResultSet(rs);
    	} finally {
    		WlwJdbcUtil.close(rs);
    		WlwJdbcUtil.close(stmt);
        }
    	
    	return list;
    }
	
	public static List<JtBillData> queryGroupNo(Connection iamConn, Long payInterimSeq, String status, String serviceType, String serv_code) throws Exception {
    	StringBuffer sql = new StringBuffer();
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        int index = 0;
        List<JtBillData> list = new ArrayList<JtBillData>();
    	sql.append("select * from JT_BILL_DATA");
    	
    	sql.append(" where 1 = 1 ");
    	
    	if(StringUtils.isNotEmpty(serviceType)){
    		sql.append(" and SERVICE_TYPE = ? ");
    	}
    	
    	
    	if(null != payInterimSeq){
    		sql.append(" and PAY_INTERIM_SEQ = ? ");
    	}
    	
    	if(StringUtils.isNotEmpty(status)){
    		sql.append(" and STATUS = ? ");
    		if("0".equals(status)){
    			sql.append(" and PAY_INTERIM_SEQ is null ");
    		}
    	}
    	
    	if(null != serv_code){
    		sql.append(" and group_no = ? ");
    	}
    	
    	try {
    		stmt = iamConn.prepareStatement(sql.toString());
    		if(StringUtils.isNotEmpty(serviceType)){
    			stmt.setLong(++index, Long.parseLong(serviceType));
        	}
    		
    		if(null != payInterimSeq){
    			stmt.setLong(++index, payInterimSeq);
        	}
    		
    		if(StringUtils.isNotEmpty(status)){
    			stmt.setLong(++index, Long.parseLong(status));
        	}
    		
    		if(StringUtils.isNotEmpty(serv_code)){
    			stmt.setString(++index, serv_code);
        	}
    		
            rs = stmt.executeQuery();
            list = fromResultSet(rs);
    	} finally {
    		WlwJdbcUtil.close(rs);
    		WlwJdbcUtil.close(stmt);
        }
    	
    	return list;
    }
	
	public static List<JtBillData> queryPredGroupNo(Connection iamConn, Long payInterimSeq, String status, String serviceType, String serv_code) throws Exception {
    	StringBuffer sql = new StringBuffer();
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        int index = 0;
        List<JtBillData> list = new ArrayList<JtBillData>();
    	sql.append("select * from JT_BILL_DATA_YUGU");
    	
    	sql.append(" where 1 = 1 ");
    	
    	if(StringUtils.isNotEmpty(serviceType)){
    		sql.append(" and SERVICE_TYPE = ? ");
    	}
    	
    	
    	if(null != payInterimSeq){
    		sql.append(" and PAY_INTERIM_SEQ = ? ");
    	}
    	
    	if(StringUtils.isNotEmpty(status)){
    		sql.append(" and STATUS = ? ");
    		if("0".equals(status)){
    			sql.append(" and PAY_INTERIM_SEQ is null ");
    		}
    	}
    	
    	if(null != serv_code){
    		sql.append(" and group_no = ? ");
    	}
    	
    	try {
    		stmt = iamConn.prepareStatement(sql.toString());
    		if(StringUtils.isNotEmpty(serviceType)){
    			stmt.setLong(++index, Long.parseLong(serviceType));
        	}
    		
    		if(null != payInterimSeq){
    			stmt.setLong(++index, payInterimSeq);
        	}
    		
    		if(StringUtils.isNotEmpty(status)){
    			stmt.setLong(++index, Long.parseLong(status));
        	}
    		
    		if(StringUtils.isNotEmpty(serv_code)){
    			stmt.setString(++index, serv_code);
        	}
    		
            rs = stmt.executeQuery();
            list = fromResultSet(rs);
    	} finally {
    		WlwJdbcUtil.close(rs);
    		WlwJdbcUtil.close(stmt);
        }
    	
    	return list;
    }
	
	public static List<JtBillData> queryPred(Connection iamConn, Long payInterimSeq, String status, String serviceType, String serv_code) throws Exception {
    	StringBuffer sql = new StringBuffer();
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        int index = 0;
        List<JtBillData> list = new ArrayList<JtBillData>();
    	sql.append("select * from JT_BILL_DATA_YUGU");
    	
    	sql.append(" where 1 = 1 ");
    	
    	if(StringUtils.isNotEmpty(serviceType)){
    		sql.append(" and SERVICE_TYPE = ? ");
    	}
    	
    	
    	if(null != payInterimSeq){
    		sql.append(" and PAY_INTERIM_SEQ = ? ");
    	}
    	
    	if(StringUtils.isNotEmpty(status)){
    		sql.append(" and STATUS = ? ");
    		if("0".equals(status)){
    			sql.append(" and PAY_INTERIM_SEQ is null ");
    		}
    	}
    	
    	if(null != serv_code){
    		sql.append(" and serv_code = ? ");
    	}
    	
    	try {
    		stmt = iamConn.prepareStatement(sql.toString());
    		if(StringUtils.isNotEmpty(serviceType)){
    			stmt.setLong(++index, Long.parseLong(serviceType));
        	}
    		
    		if(null != payInterimSeq){
    			stmt.setLong(++index, payInterimSeq);
        	}
    		
    		if(StringUtils.isNotEmpty(status)){
    			stmt.setLong(++index, Long.parseLong(status));
        	}
    		
    		if(StringUtils.isNotEmpty(serv_code)){
    			stmt.setString(++index, serv_code);
        	}
    		
            rs = stmt.executeQuery();
            list = fromResultSet(rs);
    	} finally {
    		WlwJdbcUtil.close(rs);
    		WlwJdbcUtil.close(stmt);
        }
    	
    	return list;
    }

	public void updatePred(Connection iamConn) throws Exception {
		int index = 0;
       PreparedStatement stmt = null;
       StringBuffer sql = new StringBuffer();
       sql.append("update JT_BILL_DATA_YUGU set STATUS = ?");
       if(null != this.getPayInterimSeq()) {
    	   sql.append(",PAY_INTERIM_SEQ = ? ");
       }
       if(null != this.getAnnotation()) {
    	   sql.append(",ANNOTATION = ? ");
       }
       sql.append(" where SEQ = ?");
       try {
           stmt = iamConn.prepareStatement(sql.toString());
           stmt.setLong(++index, this.getStatus());
           if(null != this.getPayInterimSeq()) {
        	  stmt.setLong(++index, this.getPayInterimSeq());
           }
           if(null != this.getAnnotation()) {
        	   stmt.setString(++index, this.getAnnotation());
           }
           stmt.setLong(++index, this.getSeq());
           stmt.executeUpdate();
       } finally {
       	WlwJdbcUtil.close(stmt);
       }
	}
	
}
