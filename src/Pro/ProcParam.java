
package Pro;

/**
 * @author zhangye
 *
 * �洢���̲�����
 */
public abstract class ProcParam {

	//��������
	private int index;
		
	//��������
	private int paramType;
	
	//��������������VALUE������ֵ�����������VALUE�����ֵ
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
