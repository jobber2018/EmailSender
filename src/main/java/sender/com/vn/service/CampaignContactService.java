package sender.com.vn.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sender.com.vn.common.Db;
import sender.com.vn.model.CampaignContact;
import sender.com.vn.model.CampaignContactHistory;

public class CampaignContactService {
	
	public Statement stmt;
	public Connection con;
	
	public CampaignContactService(Db db) {
		this.con=db.getConnection();
		this.stmt=db.getStmt();
	}
	
	public CampaignContact getById(Integer id) {
		try {
			String sql ="SELECT * FROM campaign_contact WHERE id ='"+id+"'";
			ResultSet rs= this.stmt.executeQuery(sql);
			
			CampaignContact campaignContact = new CampaignContact();
			if(rs.next()) {
				campaignContact.setId(rs.getInt("id"));
				campaignContact.setCampaign_id(rs.getInt("campaign_id"));
				campaignContact.setContact_id(rs.getInt("contact_id"));
				campaignContact.setOpen(rs.getInt("open"));
				campaignContact.setClick(rs.getInt("click"));
			}
//			this.close();
			return campaignContact;
		} catch (SQLException e) {
			e.printStackTrace();
//			this.close();
		}
		
		return null;
	}

	public void update(CampaignContact campaignContact) {
		try {
			String query = "UPDATE campaign_contact SET open=?, click=? "
					+ "WHERE id = ?";
		    
			PreparedStatement preparedStmt;
			preparedStmt = this.con.prepareStatement(query);
			preparedStmt.setInt(1, campaignContact.getOpen());
			preparedStmt.setInt(2, campaignContact.getClick());
			preparedStmt.setInt(3, campaignContact.getId());
			preparedStmt.executeUpdate();
//			this.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addHistory(CampaignContactHistory campaignContactHistory) {
		try {
			String query = "INSERT INTO campaign_contact_history "
					+ "( `created_date`, `remark`, `campaign_contact_id`, `action`) "
					+ "values ( NOW(), ?, ?, 'Open')";
		    
			PreparedStatement preparedStmt;
			preparedStmt = this.con.prepareStatement(query);
			preparedStmt.setString(1, campaignContactHistory.getRemark());
			preparedStmt.setInt(2, campaignContactHistory.getCampaign_contact_id());
			preparedStmt.executeUpdate();
//			this.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
