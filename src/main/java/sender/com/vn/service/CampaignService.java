package sender.com.vn.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sender.com.vn.common.Db;
import sender.com.vn.common.Define;
import sender.com.vn.model.Campaign;

public class CampaignService {
	
	public Statement stmt;
	public Connection con;
	
	public CampaignService(Db db) {
		this.con=db.getConnection();
		this.stmt=db.getStmt();
	}
	
	public Campaign getById(Integer id) {
		try {
			String sql ="SELECT c.*,u.firstname,u.lastname,u.email FROM campaign c "
					+ "INNER JOIN users u ON (c.created_by = u.id) "
					+ "WHERE c.id = "+id;
			ResultSet rs= this.stmt.executeQuery(sql);
			
			Campaign campaign = new Campaign();
			if(rs.next()) {
				campaign.setId(rs.getInt("id"));
				campaign.setName(rs.getString("name"));
				campaign.setContact_group_id(rs.getInt("contact_group_id"));
				campaign.setStatus(rs.getString("status"));
				campaign.setReply(rs.getString("reply"));
				campaign.setMail_cc(rs.getString("mail_cc"));
				campaign.setRunning_date(rs.getString("running_date"));
				campaign.setCompleted_date(rs.getString("completed_date"));
				campaign.setBrand_name(rs.getString("brand_name"));
				campaign.setCreated_by(rs.getInt("created_by"));
				campaign.setEmail(rs.getString("email"));
				campaign.setFirstname(rs.getString("firstname"));
				campaign.setLastname(rs.getString("lastname"));
				campaign.setCost(rs.getInt("cost"));
				campaign.setEstimate_send_email_number(rs.getInt("estimate_send_email_number"));
				campaign.setEstimate_cost(rs.getInt("estimate_cost"));
				campaign.setActual_cost(rs.getInt("actual_cost"));
				campaign.setActual_send_email_number(rs.getInt("actual_send_email_number"));
			}
			
//			this.close();
			return campaign;
		} catch (SQLException e) {
			e.printStackTrace();
//			this.close();
		}
		
		return null;
	}
	
	public Integer isCloneContact(Integer campaignId) {
		Integer count=0;
		try {
			String sql ="select count(*) as count_number from campaign_contact WHERE campaign_id = "+campaignId;
			ResultSet rs=this.stmt.executeQuery(sql);
			if(rs.next())
				count=rs.getInt("count_number");
			
//			this.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public boolean setCampaignRunning(Integer campaignId) {
		try {
			String query = "UPDATE campaign SET status=?, running_date = now() WHERE id = ?";
		    
			PreparedStatement preparedStmt;
			preparedStmt = this.con.prepareStatement(query);
			preparedStmt.setString(1, Define.CAMPAIGN_STATUS_IN_PROGRESS);
			preparedStmt.setInt(2, campaignId);
			preparedStmt.executeUpdate();
//			this.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean setCampaignCompleted(Integer campaignId) {
		try {
			String query = "UPDATE campaign SET status=?, completed_date = now() WHERE id = ?";
		    
			PreparedStatement preparedStmt;
			preparedStmt = this.con.prepareStatement(query);
			preparedStmt.setString(1, Define.CAMPAIGN_STATUS_COMPLETED);
			preparedStmt.setInt(2, campaignId);
			preparedStmt.executeUpdate();
//			this.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean cloneContact(Integer contactGroupId, Integer campaignId) {
		try {
			String query = "INSERT INTO campaign_contact (campaign_id, contact_id)\n" + 
					"SELECT ?, contact_id\n" + 
					"FROM contact_group_contact\n" + 
					"WHERE contact_group_id=?";
		    
			PreparedStatement preparedStmt;
			preparedStmt = this.con.prepareStatement(query);
			preparedStmt.setInt(1, campaignId);
			preparedStmt.setInt(2, contactGroupId);
			preparedStmt.executeUpdate();
//			this.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	/**
	 * 
	 * @param campaignId
	 * @return
	 */
	public ResultSet getAllEmail(Integer campaignId) {
		try {
			String sql ="SELECT cc.id,c.email,c.lastname,c.firstname,c.mobile from campaign_contact cc \n" + 
					"INNER JOIN contact c ON cc.contact_id = c.id\n" + 
					"WHERE cc.campaign_id = "+campaignId;
			
			ResultSet rs=this.stmt.executeQuery(sql);
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		this.close();
		return null;
	}
	/**
	 * 
	 * @param campaignId
	 * @return
	 */
	public ResultSet getAllEmailRead(Integer campaignId) {
		try {
			String sql ="SELECT c.id,c.email,c.lastname,c.firstname,c.mobile from campaign_contact cc \n" + 
					"INNER JOIN contact c ON cc.contact_id = c.id\n" + 
					"WHERE cc.open=1 AND cc.campaign_id = "+campaignId;
			
			ResultSet rs=this.stmt.executeQuery(sql);
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		this.close();
		return null;
	}
	/**
	 * 
	 * @param campaignId
	 * @return
	 */
	public ResultSet getAllEmailUnRead(Integer campaignId) {
		try {
			String sql ="SELECT c.id,c.email,c.lastname,c.firstname,c.mobile from campaign_contact cc \n" + 
					"INNER JOIN contact c ON cc.contact_id = c.id\n" + 
					"WHERE cc.open=0 AND cc.campaign_id = "+campaignId;
			
			ResultSet rs=this.stmt.executeQuery(sql);
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		this.close();
		return null;
	}

	public void setActualCostAndSendMail(Integer campaignId, int sentEmail) {
		try {
			String query = "UPDATE campaign SET actual_send_email_number=actual_send_email_number+?, actual_cost = actual_cost + cost*? WHERE id = ?";
		    
			PreparedStatement preparedStmt;
			preparedStmt = this.con.prepareStatement(query);
			preparedStmt.setInt(1, sentEmail);
			preparedStmt.setInt(2, sentEmail);
			preparedStmt.setInt(3, campaignId);
			preparedStmt.executeUpdate();
//			this.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
