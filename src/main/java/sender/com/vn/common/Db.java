package sender.com.vn.common;

import java.sql.*;

public class Db {
	public Statement stmt;
	public Connection con;
	public Db() {
		try{
			Class.forName("com.mysql.jdbc.Driver");
//			localhot
			this.con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/emo?characterEncoding=utf-8","root","12345678");
//			Server
//			this.con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/sender_sender?characterEncoding=utf-8","sender_sender","5mMTQdeZ");
			this.stmt=con.createStatement();
//			StackTraceElement[] st = Thread.currentThread().getStackTrace();
//		    System.out.println(  "create connection called from " + st[2] );
		}catch(Exception e){ 
			System.out.println(e);
		}
	}
	public void close() {
//		try {
//			this.stmt.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		try {
//			this.con.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
	}
	
	public Connection getConnection() {
		return this.con;
	}
	public Statement getStmt() {
		return this.stmt;
	}
}
