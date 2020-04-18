package sender.com.vn.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sender.com.vn.common.Db;
import sender.com.vn.common.Define;
import sender.com.vn.model.User;

public class UserService {
	
	public Statement stmt;
	public Connection con;
	
	public UserService(Db db) {
		this.con=db.getConnection();
		this.stmt=db.getStmt();
	}
	
	public User getById(Integer id) {
		try {
			String sql ="SELECT * FROM users WHERE id ='"+id+"'";
			ResultSet rs= this.stmt.executeQuery(sql);
			
			User user = new User();
			if(rs.next()) {
				user.setId(rs.getInt("id"));
				user.setEmail(rs.getString("email"));
				user.setBalance(rs.getInt("balance"));
				user.setFirstname(rs.getString("firstname"));
			}
//			this.close();
			return user;
		} catch (SQLException e) {
			e.printStackTrace();
//			this.close();
		}
		
		return null;
	}

	public void getBackAmount(Integer userId, Integer remainAmount) {
		try {
			String query = "UPDATE users SET balance=balance+? WHERE id = ?";
		    
			PreparedStatement preparedStmt;
			preparedStmt = this.con.prepareStatement(query);
			preparedStmt.setInt(1, remainAmount);
			preparedStmt.setInt(2, userId);
			preparedStmt.executeUpdate();
//			this.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
