package Pro;

public class JdbcException extends Exception {

	private static final long serialVersionUID = 201819062098473181L;
	
	public JdbcException(){
        super("�������ݿ�ʱ����!");
    }
    public JdbcException(final String msg) {
        super(msg);
    }
}
