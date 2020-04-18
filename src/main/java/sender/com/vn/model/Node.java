package sender.com.vn.model;

public class Node {
	private Integer id;
	private String name;
	
	private Integer campaign_id;
	
	private Integer email_template_id;
	private Integer parent_id;
	
	private Integer wait_num;
	private String wait_op;
	
	private String wait_at;
	
	private String wait_date;
	
	private String running_date;
	
	private String status;
	
	private String category;
	
	private Integer actual_send_email_number;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getCampaign_id() {
		return campaign_id;
	}
	public void setCampaign_id(Integer campaign_id) {
		this.campaign_id = campaign_id;
	}
	public Integer getEmail_template_id() {
		return email_template_id;
	}
	public void setEmail_template_id(Integer email_template_id) {
		this.email_template_id = email_template_id;
	}
	public Integer getParent_id() {
		return parent_id;
	}
	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
	}
	public Integer getWait_num() {
		return wait_num;
	}
	public void setWait_num(Integer wait_num) {
		this.wait_num = wait_num;
	}
	public String getWait_op() {
		return wait_op;
	}
	public void setWait_op(String wait_op) {
		this.wait_op = wait_op;
	}
	public String getWait_at() {
		return wait_at;
	}
	public void setWait_at(String wait_at) {
		this.wait_at = wait_at;
	}
	public String getWait_date() {
		return wait_date;
	}
	public void setWait_date(String wait_date) {
		this.wait_date = wait_date;
	}
	
	public String getRunning_date() {
		return running_date;
	}
	public void setRunning_date(String running_date) {
		this.running_date = running_date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Integer getActual_send_email_number() {
		return actual_send_email_number;
	}
	public void setActual_send_email_number(Integer actual_send_email_number) {
		this.actual_send_email_number = actual_send_email_number;
	}
	
	
}
