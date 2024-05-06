package dbConn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DbConn {
	private static Connection conn = null;
	public static Connection getConn () {
		/**************************************************************************************************************
		 * Instantiate and initialize data source for JDBC data base access
		 *************************************************************************************************************/
		if (conn == null) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/yamindb", "student", "Java#1Rules");				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return conn;
	}
	public static void close() {
		if (conn != null) {
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
