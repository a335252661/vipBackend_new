package vip.IDCLoad;

/**
 * 
 */
import org.apache.commons.lang.StringUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * 文件处理日志表
 * @author Administrator
 *
 */
public class JtBillLog {

	private String fileName;
	private String createDate;
	private String endDate;
	
	//0：新建，1：处理中，2：结束，-1：失败
	private long status;
	
	//1：物联网，2：云提
	private long serviceType;
	
	//1：稽核文件，2：帐单文件，3：拒收文件，4：错误文件，5：空文件，6：AC确认文件，7：销账文件，8：调整文件
	private long fileType;
	
	//稽核文件名指行数；帐单文件指大小（稽核文件中的大小）
	private long fileSize;
	
	private String errCode;
	private String errDesc;
	private String createBy;
	private String chgWho;
	private String annotation;
	
	private long batchNo;
	
	
	public long getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(long batchNo) {
		this.batchNo = batchNo;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
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
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
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
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getChgWho() {
		return chgWho;
	}
	public void setChgWho(String chgWho) {
		this.chgWho = chgWho;
	}
	public String getAnnotation() {
		return annotation;
	}
	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
	
	public static long getSeq(Connection iamConn) throws Exception {
    	return WlwJdbcUtil.getSeqNextVal(iamConn,"SEQ_JT_BILL_LOG");
    }
	
	public void insert(Connection iamConn) throws Exception {
    	int index = 0;
        PreparedStatement stmt = null;
        StringBuffer sql = new StringBuffer();
        sql.append(" insert into JT_BILL_LOG( ");
        sql.append("FILE_NAME,");
        sql.append("STATUS,");
        sql.append("SERVICE_TYPE,");
        sql.append("FILE_TYPE,");
        sql.append("FILE_SIZE,");
        sql.append("ERR_CODE,");
        sql.append("ERR_DESC,");
        sql.append("CREATE_BY,");
        sql.append("BATCH_NO, ");
        sql.append("ANNOTATION, ");
        sql.append("CREATE_DATE");
        sql.append(" ) values ( ?,?,?,?,?,?,?,?,?,?,sysdate) ");
        try {
            stmt = iamConn.prepareStatement(sql.toString());
            stmt.setString(++index, this.getFileName());
            stmt.setLong(++index, this.getStatus());
            stmt.setLong(++index, this.getServiceType());
            stmt.setLong(++index, this.getFileType());
            stmt.setLong(++index, this.getFileSize());
            stmt.setString(++index, this.getErrCode());
            stmt.setString(++index, this.getErrDesc());
            stmt.setString(++index, this.getCreateBy());
            stmt.setLong(++index, this.getBatchNo());
            stmt.setString(++index, this.getAnnotation());
            stmt.executeUpdate();
        } finally {
        	WlwJdbcUtil.close(stmt);
        }
    }
	
	public static List<JtBillLog> query(Connection iamConn, String status, String fileType, String serviceType) throws Exception {
    	StringBuffer sql = new StringBuffer();
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        int index = 0;
        List<JtBillLog> list = new ArrayList<JtBillLog>();
    	sql.append("select FILE_NAME,CREATE_DATE,END_DATE,STATUS,SERVICE_TYPE,FILE_TYPE,FILE_SIZE,ERR_CODE,ERR_DESC,BATCH_NO ");
    	sql.append(" from JT_BILL_LOG ");
    	
    	sql.append(" where 1 = 1 ");
    	
    	if(StringUtils.isNotEmpty(status)){
    		sql.append(" and STATUS = ? ");
    	}
    	
    	if(StringUtils.isNotEmpty(fileType)){
    		sql.append(" and FILE_TYPE in (").append(fileType).append(")");
    	}
    	
    	if(StringUtils.isNotEmpty(serviceType)){
    		sql.append(" and SERVICE_TYPE = ? ");
    	}
    	try {
    		stmt = iamConn.prepareStatement(sql.toString());
    		if(StringUtils.isNotEmpty(status)){
    			stmt.setInt(++index, Integer.parseInt(status));
        	}
    		
//    		if(StringUtils.isNotEmpty(fileType)){
//    			stmt.setInt(++index, Integer.parseInt(fileType));
//        	}
    		
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
	
	public void update(Connection iamConn) throws Exception {
		int index = 0;
       PreparedStatement stmt = null;
       StringBuffer sql = new StringBuffer();
       sql.append("update JT_BILL_LOG set END_DATE = sysdate");
       sql.append(",STATUS = ? ");
       
       sql.append(" where BATCH_NO = ? and FILE_NAME = ?");
       try {
           stmt = iamConn.prepareStatement(sql.toString());
           stmt.setLong(++index, this.getStatus());
           stmt.setLong(++index, this.getBatchNo());
           stmt.setString(++index, this.getFileName());
           stmt.executeUpdate();
       } finally {
       	WlwJdbcUtil.close(stmt);
       }
	}
	
	private static List<JtBillLog> fromResultSet(ResultSet rs) throws SQLException {
        List<JtBillLog> list = new ArrayList<JtBillLog>();
        JtBillLog entity = null;
        while (rs.next()) {
        	entity = new JtBillLog();
        	entity.setFileName(rs.getString("FILE_NAME"));
        	entity.setServiceType(rs.getLong("SERVICE_TYPE"));
        	entity.setBatchNo(rs.getLong("BATCH_NO"));
            list.add(entity);
        }
        return list;
    }
}
