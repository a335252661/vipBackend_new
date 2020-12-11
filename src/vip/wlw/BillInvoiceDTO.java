package vip.wlw;

import lombok.Data;
//import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class BillInvoiceDTO {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 帐户标识
     * 表字段 : ACCT_ID
     */
    private Long acctId;

    /**
     * 帐单编号
     * 表字段 : BILL_REF_NO
     */
    private String billRefNo;

    /**
     * 账单支付方式(付费账户的支付方式
     * 表字段 : PAY_METHOD
     */
    private Integer payMethod;

    /**
     * 帐期(YYYYMMDD)
     * 表字段 : BILLING_CYCLE_ID
     */
    private Integer billingCycleId;

    /**
     * 帐期符号标记
     * -n:时间范围判断条件，n为时间戳的差值,0:"=",1:">",2:">=",3:"<",4:"<=",5:"like",6:"<>",7:"!=",8:"null",9:"notNull"
     */
    private Long billingCycleIdMark = 0L;

    /**
     * 账期开始日期
     * 表字段 : FROM_DATE
     */
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fromDate;

    /**
     * 账期结束日期
     * 表字段 : TO_DATE
     */
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date toDate;

    /**
     * 账单生成的系统时间（default sysdate）
     * 表字段 : CREATE_DATE
     */
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    /**
     * 最后付款日期
     * 表字段 : PAYMENT_DUE_DATE
     */
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paymentDueDate;

    /**
     * 违约金起算日期
     * 表字段 : LATE_FEE_FROM_DATE
     */
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lateFeeFromDate;

    /**
     * 开账频率（单位：月）
     * 表字段 : BILLING_FREQUENCY
     */
    private Integer billingFrequency;

    /**
     * 欠费不确认收入标志（3,4,5,6）(现有字段DISPATCH_COUNT)
     * 表字段 : COLL_FlAG
     */
    private Integer collFlag;

    /**
     * 欠费不确认收入账单全额销账或调账后，置系统日期(现有字段DISPATCH_DATE)
     * 表字段 : COLL_RECOVERY_DATE
     */
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date collRecoveryDate;

    /**
     * 立即出账标志 1 是 0 否 default 0
     * 表字段 : INTERIM_BILL_FLAG
     */
    private Integer interimBillFlag;

    /**
     * 账单违约金免除标志 1 免除 0 不免除 default 0
     * 表字段 : LATE_FEE_EXEMPT_FLAG
     */
    private Integer lateFeeExemptFlag;

    /**
     * 状态
     * 表字段 : STATUS_CD
     */
    private Integer statusCd;

    private Long statusCdMark = 0L;
    private List<Integer> statusCdList4In;
    /**
     * 余额变为0的日期
     * 表字段 : CLOSE_DATE
     */
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date closeDate;

    /**
     * 账单原始总金额(账单明细中 租费、使用费、一次费、调增费用、结转违约金、结转零头费的总额）
     * 表字段 : NEW_CHARGES
     */
    private Long newCharges;

    /**
     * 零头费
     * 表字段 : ODD_CHARGES
     */
    private Integer oddCharges;

    /**
     * 账单调整费用
     * 表字段 : TOTAL_ADJ
     */
    private Long totalAdj;

    /**
     * 支付金额
     * 表字段 : TOTAL_PAID
     */
    private Long totalPaid;

    /**
     * 账单欠费余额=SUM(new_charges+odd_charges+total_adj+total_paid)
     * 表字段 : BALANCE_DUE
     */
    private Long balanceDue;

    /**
     * 当账单汇总后，总金额为负，则在预存抵扣前，将负金额的BALANCE_DUE作为溢收款存入ABM中，而NEW_CHARGES为0，BALANCE_DUE为0，保证不出现BALANCE_DUE<0的情况
     * 表字段 : ORIG_NEW_CHARGES
     */
    private Long origNewCharges;

    /**
     * 备注1
     * 表字段 : REMARK1
     */
    private String remark1;

    /**
     * 备注2
     * 表字段 : REMARK2
     */
    private String remark2;

    /**
     * 修改时间
     * 表字段 : UPDATE_DATE
     */
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    /**
     * 修改人
     * 表字段 : UPDATE_STAFF
     */
    private Long updateStaff;

    /**
     * 分批标识
     * 表字段 : PART
     */
    private Integer part;

    /**
     * 帐单类型（区分物联网、IDC、卫星等集团出帐帐单）
     * 表字段 : bill_type
     */
    private Integer billType;

    /**
     * 是否年付帐单
     * 表字段 : YEARLY_BILL_FLAG
     */
    private Integer yearlyBillFlag;

    /**
     * 预后属性
     * 表字段 : acct_type
     */
    private Integer acctType;

    /**
     * 备用字段数字1
     * 表字段 : RESERVE_NUM1
     */
    private Long reserveNum1;

    /**
     * 备用字段数字2
     * 表字段 : RESERVE_NUM2
     */
    private Long reserveNum2;

    /**
     * 备用字段字符1
     * 表字段 : RESERVE_CHAR1
     */
    private String reserveChar1;

    /**
     * 备用字段字符2
     * 表字段 : RESERVE_CHAR2
     */
    private String reserveChar2;

    /**
     * 备用字段字符3
     * 表字段 : RESERVE_CHAR3
     */
    private String reserveChar3;

    /**
     * 表字段 : cust_id
     */
    private String custId;

    private Long balanceDueMark = 0L;

    private Long createDateMark = 0L;


    private Long totalPaidLateFee;

    private Long totalPaidLateFeeMark = 0L;

    public Long getAcctId() {
        return acctId;
    }

    public void setAcctId(Long acctId) {
        this.acctId = acctId;
    }

    public String getBillRefNo() {
        return billRefNo;
    }

    public void setBillRefNo(String billRefNo) {
        this.billRefNo = billRefNo;
    }

    public Integer getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }

    public Integer getBillingCycleId() {
        return billingCycleId;
    }

    public void setBillingCycleId(Integer billingCycleId) {
        this.billingCycleId = billingCycleId;
    }

    public Long getBillingCycleIdMark() {
        return billingCycleIdMark;
    }

    public void setBillingCycleIdMark(Long billingCycleIdMark) {
        this.billingCycleIdMark = billingCycleIdMark;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(Date paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    public Date getLateFeeFromDate() {
        return lateFeeFromDate;
    }

    public void setLateFeeFromDate(Date lateFeeFromDate) {
        this.lateFeeFromDate = lateFeeFromDate;
    }

    public Integer getBillingFrequency() {
        return billingFrequency;
    }

    public void setBillingFrequency(Integer billingFrequency) {
        this.billingFrequency = billingFrequency;
    }

    public Integer getCollFlag() {
        return collFlag;
    }

    public void setCollFlag(Integer collFlag) {
        this.collFlag = collFlag;
    }

    public Date getCollRecoveryDate() {
        return collRecoveryDate;
    }

    public void setCollRecoveryDate(Date collRecoveryDate) {
        this.collRecoveryDate = collRecoveryDate;
    }

    public Integer getInterimBillFlag() {
        return interimBillFlag;
    }

    public void setInterimBillFlag(Integer interimBillFlag) {
        this.interimBillFlag = interimBillFlag;
    }

    public Integer getLateFeeExemptFlag() {
        return lateFeeExemptFlag;
    }

    public void setLateFeeExemptFlag(Integer lateFeeExemptFlag) {
        this.lateFeeExemptFlag = lateFeeExemptFlag;
    }

    public Integer getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(Integer statusCd) {
        this.statusCd = statusCd;
    }

    public Long getStatusCdMark() {
        return statusCdMark;
    }

    public void setStatusCdMark(Long statusCdMark) {
        this.statusCdMark = statusCdMark;
    }

    public List<Integer> getStatusCdList4In() {
        return statusCdList4In;
    }

    public void setStatusCdList4In(List<Integer> statusCdList4In) {
        this.statusCdList4In = statusCdList4In;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public Long getNewCharges() {
        return newCharges;
    }

    public void setNewCharges(Long newCharges) {
        this.newCharges = newCharges;
    }

    public Integer getOddCharges() {
        return oddCharges;
    }

    public void setOddCharges(Integer oddCharges) {
        this.oddCharges = oddCharges;
    }

    public Long getTotalAdj() {
        return totalAdj;
    }

    public void setTotalAdj(Long totalAdj) {
        this.totalAdj = totalAdj;
    }

    public Long getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(Long totalPaid) {
        this.totalPaid = totalPaid;
    }

    public Long getBalanceDue() {
        return balanceDue;
    }

    public void setBalanceDue(Long balanceDue) {
        this.balanceDue = balanceDue;
    }

    public Long getOrigNewCharges() {
        return origNewCharges;
    }

    public void setOrigNewCharges(Long origNewCharges) {
        this.origNewCharges = origNewCharges;
    }

    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getUpdateStaff() {
        return updateStaff;
    }

    public void setUpdateStaff(Long updateStaff) {
        this.updateStaff = updateStaff;
    }

    public Integer getPart() {
        return part;
    }

    public void setPart(Integer part) {
        this.part = part;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public Integer getYearlyBillFlag() {
        return yearlyBillFlag;
    }

    public void setYearlyBillFlag(Integer yearlyBillFlag) {
        this.yearlyBillFlag = yearlyBillFlag;
    }

    public Integer getAcctType() {
        return acctType;
    }

    public void setAcctType(Integer acctType) {
        this.acctType = acctType;
    }

    public Long getReserveNum1() {
        return reserveNum1;
    }

    public void setReserveNum1(Long reserveNum1) {
        this.reserveNum1 = reserveNum1;
    }

    public Long getReserveNum2() {
        return reserveNum2;
    }

    public void setReserveNum2(Long reserveNum2) {
        this.reserveNum2 = reserveNum2;
    }

    public String getReserveChar1() {
        return reserveChar1;
    }

    public void setReserveChar1(String reserveChar1) {
        this.reserveChar1 = reserveChar1;
    }

    public String getReserveChar2() {
        return reserveChar2;
    }

    public void setReserveChar2(String reserveChar2) {
        this.reserveChar2 = reserveChar2;
    }

    public String getReserveChar3() {
        return reserveChar3;
    }

    public void setReserveChar3(String reserveChar3) {
        this.reserveChar3 = reserveChar3;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public Long getBalanceDueMark() {
        return balanceDueMark;
    }

    public void setBalanceDueMark(Long balanceDueMark) {
        this.balanceDueMark = balanceDueMark;
    }

    public Long getCreateDateMark() {
        return createDateMark;
    }

    public void setCreateDateMark(Long createDateMark) {
        this.createDateMark = createDateMark;
    }

    public Long getTotalPaidLateFee() {
        return totalPaidLateFee;
    }

    public void setTotalPaidLateFee(Long totalPaidLateFee) {
        this.totalPaidLateFee = totalPaidLateFee;
    }

    public Long getTotalPaidLateFeeMark() {
        return totalPaidLateFeeMark;
    }

    public void setTotalPaidLateFeeMark(Long totalPaidLateFeeMark) {
        this.totalPaidLateFeeMark = totalPaidLateFeeMark;
    }
}