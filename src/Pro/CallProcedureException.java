
package Pro;


/**
 * 调用存储过程异常
 * @author zhangye
 */
public class CallProcedureException extends Exception {
	
	private static final long serialVersionUID = 504933403213249095L;
	
	public CallProcedureException(){
        super("调用存储过程时出错!");
    }
    public CallProcedureException(final String msg) {
        super(msg);
    }
}
