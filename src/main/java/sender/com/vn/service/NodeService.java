package sender.com.vn.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import sender.com.vn.common.Db;
import sender.com.vn.common.Define;
import sender.com.vn.model.EmailTemplate;
import sender.com.vn.model.Node;

public class NodeService {
	
	public Statement stmt;
	public Connection con;
	
	public NodeService(Db db) {
		this.con=db.getConnection();
		this.stmt=db.getStmt();
	}
	
	public List<Node> getParents(Integer id) {
		try {
			String sql ="SELECT cn.* FROM campaign_node cn WHERE cn.parent_id = "+id;
			ResultSet rs=this.stmt.executeQuery(sql);
			List<Node> nodes = new ArrayList<Node>();
			while(rs.next()) {
				Node node = new Node();
				node.setId(rs.getInt("id"));
				node.setCampaign_id(rs.getInt("campaign_id"));
				node.setParent_id(rs.getInt("parent_id"));
				node.setEmail_template_id(rs.getInt("email_template_id"));
				node.setWait_num(rs.getInt("wait_num"));
				node.setWait_at(rs.getString("wait_at"));
				node.setWait_date(rs.getString("wait_date"));
				node.setWait_op(rs.getString("wait_op"));
				node.setName(rs.getString("name"));
				node.setCategory(rs.getString("category"));
				node.setStatus(rs.getString("status"));
				nodes.add(node);
			}
//			this.close();
			return nodes;
		} catch (SQLException e) {
			e.printStackTrace();
//			this.close();
		}
		
		return null;
	}
	
	public Node getById(Integer id) {
		try {
			String sql ="SELECT cn.* FROM campaign_node cn WHERE cn.id = "+id;
			ResultSet rs= this.stmt.executeQuery(sql);
			
			Node node = new Node();
			if(rs.next()) {
				node = setNodeObject(rs);
			}
			
//			this.close();
			return node;
		} catch (SQLException e) {
			e.printStackTrace();
//			this.close();
		}
		
		return null;
	}
	
	public Node getNodeStart(Integer campaignId) {
		try {
			String sql ="SELECT cn.* FROM campaign_node cn WHERE cn.campaign_id = "+campaignId + " AND parent_id=0";
			ResultSet rs= this.stmt.executeQuery(sql);
			
			Node node = new Node();
			if(rs.next()) 
				node = setNodeObject(rs);
			
//			this.close();
			return node;
		} catch (SQLException e) {
			e.printStackTrace();
//			this.close();
		}
		
		return null;
	}
	
	public Node getParent(Integer id) {
		try {
			String sql ="SELECT cn.* FROM campaign_node cn WHERE cn.parent_id = "+id;
			ResultSet rs= this.stmt.executeQuery(sql);
			
			Node node = new Node();
			if(rs.next()) {
				node = setNodeObject(rs);
			}
			
//			this.close();
			return node;
		} catch (SQLException e) {
			e.printStackTrace();
//			this.close();
		}
		
		return null;
	}
	
	public EmailTemplate getEmailTemplate(Integer emailTemplateId) {
		try {
			String sql ="SELECT et.* FROM email_template et WHERE et.id = "+emailTemplateId;
			ResultSet rs= this.stmt.executeQuery(sql);
			
			EmailTemplate emailTemplate = new EmailTemplate();
			if(rs.next()) {
				emailTemplate.setId(rs.getInt("id"));
				emailTemplate.setSubject(rs.getString("subject"));
				emailTemplate.setContent(rs.getString("content"));
			}
			
//			this.close();
			return emailTemplate;
		} catch (SQLException e) {
			e.printStackTrace();
//			this.close();
		}
		
		return null;
	}
	
	public boolean setNodeComplated(Integer nodeId) {
		try {
			String query = "UPDATE campaign_node SET status=? WHERE id = ?";
		    
			PreparedStatement preparedStmt;
			preparedStmt = this.con.prepareStatement(query);
			preparedStmt.setString(1, Define.CAMPAIGN_NODE_STATUS_COMPLETED);
			preparedStmt.setInt(2, nodeId);
			preparedStmt.executeUpdate();
//			this.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean setNodeInProgress(Integer nodeId) {
		try {
			String query = "UPDATE campaign_node SET status=? WHERE id = ?";
		    
			PreparedStatement preparedStmt;
			preparedStmt = this.con.prepareStatement(query);
			preparedStmt.setString(1, Define.CAMPAIGN_NODE_STATUS_IN_PROGRESS);
			preparedStmt.setInt(2, nodeId);
			preparedStmt.executeUpdate();
//			this.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean setNodeQueue(Integer nodeId, String runningDate) {
		try {
			String query = "UPDATE campaign_node SET status=?, running_date = ? WHERE id = ?";
		    
			PreparedStatement preparedStmt;
			preparedStmt = this.con.prepareStatement(query);
			preparedStmt.setString(1, Define.CAMPAIGN_NODE_STATUS_QUEUE);
			preparedStmt.setString(2, runningDate);
			preparedStmt.setInt(3, nodeId);
			preparedStmt.executeUpdate();
//			this.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private Node setNodeObject(ResultSet rs) {
		Node node = new Node();
		try {
			node.setId(rs.getInt("id"));
			node.setCampaign_id(rs.getInt("campaign_id"));
			node.setParent_id(rs.getInt("parent_id"));
			node.setEmail_template_id(rs.getInt("email_template_id"));
			node.setWait_num(rs.getInt("wait_num"));
			node.setWait_at(rs.getString("wait_at"));
			node.setWait_date(rs.getString("wait_date"));
			node.setWait_op(rs.getString("wait_op"));
			node.setName(rs.getString("name"));
			node.setCategory(rs.getString("category"));
			node.setStatus(rs.getString("status"));
			node.setRunning_date(rs.getString("running_date"));
			node.setActual_send_email_number(rs.getInt("actual_send_email_number"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return node;
	}

	public List<Node> getNodeQueueInProgress(Integer campaignId) {
		try {
			String sql ="SELECT cn.* FROM campaign_node cn WHERE (cn.status = '"+Define.CAMPAIGN_NODE_STATUS_QUEUE + "' OR cn.status = '"+Define.CAMPAIGN_NODE_STATUS_IN_PROGRESS + "') AND campaign_id="+campaignId;
			ResultSet rs= this.stmt.executeQuery(sql);
			
			List<Node> nodes = new ArrayList<Node>();
			while(rs.next()) {
				Node node = new Node();
				node = setNodeObject(rs);
				nodes.add(node);
			}
//			this.close();
			return nodes;
		} catch (SQLException e) {
			e.printStackTrace();
//			this.close();
		}
		
		return null;
		
	}

	public void setActualSendEmailNumber(Integer nodeId, int num) {
		// 
		try {
			String query = "UPDATE campaign_node SET actual_send_email_number=? WHERE id = ?";
		    
			PreparedStatement preparedStmt;
			preparedStmt = this.con.prepareStatement(query);
			preparedStmt.setInt(1, num);
			preparedStmt.setInt(2, nodeId);
			preparedStmt.executeUpdate();
//			this.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setStatusWait(Integer campaignId) {
		try {
			String query = "UPDATE campaign_node SET status=? WHERE campaign_id = ?";
		    
			PreparedStatement preparedStmt;
			preparedStmt = this.con.prepareStatement(query);
			preparedStmt.setString(1, Define.CAMPAIGN_NODE_STATUS_WAIT);
			preparedStmt.setInt(2, campaignId);
			preparedStmt.executeUpdate();
//			this.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public void update(Node node) {
		try {
			String query = "UPDATE campaign_node SET status=?, actual_send_email_number=?, running_date = ? "
					+ "WHERE id = ?";
		    
			PreparedStatement preparedStmt;
			preparedStmt = this.con.prepareStatement(query);
			preparedStmt.setString(1, node.getStatus());
			preparedStmt.setInt(2, node.getActual_send_email_number());
			preparedStmt.setString(3, node.getRunning_date());
			preparedStmt.setInt(4, node.getId());
			preparedStmt.executeUpdate();
//			this.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 
	 * @return
	 */
	public List<Node> getNodePending() {
		try {
			String sql ="SELECT cn.* FROM campaign_node cn WHERE cn.status = '"+Define.CAMPAIGN_NODE_STATUS_QUEUE + "' OR cn.status = '"+Define.CAMPAIGN_NODE_STATUS_IN_PROGRESS +"'";
			ResultSet rs= this.stmt.executeQuery(sql);
			
			List<Node> nodes = new ArrayList<Node>();
			while(rs.next()) {
				Node node = new Node();
				node = setNodeObject(rs);
				nodes.add(node);
			}
//			this.close();
			return nodes;
		} catch (SQLException e) {
			e.printStackTrace();
//			this.close();
		}
		
		return null;
		
	}
	
}
