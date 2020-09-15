
package Pro;

/**
 * @author zhangye
 *
 * 存储过程参数类
 */
public abstract class ProcParam {

	//参数索引
	private int index;
		
	//参数类型
	private int paramType;
	
	//如果是输入参数则VALUE是输入值，输出参数则VALUE是输出值
	private Object value;
	
	public ProcParam() {
	}

	public ProcParam(int index, int paramType, Object value) {
		this.index = index;
		this.paramType = paramType;
		this.value = value;
	}
	/**
	 * @return
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @return
	 */
	public int getParamType() {
		return paramType;
	}

	/**
	 * @return
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param i
	 */
	public void setIndex(int i) {
		index = i;
	}

	/**
	 * @param i
	 */
	public void setParamType(int i) {
		paramType = i;
	}

	/**
	 * @param object
	 */
	public void setValue(Object object) {
		value = object;
	}

}
