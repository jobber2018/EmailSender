package sender.com.vn.model;

import java.util.HashMap;

import sender.com.vn.common.Define;

public class MailNotify {

	private String mailTo;
	private String mailCC;
	private String mailSubject;
	private String firstname;
	private String lastname;
	private String brandName;
	private String mailFrom;
	private String mailBCC;
	private String mailContent;
	HashMap<String, Object> scopes = new HashMap<String, Object>();
	
	public String getMailTo() {
		return mailTo;
	}
	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}
	public String getMailCC() {
		return mailCC;
	}
	public void setMailCC(String mailCC) {
		this.mailCC = mailCC;
	}
	public String getMailSubject() {
		return mailSubject;
	}
	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
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
	public String getBrandName() {
		if(this.brandName==null)
			return this.brandName = Define.BRAND_NAME_DEFAULT;
		return brandName;
	}
	public void setBrandName(String brandName) {	
		this.brandName = brandName;
	}
	public String getMailFrom() {
		if(this.mailFrom.isEmpty())
			return this.mailFrom = Define.MAIL_FROM_DEFAULT;
		return mailFrom;
	}
	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}
	public String getMailBCC() {
		if(this.mailBCC==null)
			return this.mailBCC = Define.MAIL_BCC_DEFAULT;
		return mailBCC;
	}
	public void setMailBCC(String mailBCC) {
		this.mailBCC = mailBCC;
	}
	public String getMailContent() {
		return mailContent;
	}
	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}
	public HashMap<String, Object> getScopes() {
		return scopes;
	}
	public void setScopes(HashMap<String, Object> scopes) {
		this.scopes = scopes;
	}
	
}
