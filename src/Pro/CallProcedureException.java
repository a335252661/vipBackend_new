
package Pro;


/**
 * ���ô洢�����쳣
 * @author zhangye
 */
public class CallProcedureException extends Exception {
	
	private static final long serialVersionUID = 504933403213249095L;
	
	public CallProcedureException(){
        super("���ô洢����ʱ����!");
    }
    public CallProcedureException(final String msg) {
        super(msg);
    }
}
