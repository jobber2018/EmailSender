package sender.com.vn.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sender.com.vn.common.Db;
import sender.com.vn.model.Contact;

public class ContactService {
	public Statement stmt;
	public Connection con;
	
	public ContactService(Db db) {
		this.con=db.getConnection();
		this.stmt=db.getStmt();
	}
	
	public Contact getById(Integer id) {
		try {
			String sql ="SELECT * FROM contact WHERE id ='"+id+"'";
			ResultSet rs= this.stmt.executeQuery(sql);
			
			Contact contact = new Contact();
			if(rs.next()) {
				contact.setId(rs.getInt("id"));
				contact.setFirstname(rs.getString("firstname"));
				contact.setLastname(rs.getString("lastname"));
				contact.setBirthday(rs.getString("birthday"));
				contact.setEmail(rs.getString("email"));
				contact.setVerify(rs.getInt("verify"));
			}
//			this.close();
			return contact;
		} catch (SQLException e) {
			e.printStackTrace();
//			this.close();
		}
		
		return null;
	}
	
	public Contact getByEmail(String email) {
		try {
			String sql ="SELECT * FROM contact WHERE email ='"+email+"'";
			ResultSet rs= this.stmt.executeQuery(sql);
			
			Contact contact = new Contact();
			if(rs.next()) 
				contact = setObj(rs);
			
//			this.close();
			return contact;
		} catch (SQLException e) {
			e.printStackTrace();
//			this.close();
		}
		
		return null;
	}
	
	private Contact setObj(ResultSet rs) throws SQLException {
		
		Contact contact = new Contact();
		contact.setId(rs.getInt("id"));
		contact.setFirstname(rs.getString("firstname"));
		contact.setLastname(rs.getString("lastname"));
		contact.setBirthday(rs.getString("birthday"));
		contact.setEmail(rs.getString("email"));
		contact.setVerify(rs.getInt("verify"));
		
		return contact;
	}
	
	public void update(Contact contact) {
		try {
			String query = "UPDATE contact SET firstname=?, lastname=?, birthday=?, mobile=?, email=?, verify=? "
					+ "WHERE id = ?";
		    
			PreparedStatement preparedStmt;
			preparedStmt = this.con.prepareStatement(query);
			preparedStmt.setString(1, contact.getFirstname());
			preparedStmt.setString(2, contact.getLastname());
			preparedStmt.setString(3, contact.getBirthday());
			preparedStmt.setString(4, contact.getMobile());
			preparedStmt.setString(5, contact.getEmail());
			preparedStmt.setInt(6, contact.getVerify());
			preparedStmt.setInt(7, contact.getId());
			preparedStmt.executeUpdate();
//			this.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insert(Contact contact) {
		try {
			String query = "insert into `contact` ( `lastname`,`firstname`, `email`,`mobile`, `birthday`,`address`,`fullname`,`gender`,`aeg`,`verify`, `created_by`,`created_date`) "
					+ "values ( ?, ?, ?, ?,?, ?, ?, ?,?,'0', ?,NOW())";
		    
			PreparedStatement preparedStmt;
			preparedStmt = this.con.prepareStatement(query);
			preparedStmt.setString(1, contact.getLastname());
			preparedStmt.setString(2, contact.getFirstname());
			preparedStmt.setString(3, contact.getEmail());
			preparedStmt.setString(4, contact.getMobile());
			preparedStmt.setString(5, contact.getBirthday());
			preparedStmt.setString(6, contact.getAddress());
			preparedStmt.setString(7, contact.getFullname());
			preparedStmt.setString(8, contact.getGender());
			preparedStmt.setString(9, contact.getAeg());
			preparedStmt.setInt(10, contact.getCreated_by());
			preparedStmt.executeUpdate();
//			this.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertContactGroup(Integer contact_id, Integer contact_group_id) {
		try {
			
			String sql ="SELECT * FROM contact_group_contact WHERE contact_id ="+contact_id+" and contact_group_id="+contact_group_id;
			ResultSet rs= this.stmt.executeQuery(sql);
			int size=0;
			
			if(rs.next())
				size++;
			
			if(size==0) {
				String query = "insert into `contact_group_contact` ( `contact_id`,`contact_group_id`) "
						+ "values ( ?, ?)";
			    
				PreparedStatement preparedStmt;
				preparedStmt = this.con.prepareStatement(query);
				preparedStmt.setInt(1, contact_id);
				preparedStmt.setInt(2, contact_group_id);
				preparedStmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
