package sender.com.vn.model;

public class CampaignContactHistory {
	
	private Integer id;
	private Integer campaign_contact_id;
	private String action;
	private String remark;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCampaign_contact_id() {
		return campaign_contact_id;
	}
	public void setCampaign_contact_id(Integer campaign_contact_id) {
		this.campaign_contact_id = campaign_contact_id;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
