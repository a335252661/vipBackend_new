package Pro;

public class JdbcException extends Exception {

	private static final long serialVersionUID = 201819062098473181L;
	
	public JdbcException(){
        super("操作数据库时出错!");
    }
    public JdbcException(final String msg) {
        super(msg);
    }
}
