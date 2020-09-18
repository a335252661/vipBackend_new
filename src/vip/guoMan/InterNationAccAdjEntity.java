package vip.guoMan;

import java.math.BigDecimal;

public class InterNationAccAdjEntity {
	
	//��ˮ��
	private String trackingId;
	//�豸��
	private String serviceNbr;
	//��������
	private String productLineId;
	//�˵������Ч����
	private String effectiveDate;
	//�˵��·�
	private String statementDate;
	//�������
	private String accountNo;
	//�˵����
	private String billRefNo;
	//�豸��
	private String subscr_no;
	//���ε������
	private BigDecimal expectAdjFee;
	
	public InterNationAccAdjEntity(){}
	
	public InterNationAccAdjEntity(String trackingId, String serviceNbr, String productLineId, String effectiveDate,
                                   String statementDate, String accountNo, String billRefNo, String subscr_no, BigDecimal expectAdjFee) {
		super();
		this.trackingId = trackingId;
		this.serviceNbr = serviceNbr;
		this.productLineId = productLineId;
		this.effectiveDate = effectiveDate;
		this.statementDate = statementDate;
		this.accountNo = accountNo;
		this.billRefNo = billRefNo;
		this.subscr_no = subscr_no;
		this.expectAdjFee = expectAdjFee;
	}

	public String getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}

	public String getServiceNbr() {
		return serviceNbr;
	}

	public void setServiceNbr(String serviceNbr) {
		this.serviceNbr = serviceNbr;
	}

	public String getProductLineId() {
		return productLineId;
	}

	public void setProductLineId(String productLineId) {
		this.productLineId = productLineId;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getStatementDate() {
		return statementDate;
	}

	public void setStatementDate(String statementDate) {
		this.statementDate = statementDate;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getBillRefNo() {
		return billRefNo;
	}

	public void setBillRefNo(String billRefNo) {
		this.billRefNo = billRefNo;
	}

	public String getSubscr_no() {
		return subscr_no;
	}

	public void setSubscr_no(String subscr_no) {
		this.subscr_no = subscr_no;
	}

	public BigDecimal getExpectAdjFee() {
		return expectAdjFee;
	}

	public void setExpectAdjFee(BigDecimal expectAdjFee) {
		this.expectAdjFee = expectAdjFee;
	}
	
}
