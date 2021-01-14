package vip.wlwcoll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import utils.DBConn;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账务oracle复制库操作类
 * 该数据库连接没有做多线程处理
 */
public class JTRepJdbcUtil {


	public static Connection connnection = null;

	private static PreparedStatement preparedStatement = null;

	private static CallableStatement callableStatement = null;

	private static ResultSet resultSet = null;
	
//	@Autowired
//	@Qualifier("oracleCopyDataSource")
	private DataSource oracleCopyDataSource ;
	
	
	private static DataSource dataSource ;
	
//	@PostConstruct
	public void init() { 
		dataSource = oracleCopyDataSource ;
	}
		

	public static Connection getConnection() throws SQLException {
		return dataSource.getConnection() ;
	}

	/**
	 * 执行更新操作,可以是增删改,返回受影响的条数
	 */
	public static int executeUpdate(String sql, Object... params) {
		int affectedLine = 0;
		Connection connnection = DBConn.getCopyProConn();;
		try {
//			connnection = getConnection();
			preparedStatement = connnection.prepareStatement(sql);

			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					preparedStatement.setObject(i + 1, params[i]);
				}
			}

			affectedLine = preparedStatement.executeUpdate();
			connnection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll();
		}
		return affectedLine;
	}

	private static ResultSet executeQueryRS(String sql, Object[] params) {
		try {
			connnection = getConnection();
			preparedStatement = connnection.prepareStatement(sql);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					preparedStatement.setObject(i + 1, params[i]);
				}
			}
			resultSet = preparedStatement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	public static Object executeQuerySingle(String sql, Object[] params) {
		Object object = null;
		try {
			connnection = getConnection();
			preparedStatement = connnection.prepareStatement(sql);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					preparedStatement.setObject(i + 1, params[i]);
				}
			}
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				object = resultSet.getObject(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll();
		}

		return object;
	}
	
	
	/**
	 * 数据库 查询 操作接口
	 * 将params参数动态地传入预定义SQL语句 查询结果保存在List中返回
	 */
	public static List<Object[]> excuteQueryS(String sql, Object... params)  {
		List<Object[]> resultList = new ArrayList<Object[]>();
		try {
			ResultSet rs = executeQueryRS(sql, params);
			while (rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();

				Object[] rowValue = new Object[columnCount];
				for (int i = 0; i < rowValue.length; i++) {
					rowValue[i] = rs.getObject(i + 1);
				}
				resultList.add(rowValue);
			}
		} catch (SQLException e) {
			// 发生SQL异常把connection放进池子里面
			e.printStackTrace();
		} finally {
			closeAll();
		}
		return resultList;
	}

	public static List<Object> excuteQuery(String sql, Object... params) {
		ResultSet rs = executeQueryRS(sql, params);

		ResultSetMetaData rsmd = null;

		int columnCount = 0;
		try {
			rsmd = rs.getMetaData();

			columnCount = rsmd.getColumnCount();
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.out.println(e1.getMessage());
		}

		List<Object> list = new ArrayList<Object>();

		try {
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					map.put(rsmd.getColumnLabel(i), rs.getObject(i));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll();
		}

		return list;
	}

	public static List<List<Object>> excuteQuery2(String sql, Object... params) {
		ResultSet rs = executeQueryRS(sql, params);

		ResultSetMetaData rsmd = null;

		int columnCount = 0;
		try {
			rsmd = rs.getMetaData();

			columnCount = rsmd.getColumnCount();
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.out.println(e1.getMessage());
		}

		List<List<Object>> list = new ArrayList<List<Object>>();

		try {
			while (rs.next()) {
				List<Object> l = new ArrayList<Object>();
				for (int i = 1; i <= columnCount; i++) {
					l.add(rs.getObject(i));
				}
				list.add(l);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll();
		}

		return list;
	}

	public static Object excuteQuery(String sql, Object[] params, int outParamPos, int SqlType) {
		Object object = null;
		try {
			connnection = getConnection();
			callableStatement = connnection.prepareCall(sql);

			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					callableStatement.setObject(i + 1, params[i]);
				}
			}

			callableStatement.registerOutParameter(outParamPos, SqlType);

			callableStatement.execute();

			object = callableStatement.getObject(outParamPos);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll();
		}
		return object;
	}

	public static void closeAll() {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (callableStatement != null) {
			try {
				callableStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (connnection != null) {
			try {
				connnection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

//	/**
//	 * 根据一组sqldata插入至数据库 注意:插入数据列必须与表列一致
//	 */
//	public static void insertCustInfo(List<SqlData> listSql, final String sql) throws ArrayIndexOutOfBoundsException, SQLException {
//		PreparedStatement ps = null;
//		int lastrow = 0;
//		for (SqlData sqlData : listSql) {
//			List<String[]> li = sqlData.getList();
//			Connection conne = getConnection();
//			try {
//				conne.setAutoCommit(false);
//				ps = conne.prepareStatement(sql);
//				for (int j = 0; j < li.size(); j++) {
//					lastrow = j;
//					setparam(ps, li.get(j));
//					ps.addBatch();
//					if ((j + 1) % 1000 == 0) {
//						ps.executeBatch();
//					}
//				}
//				ps.executeBatch();
//				conne.commit();
//				conne.close();
//			} catch (SQLException e) {
//				throw new SQLException("执行sql" + sqlData.getFile().getName() + "时错误,在文件" + sqlData.getFile().getName() + ",内容格式不正确.\r\n"+e.getMessage());
//			} catch (ArrayIndexOutOfBoundsException e) {
//				throw new ArrayIndexOutOfBoundsException("设置参数时错误,在第" + lastrow + "行,文件"+ sqlData.getFile().getName() +",列数不正确.\r\n"+e.getMessage());
//			} finally {
//				closeAll();
//			}
//		}
//	}

	public static void setparam(PreparedStatement ps, Object... obj) throws SQLException {
		for (int i = 0; i < obj.length; i++) {
			ps.setObject(i + 1, obj[i]);
		}
	}
	
	
	public static void batchExecuteUpdate(String sql, List<Object[]> paramList) {
		try {
			connnection = getConnection();
			connnection.setAutoCommit(false);
			preparedStatement = connnection.prepareStatement(sql);
			int count = 0 ;
			for(Object[] params : paramList) {
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						preparedStatement.setObject(i + 1, params[i]);
					}
					preparedStatement.addBatch();
	                if(++count == 1000){ //500提交一次
	                	preparedStatement.executeBatch();
	                	connnection.commit(); 
	                	preparedStatement.clearBatch();
	                	count = 0 ;
	                }
				}
			}
			preparedStatement.executeBatch();
			connnection.commit(); 
			preparedStatement.clearBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll();
		}
	}


	/**
	 * 数据库 查询 操作接口
	 * 将params参数集合动态地传入预定义SQL语句 查询结果保存在List中返回
	 */
	public static List<Object[]> batchExcuteQueryS(String sql, List<Object[]> list)  {
		List<Object[]> resultList = new ArrayList<Object[]>();
		try {
			for(Object[] params : list) {
				ResultSet rs = executeQueryRS(sql, params);
				while (rs.next()) {
					ResultSetMetaData rsmd = rs.getMetaData();
					int columnCount = rsmd.getColumnCount();

					Object[] rowValue = new Object[columnCount];
					for (int i = 0; i < rowValue.length; i++) {
						rowValue[i] = rs.getObject(i + 1);
					}
					resultList.add(rowValue);
				}
			}			
		} catch (SQLException e) {
			// 发生SQL异常把connection放进池子里面
			e.printStackTrace();
		} finally {
			closeAll();
		}
		return resultList;
	}
	


}
