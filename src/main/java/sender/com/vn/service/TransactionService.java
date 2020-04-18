package sender.com.vn.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import sender.com.vn.common.Db;
import sender.com.vn.model.Transaction;

public class TransactionService{
	
	public Statement stmt;
	public Connection con;
	
	public TransactionService(Db db) {
		this.con=db.getConnection();
		this.stmt=db.getStmt();
	}
	
	public boolean addTransaction(Transaction transaction) {
		try {
			String query = "insert into `transaction` ( "
					+ "`created_by`"
					+ ", `remark`"
					+ ", `transaction_type`"
					+ ", `balance_before_update`"
					+ ", `debit_account`"
					+ ", `money`"
					+ ", `credit_account`"
					+ ",`created_date`) " + 
					"values ( "
					+ "?,?,?,?,?,?,?, NOW())";
		    
			PreparedStatement preparedStmt;
			preparedStmt = this.con.prepareStatement(query);
			preparedStmt.setInt(1, transaction.getCreated_by());
			preparedStmt.setString(2, transaction.getRemark());
			preparedStmt.setInt(3, transaction.getTransaction_type());
			preparedStmt.setInt(4, transaction.getBalance_before_update());
			preparedStmt.setInt(5, transaction.getDebit_account());
			preparedStmt.setInt(6, transaction.getMoney());
			preparedStmt.setInt(7, transaction.getCredit_account());
			preparedStmt.executeUpdate();
//			this.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
}
