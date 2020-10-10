
package Pro;

import com.ibm.ebiz.base.BizComponent;
import helps.DateTimeHelp;
import oracle.jdbc.OracleCallableStatement;
import oracle.sql.ARRAY;
import oracle.sql.STRUCT;
import org.apache.commons.lang.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangye
 *
 * 存储过程调用类
 */
public class ProcUtil {
	
	/**
	 * 调用存储过程方法
	 * @param dbConn	 数据库链接
	 * @param paramList  输入输出参数集合
	 * @return
	 * @throws Exception
	 */
	public static void callProc(Connection dbConn, String procName, List paramList) throws CallProcedureException{
		ProcParam procParam = null;
		ProcOutputParam procOutputParam = null;
		List outParamList = new ArrayList();
		CallableStatement callStmt = null;
		ResultSet rs = null;
		String callProcStr = null;
		try {
			callProcStr = getCallProcStr(procName,paramList.size());
			callStmt = dbConn.prepareCall(callProcStr);
			for(int i = 0;i < paramList.size();i++){
				procParam = (ProcParam)paramList.get(i);
				if(procParam instanceof ProcInputParam){
					//设置输入参数
					setIntputParam(callStmt,procParam);
				} else if(procParam instanceof ProcOutputParam){
					//设置输出参数
					if(PROC_PARAM_TYPE.ARRAY == procParam.getParamType()){
						ProcOutputParam tmp = (ProcOutputParam)procParam;
						callStmt.registerOutParameter(procParam.getIndex(), procParam.getParamType(),tmp.getObjectName());
					} else {
						callStmt.registerOutParameter(procParam.getIndex(), procParam.getParamType());
					}
					
					outParamList.add(procParam);
				}
			}
			//执行存储过程
			callStmt.execute();
			
			//接收输出参数
			for(int i = 0;i < outParamList.size();i++){
				procOutputParam = (ProcOutputParam)outParamList.get(i);
				switch (procOutputParam.getParamType()) {
					case PROC_PARAM_TYPE.VARCHAR:
						procOutputParam.setValue(callStmt.getString(procOutputParam.getIndex()));
						break;
					case PROC_PARAM_TYPE.INTEGER:
						procOutputParam.setValue(callStmt.getString(procOutputParam.getIndex()));
						break;
					case PROC_PARAM_TYPE.LONG:
						procOutputParam.setValue(callStmt.getString(procOutputParam.getIndex()));
						break;
					case PROC_PARAM_TYPE.DATE:
						procOutputParam.setValue(callStmt.getDate(procOutputParam.getIndex()));
						break;
					case PROC_PARAM_TYPE.TIMESTAMP:
						procOutputParam.setValue(callStmt.getTimestamp(procOutputParam.getIndex()));
						break;
					case PROC_PARAM_TYPE.CURSOR:
							rs = (ResultSet) callStmt.getObject(procOutputParam.getIndex());
//							if(PROC_RETURN_TYPE.COLLECTION == procOutputParam.getReturnType()){
							procOutputParam.setValue(JdbcUtil.getMaps(rs));
//							} else if(PROC_RETURN_TYPE.BIZ_COMPONENT == procOutputParam.getReturnType()) {
//								BizComponent comp = new BizComponent("BizComponent");
//								comp.fromResultSet(rs);
//								procOutputParam.setValue(comp);
//							}
						//procParam.setValue(call.getString(procParam.getIndex()));
						break;
					case PROC_PARAM_TYPE.ARRAY:
						procOutputParam.setValue(callStmt.getArray(procOutputParam.getIndex()).getArray());
						break;
					default:
						break;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new CallProcedureException("调用存储过程出错! PROC_NAME = "+callProcStr);
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(callStmt);
		}
	}
	
	/**
	 * 调用存储过程方法查询
	 * 调用该方法时，请遵守下列规则
	 * 1)数组顺序必须按入参顺序放置,该方法会将参数默认以VARCHAR类型传入
	 * 2)存储过程最后一个参数必须是输出游标参数
	 * @param dbConn	 数据库链接
	 * @param procName   存储过程名
	 * @param paramArr   输入参数数组 数组顺序应按入参顺序放置
	 * @return 查询结果
	 * @throws CallProcedureException 
	 */
	
	public static List queryForList(Connection dbConn, String procName, Object[] paramArr) throws CallProcedureException {
		List paramList = setIntputParam(paramArr);
		ProcOutputParam outputParam = new ProcOutputParam(paramArr.length + 1,PROC_PARAM_TYPE.CURSOR);
		paramList.add(outputParam);
		callProc(dbConn,procName,paramList);
		return (List)outputParam.getValue();
	}
	
	/**
	 * 调用存储过程方法查询
	 * 调用该方法时，请遵守下列规则
	 * 1)数组顺序必须按入参顺序放置,该方法会将参数默认以VARCHAR类型传入
	 * 2)存储过程最后一个参数必须是输出游标参数
	 * @param dbConn	 数据库链接
	 * @param procName   存储过程名
	 * @param paramArr   输入参数数组 数组顺序应按入参顺序放置
	 * @return 查询结果
	 * @throws CallProcedureException 
	 */
	
	public static BizComponent queryForComponent(Connection dbConn, String procName, Object[] paramArr, String compName) throws CallProcedureException {
		BizComponent comp = new BizComponent(compName);
		List list =queryForList(dbConn,procName,paramArr);
		comp.fromList(list);
		return comp;
	}
	
	/**
	 * 用于更新数据库
	 * 调用该方法时，请遵守下列规则
	 * 1)数组顺序必须按入参顺序放置,该方法会将参数默认以VARCHAR类型传入
	 * 2)存储过程不能有输出参数
	 * @param dbConn	 数据库链接
	 * @param procName   存储过程名
	 * @param paramArr   输入参数数组 数组顺序应按入参顺序放置
	 * @throws CallProcedureException 
	 */
	
	public static void callProc(Connection dbConn, String procName, Object[] paramArr) throws CallProcedureException {
		DateTimeHelp.start();
		System.out.println(paramArr[0].toString());
		List paramList = setIntputParam(paramArr);
		callProc(dbConn,procName,paramList);
		DateTimeHelp.end();
	}
	
	private static int getParamAmount(String procName){
		if(StringUtils.isNotEmpty(procName)){
			return procName.split(",").length;
		}
		return 0;
	}
	
	public static List setIntputParam(Object[] paramArr){
		List paramList = new ArrayList();
		for(int i = 0;i< paramArr.length;i++){
			paramList.add(new ProcInputParam(i+1,PROC_PARAM_TYPE.VARCHAR,paramArr[i]));
		}
		return paramList;
	}
	
	private static void setIntputParam(CallableStatement callStmt, ProcParam procParam) throws SQLException {
		if(null == procParam.getValue()){
			callStmt.setNull(procParam.getIndex(),procParam.getParamType());
		} else {
			switch (procParam.getParamType()) {
				case PROC_PARAM_TYPE.VARCHAR:
					callStmt.setString(procParam.getIndex(),String.valueOf(procParam.getValue()));
					break;
				case PROC_PARAM_TYPE.INTEGER:
					callStmt.setInt(procParam.getIndex(),Integer.valueOf(procParam.getValue().toString()).intValue());
					break;
				case PROC_PARAM_TYPE.LONG:
					callStmt.setLong(procParam.getIndex(),Long.valueOf(procParam.getValue().toString()).longValue());
					break;
				case PROC_PARAM_TYPE.STRUCT:
					((OracleCallableStatement) callStmt).setSTRUCT(procParam.getIndex(),(STRUCT)procParam.getValue());
					break;
				case PROC_PARAM_TYPE.ARRAY:
					((OracleCallableStatement) callStmt).setARRAY(procParam.getIndex(),(ARRAY)procParam.getValue());
					break;
				case PROC_PARAM_TYPE.DATE:
					callStmt.setDate(procParam.getIndex(),Date.valueOf(String.valueOf(procParam.getValue())));
					break;
				default:
					break;
			}
		}
	}
	
	public static String getCallProcStr(String procName, int paramAmount){
		StringBuffer callProcStr = new StringBuffer();
		callProcStr.append("{ call ")
					.append(procName)
					.append("(");
		for(int i = 0;i < paramAmount;i++){
			if(i != 0){
				callProcStr.append(",");
			}
			callProcStr.append("?");
		}
		callProcStr.append(") }");
		return callProcStr.toString();
	}
}
