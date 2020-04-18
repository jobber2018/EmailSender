package sender.com.vn.model;

public class Transaction {
	private Integer id;
	private Integer credit_account;
	private Integer debit_account;
	private Integer money;
	private String remark;
	private Integer created_by;
	private Integer transaction_type;
	private Integer balance_before_update;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCredit_account() {
		return credit_account;
	}
	public void setCredit_account(Integer credit_account) {
		this.credit_account = credit_account;
	}
	public Integer getDebit_account() {
		return debit_account;
	}
	public void setDebit_account(Integer debit_account) {
		this.debit_account = debit_account;
	}
	public Integer getMoney() {
		return money;
	}
	public void setMoney(Integer money) {
		this.money = money;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getCreated_by() {
		return created_by;
	}
	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}
	public Integer getTransaction_type() {
		return transaction_type;
	}
	public void setTransaction_type(Integer transaction_type) {
		this.transaction_type = transaction_type;
	}
	public Integer getBalance_before_update() {
		return balance_before_update;
	}
	public void setBalance_before_update(Integer balance_before_update) {
		this.balance_before_update = balance_before_update;
	}
}
