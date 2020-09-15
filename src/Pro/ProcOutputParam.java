
package Pro;


/**
 * @author zhangye
 *
 * 存储过程输出参数类
 */
public class ProcOutputParam extends ProcParam {
	
	private int returnType;
	
	private String objectName;
	
	
	/**
	 * @return the objectName
	 */
	public String getObjectName() {
		return objectName;
	}

	/**
	 * @param objectName the objectName to set
	 */
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public ProcOutputParam(int index, int paramType) {
		super(index, paramType,null);
		this.returnType = PROC_RETURN_TYPE.COLLECTION;
	}
	
	public ProcOutputParam(int index, int paramType,int returnType) {
		super(index, paramType,null);
		this.returnType = returnType;
	}

	public ProcOutputParam(int index, int paramType,String objectName) {
		super(index, paramType,null);
		this.objectName = objectName;
		this.returnType = PROC_RETURN_TYPE.COLLECTION;
	}
	/**
	 * @return
	 */
	public int getReturnType() {
		return returnType;
	}

	/**
	 * @param string
	 */
	public void setReturnType(int string) {
		returnType = string;
	}

}
