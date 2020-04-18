package sender.com.vn.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sender.com.vn.common.Db;
import sender.com.vn.model.EmailTemplate;

public class EmailTemplateService {
	
	public Statement stmt;
	public Connection con;
	
	public EmailTemplateService(Db db) {
		this.con=db.getConnection();
		this.stmt=db.getStmt();
	}
	public EmailTemplate getByKey(String key) {
		try {
			String sql ="SELECT * FROM system_email_template WHERE `key` ='"+key+"'";
			ResultSet rs= this.stmt.executeQuery(sql);
			
			EmailTemplate emailTemplate = new EmailTemplate();
			if(rs.next()) {
				emailTemplate.setId(rs.getInt("id"));
				emailTemplate.setSubject(rs.getString("subject"));
				emailTemplate.setContent(rs.getString("template"));
			}
//			this.close();
			return emailTemplate;
		} catch (SQLException e) {
			e.printStackTrace();
//			this.close();
		}
		
		return null;
	}
}
