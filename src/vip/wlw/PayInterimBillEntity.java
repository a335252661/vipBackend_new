/**
 * 
 */
package vip.wlw;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

/**
 * IAM增量账单出账记录日志表(PAY_INTERIM_BILL)
 * @author zhangye
 *
 */
public class PayInterimBillEntity {

	private Long seq;
	
	//分账序号
	private String acctId;
	
	//设备号 当TRANS_TYPE =2 时 可以为null
	//private String serviceNbr;
	
	//根资产集成id 当TRANS_TYPE =2 时 可以为null
	//private String assetIntegrationId;
	
	//根产品类型 当TRANS_TYPE =2 时 可以为null
	//private Long prdId;
	
	//交易类型:1 拆机立即出账 2 不拆机立即出账
	private Long transType;
	
	//申请员工号
	private Long createStaff;
	
	//申请员工对应网点编号
	private String reqOfficeId;
	
	//申请日期
	private String createDate;
	
	/*
	 * 状态
	 * 	0 已申请 
	 *  1 KBP(ABP)出账成功 
	 *  2 KBP(ABP)出账失败 
	 *  3 IAM账单转化成功 
	 *  4 IAM账单转化失败 
	 *  5 IAM余额抵扣零头结转成功 
	 *  6 IAM余额抵扣零头结转失败
	 *  7 开账自动退费抽取成功 
	 *  8 开账自动退费抽取失败
	 *  9 开账自动退费处理成功 
	 *  10 开账自动退费处理失败state
	 */
	private Long statusCd;
	
	//前台指定账单最后付款日期 用来覆盖IAM中的bill_invoice.payment_due_date
	private String paymentDueDate;
	
	//KBP账单编号
	//private Long kbpBillRefNo;
	
	//KBP CUST DB SERVER_ID
	//private Long kbpServId;
	
	//来源类型：1 前台立即出账, 2 KBP增量补出账单, 3 开帐回退KBP重出账单, 4 ABP增量补出账单, 5 开帐回退ABP重出账单
	private Long sourceType;
	
	//修改人
	private String updateStaff;
	
	//修改日期
	private String updateDate;
	
	//备注
	private String remark;

	//转化后的帐单编号
	private String billRefNo;

	/**
	 * 重开账，立即出账 的不确认收入标志刷新状态。null 未处理，处理成功，-1 处理失败
	 */
	private Long collFlagStatus;
	
	
	/**
	 * @return the seq
	 */
	public Long getSeq() {
		return seq;
	}

	/**
	 * @param seq the seq to set
	 */
	public void setSeq(Long seq) {
		this.seq = seq;
	}

	

	/**
	 * @return the acctId
	 */
	public String getAcctId() {
		return acctId;
	}

	/**
	 * @param acctId the bpId to set
	 */
	public void setAcctId(String acctId) {
		this.acctId = acctId;
	}

	/**
	 * @return the serviceNbr
	public String getServiceNbr() { return serviceNbr; }
	*/
	/**
	 * @param serviceNbr the serviceNbr to set
	public void setServiceNbr(String serviceNbr) {
		this.serviceNbr = serviceNbr;
	}
	 */
	/**
	 * @return the assetIntegrationId

	public String getAssetIntegrationId() {
		return assetIntegrationId;
	}
	 */
	/**
	 * @param assetIntegrationId the assetIntegrationId to set

	public void setAssetIntegrationId(String assetIntegrationId) {
		this.assetIntegrationId = assetIntegrationId;
	}
	 */
	/**
	 * @return the prdId

	public Long getPrdId() {
		return prdId;
	}
	 */
	/**
	 * @param prdId the prdId to set

	public void setPrdId(Long prdId) {
		this.prdId = prdId;
	}
	 */
	/**
	 * @return the transType
	 */
	public Long getTransType() {
		return transType;
	}

	/**
	 * @param transType the transType to set
	 */
	public void setTransType(Long transType) {
		this.transType = transType;
	}

	/**
	 * @return the createStaff
	 */
	public Long getCreateStaff() {
		return createStaff;
	}

	/**
	 * @param createStaff the createStaff to set
	 */
	public void setCreateStaff(Long createStaff) {
		this.createStaff = createStaff;
	}

	/**
	 * @return the reqOfficeId
	 */
	public String getReqOfficeId() {
		return reqOfficeId;
	}

	/**
	 * @param reqOfficeId the reqOfficeId to set
	 */
	public void setReqOfficeId(String reqOfficeId) {
		this.reqOfficeId = reqOfficeId;
	}

	/**
	 * @return the createDate
	 */
	public String getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}



	public Long getStatusCd() {
		return statusCd;
	}

	public void setStatusCd(Long statusCd) {
		this.statusCd = statusCd;
	}

	/**
	 * @return the paymentDueDate
	 */
	public String getPaymentDueDate() {
		return paymentDueDate;
	}

	/**
	 * @param paymentDueDate the paymentDueDate to set
	 */
	public void setPaymentDueDate(String paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}

	/**
	 * @return the kbpBillRefNo

	public Long getKbpBillRefNo() { return kbpBillRefNo; }
	 */
	/**
	 * @param kbpBillRefNo the kbpBillRefNo to set

	public void setKbpBillRefNo(Long kbpBillRefNo) {
		this.kbpBillRefNo = kbpBillRefNo;
	}
	 */
	/**
	 * @return the kbpServId

	public Long getKbpServId() {
		return kbpServId;
	}
	 */
	/**
	 * @param kbpServId the kbpServId to set

	public void setKbpServId(Long kbpServId) {
		this.kbpServId = kbpServId;
	}
	 */
	/**
	 * @return the sourceType
	 */
	public Long getSourceType() {
		return sourceType;
	}

	/**
	 * @param sourceType the sourceType to set
	 */
	public void setSourceType(Long sourceType) {
		this.sourceType = sourceType;
	}

	/**
	 * @return the updateStaff
	 */
	public String getUpdateStaff() {
		return updateStaff;
	}

	/**
	 * @param updateStaff the updateStaff to set
	 */
	public void setUpdateStaff(String updateStaff) {
		this.updateStaff = updateStaff;
	}

	/**
	 * @return the updateDate
	 */
	public String getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/*public String getStateDesc() {
		if(this.state == -1 || this.state == 0 || this.state == 1 || this.state == 2){
			return "未处理";
		} else if(this.state == 13 ){
			return "出帐成功";
		} else {
			return "出帐失败";
		}
	}*/
	//1 拆机立即出账 2 不拆机立即出账  3宽带年付费立即出账
	public String getTransTypeDesc() {
		if(this.transType == 1L) {
			return "拆机立即出账";
		}else if (this.transType == 3L) {
			return "宽带年付费立即出账";
		} else {
			return "不拆机立即出账";
		}
	}
	/*public static PayInterimBillEntity toEntity(ResultSet rs) throws Exception{
		PayInterimBillEntity entity = new PayInterimBillEntity();
		entity.setSeq(rs.getLong("SEQ"));
		entity.setBpId(rs.getString("BP_ID"));
		entity.setServiceNbr(rs.getString("SERVICE_NBR"));
		entity.setAssetIntegrationId(rs.getString("ASSET_INTEGRATION_ID"));
		entity.setPrdId(rs.getLong("PRD_ID"));
		entity.setTransType(rs.getLong("TRANS_TYPE"));
		entity.setReqCsrId(rs.getString("REQ_CSR_ID"));
		entity.setReqOfficeId(rs.getString("REQ_OFFICE_ID"));
		entity.setState(rs.getLong("STATE"));
		entity.setKbpBillRefNo(rs.getLong("KBP_BILL_REF_NO"));
		entity.setKbpServId(rs.getLong("KBP_SERV_ID"));
		entity.setSourceType(rs.getLong("SOURCE_TYPE"));
		entity.setChgWho(rs.getString("CHG_WHO"));
		//entity.setChgDate(rs.getString("CHG_DATE"));
		entity.setAnnotation(rs.getString("ANNOTATION"));
		entity.setIamBillRefNo(rs.getString("IAM_BILL_REF_NO"));
		entity.setPaymentDueDate(rs.getString("PAYMENT_DUE_DATE"));
		entity.setReqDate(rs.getString("REQ_DATE"));
		return entity;
	}*/
	
	public long getSequence(Connection iamConn) throws Exception {
    	return WlwJdbcUtil.getMysqlSeqNextVal(iamConn,"PAY_INTERIM_SEQ"); 
	}
	
    public void insert(Connection iamConn) throws Exception {
    	int index = 0;
        PreparedStatement stmt = null;
        StringBuffer sql = new StringBuffer();
        sql.append("insert into PAY_INTERIM_BILL (");
        sql.append(" SEQ,                 ");
        sql.append(" CREATE_DATE,            ");
        //sql.append(" PAYMENT_DUE_DATE,    ");
        sql.append(" ACCT_ID,               ");
        //sql.append(" SERVICE_NBR,         ");
        //sql.append(" ASSET_INTEGRATION_ID,");
        //sql.append(" PRD_ID,              ");
        sql.append(" TRANS_TYPE,          ");
        sql.append(" CREATE_STAFF,          ");
        sql.append(" REQ_OFFICE_ID,       ");
        sql.append(" STATUS_CD,               ");
        sql.append(" SOURCE_TYPE         ");
        //sql.append(" ) values (?,sysdate,to_date(?,'yyyy-MM-dd'),?,?,?,?,?,?,?,?,?) ");
        sql.append(" ) values (?,now(),?,?,?,?,?,?) ");
        try {
            stmt = iamConn.prepareStatement(sql.toString());
            stmt.setLong(++index, this.getSeq());
            //stmt.setString(++index, this.getPaymentDueDate());
            stmt.setString(++index, this.getAcctId());
            //stmt.setString(++index, this.getServiceNbr());
            //stmt.setString(++index, this.getAssetIntegrationId());
//            if(null == this.getPrdId()) {
//            	stmt.setNull(++index,java.sql.Types.NUMERIC);
//            } else {
//            	stmt.setLong(++index, this.getPrdId());
//            }
            
            stmt.setLong(++index, this.getTransType());
            stmt.setLong(++index, this.getCreateStaff());
            stmt.setString(++index, this.getReqOfficeId());
            stmt.setLong(++index, this.getStatusCd());
            stmt.setLong(++index, this.getSourceType());
            stmt.executeUpdate();
        } finally {
        	WlwJdbcUtil.close(stmt);
        }
    }
    
    public void batchInsert(PreparedStatement stmt) throws Exception {
    	int index = 0;       
        stmt.setLong(++index, this.getSeq());
        stmt.setString(++index, this.getAcctId());   
        stmt.setLong(++index, this.getTransType());
        stmt.setLong(++index, this.getCreateStaff());
        stmt.setString(++index, this.getReqOfficeId());
        stmt.setLong(++index, this.getStatusCd());
        stmt.setLong(++index, this.getSourceType());
        stmt.addBatch();      
    }

    /*public void update(Connection iamConn) throws Exception {
    	int index = 0;
        PreparedStatement stmt = null;
        StringBuffer sql = new StringBuffer();
        sql.append("update PAY_INTERIM_BILL set CHG_DATE=sysdate,KBP_SERV_ID=?,KBP_BILL_REF_NO=?,IAM_BILL_REF_NO=?,CHG_WHO=?");
        sql.append(",state=?,ANNOTATION=?");
        sql.append(" where SEQ=? ");
        try {
            stmt = iamConn.prepareStatement(sql.toString());
            if(null == this.getKbpServId() || 0 == this.getKbpServId()){
            	stmt.setNull(++index, java.sql.Types.NUMERIC);
            } else {
            	stmt.setLong(++index, this.getKbpServId());
            }
            
            if(null == this.getKbpBillRefNo() || 0 == this.getKbpBillRefNo()){
            	stmt.setNull(++index, java.sql.Types.NUMERIC);
            } else {
            	stmt.setLong(++index, this.getKbpBillRefNo());
            }
            
            stmt.setString(++index, this.getIamBillRefNo());
            stmt.setString(++index, this.getChgWho());
            stmt.setLong(++index, this.getState());
            stmt.setString(++index, this.getAnnotation());
            stmt.setLong(++index, this.getSeq());
            stmt.executeUpdate();
        } finally {
        	WlwJdbcUtil.close(stmt);
        }
    }*/
    
//    public static PayInterimBillEntity query(long seq) throws Exception {
//    	PayInterimBillEntity entity = null;
//		String sql = "SELECT\n" +
//				"	SEQ, ACCT_ID, TRANS_TYPE, CREATE_STAFF, REQ_OFFICE_ID, STATUS_CD, SOURCE_TYPE, UPDATE_STAFF, UPDATE_DATE, REMARK, BILL_REF_NO,\n" +
//				"	DATE_FORMAT( PAYMENT_DUE_DATE, '%Y-%m-%d' ) PAYMENT_DUE_DATE, DATE_FORMAT ( CREATE_DATE, '%Y-%m-%d %H:%m:%s') CREATE_DATE\n" +
//				"FROM\n" +
//				"	PAY_INTERIM_BILL\n" +
//				"WHERE seq = ?" ;
//		try {
//			List<PayInterimBillEntity> list = JdbcTemplateSelector.queryForList(sql, new Long[] {seq}, PayInterimBillEntity.class, 20) ;
//			entity = list.get(0) ;
//		} catch (Exception e) {
//			throw e;
//		}
//		return entity;
//	}
    
	/**
	 * @return the billRefNo
	 */
	public String getBillRefNo() {
		return billRefNo;
	}

	/**
	 * @param billRefNo the billRefNo to set
	 */
	public void setBillRefNo(String billRefNo) {
		this.billRefNo = billRefNo;
	}

	public Long getCollFlagStatus() {
		return collFlagStatus;
	}

	public void setCollFlagStatus(Long collFlagStatus) {
		this.collFlagStatus = collFlagStatus;
	}
	
	public static void main(String[] args) {
		StringBuffer sql = new StringBuffer();
		sql.append("select   SEQ,BP_ID,SERVICE_NBR,ASSET_INTEGRATION_ID,PRD_ID,TRANS_TYPE,REQ_CSR_ID,REQ_OFFICE_ID,");
		sql.append("STATE,KBP_BILL_REF_NO,KBP_SERV_ID,SOURCE_TYPE,CHG_WHO,CHG_DATE,ANNOTATION,IAM_BILL_REF_NO,");
		sql.append("to_char(PAYMENT_DUE_DATE,'yyyy-MM-dd') PAYMENT_DUE_DATE,to_char(REQ_DATE,'yyyy-MM-dd hh24:mi:ss') REQ_DATE from PAY_INTERIM_BILL");
		sql.append(" where 1 = 1 and seq = ?");
		System.out.println(sql.toString());
	}
}
