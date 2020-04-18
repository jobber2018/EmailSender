package sender.com.vn.model;

public class CampaignContact {
	private Integer id;
	private Integer campaign_id;
	private Integer contact_id;
	private Integer open;
	private Integer click;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCampaign_id() {
		return campaign_id;
	}
	public void setCampaign_id(Integer campaign_id) {
		this.campaign_id = campaign_id;
	}
	public Integer getContact_id() {
		return contact_id;
	}
	public void setContact_id(Integer contact_id) {
		this.contact_id = contact_id;
	}
	public Integer getOpen() {
		return open;
	}
	public void setOpen(Integer open) {
		this.open = open;
	}
	public Integer getClick() {
		return click;
	}
	public void setClick(Integer click) {
		this.click = click;
	}
}
