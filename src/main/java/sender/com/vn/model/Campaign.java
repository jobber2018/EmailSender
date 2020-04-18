package sender.com.vn.model;

import sender.com.vn.common.Define;

public class Campaign {
	private Integer id;
	private String name;
	private Integer contact_group_id;
	private String status;
	private String reply;
	private String mail_cc;
	private String running_date;
	private String completed_date;
	private String brand_name;
	private Integer created_by;
	
	private Integer cost;
	private Integer estimate_send_email_number;
	private Integer estimate_cost;
	private Integer actual_cost;
	private Integer actual_send_email_number;
	
	private String email;
	private String firstname;
	private String lastname;
	
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
	public Integer getContact_group_id() {
		return contact_group_id;
	}
	public void setContact_group_id(Integer contact_group_id) {
		this.contact_group_id = contact_group_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	public String getMail_cc() {
		return mail_cc;
	}
	public void setMail_cc(String mail_cc) {
		this.mail_cc = mail_cc;
	}
	public String getRunning_date() {
		return running_date;
	}
	public void setRunning_date(String running_date) {
		this.running_date = running_date;
	}
	public String getCompleted_date() {
		return completed_date;
	}
	public void setCompleted_date(String completed_date) {
		this.completed_date = completed_date;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getBrand_name() {
		if(brand_name==null)
			return Define.BRAND_NAME_DEFAULT;
		return brand_name;
	}
	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}
	public Integer getCost() {
		return cost;
	}
	public void setCost(Integer cost) {
		this.cost = cost;
	}
	public Integer getEstimate_send_email_number() {
		return estimate_send_email_number;
	}
	public void setEstimate_send_email_number(Integer estimate_send_email_number) {
		this.estimate_send_email_number = estimate_send_email_number;
	}
	public Integer getEstimate_cost() {
		return estimate_cost;
	}
	public void setEstimate_cost(Integer estimate_cost) {
		this.estimate_cost = estimate_cost;
	}
	public Integer getActual_cost() {
		return actual_cost;
	}
	public void setActual_cost(Integer actual_cost) {
		this.actual_cost = actual_cost;
	}
	public Integer getActual_send_email_number() {
		return actual_send_email_number;
	}
	public void setActual_send_email_number(Integer actual_send_email_number) {
		this.actual_send_email_number = actual_send_email_number;
	}
	public Integer getCreated_by() {
		return created_by;
	}
	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}
	
}
