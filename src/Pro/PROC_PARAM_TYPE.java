
package Pro;

/**
 * @author zhangye
 *
 * �洢���̲�������
 */
public interface PROC_PARAM_TYPE {
	
	//�ַ�������
	public static final int VARCHAR = java.sql.Types.VARCHAR;
	
	//��������
	public static final int INTEGER = java.sql.Types.INTEGER;
	
	//��������
	public static final int DATE = java.sql.Types.DATE;
	
	//���������� ��ʱ����
	public static final int TIMESTAMP = java.sql.Types.TIMESTAMP;
	
	//�α�
	public static final int CURSOR = oracle.jdbc.OracleTypes.CURSOR;
	
	//�ṹ
	public static final int STRUCT = oracle.jdbc.OracleTypes.STRUCT;
	
	public static final int ARRAY = oracle.jdbc.OracleTypes.ARRAY;
	
	
	//����������
	public static final int LONG = java.sql.Types.NUMERIC;
}
