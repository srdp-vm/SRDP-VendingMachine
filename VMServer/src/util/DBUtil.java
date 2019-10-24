package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {
	private static String drivername = null;
	private static String url = null;
	private static String username = null;
	private static String password = null;
	
	static {
		try {
			//读取配置文件，加载JDBC四大参数
			drivername = "com.mysql.cj.jdbc.Driver";
			url = "jdbc:mysql://localhost:3306/vendingmachine?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
			username = "root";
			password = "root";
			//加载驱动类
			Class.forName(drivername);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//建立连接
	public static Connection getConn() {
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
	//关闭连接
	public static void closeConn(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//关闭preparedStatement
	public static void closePstmt(PreparedStatement pstmt) {
		try {
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//关闭结果集ResultSet
	public static void closeRst(ResultSet rSet) {
		try {
			rSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
