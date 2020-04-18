package sender.com.vn.model;

public class Contact {
	
	private Integer id;
	private String lastname;
	private String firstname;
	private String birthday;
	private String mobile;
	private String email;
	private String address;
	private String fullname;
	private String gender;
	private String aeg;
	private Integer verify;
	private Integer created_by;
	
	private boolean error;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getVerify() {
		return verify;
	}
	public void setVerify(Integer verify) {
		this.verify = verify;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAeg() {
		return aeg;
	}
	public void setAeg(String aeg) {
		this.aeg = aeg;
	}
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	
	public Integer getCreated_by() {
		return created_by;
	}
	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}
	@Override
	public String toString() {
		return "Email: "+ this.email
				+ " - Error: "+ this.error;
				
	}
	
}
