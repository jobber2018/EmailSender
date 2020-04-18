package sender.com.vn.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.extern.slf4j.Slf4j;
import sender.com.vn.common.Common;
import sender.com.vn.common.Db;
import sender.com.vn.common.Define;
import sender.com.vn.model.Campaign;
import sender.com.vn.model.EmailContect;
import sender.com.vn.model.EmailTemplate;
import sender.com.vn.model.MailNotify;
import sender.com.vn.model.Node;
import sender.com.vn.model.Transaction;
import sender.com.vn.model.User;

@Slf4j
public class SendMailNode extends Thread{
	private static final Logger logger = LogManager.getLogger(SendMailNode.class);
	
	private Thread t;
	private Campaign campaign;
	private Node node;
	private Db db;
	
    private String threadName ="sendMail";
    
    public SendMailNode (Campaign campaign, Node node, Db db) {
    	this.campaign = campaign;
    	this.node = node;
    	this.db=db;
    }
    
    @Override
    public void run() {
//        logger.info("Running thread send mail");
        try {
        	CampaignService campaignService = new CampaignService(db);
            NodeService nodeService = new NodeService(db);
            EmailTemplate emailTemplate = nodeService.getEmailTemplate(node.getEmail_template_id());
            
            ResultSet rs = null;
            if(node.getCategory().equals(Define.CAMPAIGN_NODE_EMAIL))
            	rs= campaignService.getAllEmail(campaign.getId());
            else if(node.getCategory().equals(Define.CAMPAIGN_NODE_EMAIL_READ))
            	rs= campaignService.getAllEmailRead(campaign.getId());
            else if(node.getCategory().equals(Define.CAMPAIGN_NODE_EMAIL_UNREAD))
            	rs= campaignService.getAllEmailUnRead(campaign.getId());
            
            int sentEmail = 0;
			while(rs.next()) {
				Integer campaignContactId = rs.getInt("id");
				AmazonSES amazonSES = new AmazonSES();
	            amazonSES.mailReply = campaign.getReply();
				amazonSES.FROMNAME = campaign.getBrand_name();
				amazonSES.mailSubject=emailTemplate.getSubject();
		    	
				EmailContect emailContect = new EmailContect();
				emailContect.setId(rs.getInt("id"));
				emailContect.setEmail(rs.getString("email"));
				emailContect.setFirstname(rs.getString("firstname"));
				emailContect.setLastname(rs.getString("lastname"));
				
				amazonSES.mailTo = emailContect.getEmail();
				
				String mailContent = emailTemplate.getContent();
				String url=Define.URL_EMAIL_LINK_CLICK+campaignContactId+"?uri=";
				mailContent = Common.validateEmailContent(mailContent,url);
		    	mailContent = mailContent + "<img src=\""+Define.URL_EMAIL_READ+campaignContactId+"\">";
		    	
		    	amazonSES.mailBody =amazonSES.buildMailBody(mailContent,emailContect);
		    	
		    	amazonSES.sendMail();
		    	sentEmail++;
		    	//logger.info("eMail "+ rs.getString("email"));
			}

			//Update actual send email number to node
			//nodeService.setActualSendEmailNumber(node.getId(),sentEmail);
			node.setActual_send_email_number(sentEmail);
			//Update node status = completed
			//nodeService.setNodeComplated(node.getId());
			node.setStatus(Define.CAMPAIGN_NODE_STATUS_COMPLETED);
			
			nodeService.update(node);
			
			//Update actual send email and actual cost for campaign
			campaignService.setActualCostAndSendMail(campaign.getId(),sentEmail);
			
			//check parent node = end
			Node parentNode = nodeService.getParent(node.getId());
			if(parentNode.getCategory().equals(Define.CAMPAIGN_NODE_END)) {
//				nodeService.setNodeComplated(parentNode.getId());
				parentNode.setStatus(Define.CAMPAIGN_NODE_STATUS_COMPLETED);
				nodeService.update(parentNode);
			}
			
			updateCampaignStatus(campaign.getId());
			
			/**
			 * Check campaign status
			 * if campaign status = completed =>
			 * 1. Get back amount to user  
			 * 2. Send mail notify cost to user
			 */
			//if(sentEmail==0) Thread.sleep(30000);//sleep 30s wait for update campaign status
			Campaign campaignAfterSendMail = campaignService.getById(campaign.getId());
//			logger.info("Campaign status {}",campaignAfterSendMail.getStatus());
			if(campaignAfterSendMail.getStatus().equals(Define.CAMPAIGN_STATUS_COMPLETED)) {
				
				//Get back amount
				UserService userService = new UserService(db);
				User user = userService.getById(campaignAfterSendMail.getCreated_by());
				
				Integer remainAmount = campaignAfterSendMail.getEstimate_cost()-campaignAfterSendMail.getActual_cost();
				if(remainAmount>0) {
					Transaction transaction = new Transaction();
					transaction.setCreated_by(1);
					transaction.setCredit_account(campaignAfterSendMail.getCreated_by());
					transaction.setDebit_account(1);
					transaction.setMoney(remainAmount);
					transaction.setRemark("Trả lại tiền chiến dịch: "+campaignAfterSendMail.getName());
					transaction.setTransaction_type(5);
					transaction.setBalance_before_update(user.getBalance());
					
					TransactionService transactionService=new TransactionService(db);
					transactionService.addTransaction(transaction);
					
					userService.getBackAmount(user.getId(),remainAmount);
				}else if(remainAmount<0) {
					Transaction transaction = new Transaction();
					transaction.setCreated_by(1);
					transaction.setCredit_account(1);
					transaction.setDebit_account(campaignAfterSendMail.getCreated_by());
					transaction.setMoney(remainAmount);
					transaction.setRemark("Thu thêm tiền chiến dịch: "+campaignAfterSendMail.getName());
					transaction.setTransaction_type(6);
					transaction.setBalance_before_update(user.getBalance());
					
					TransactionService transactionService=new TransactionService(db);
					transactionService.addTransaction(transaction);
					
					userService.getBackAmount(user.getId(),remainAmount);
				}
				
				//Send mail notify
				/*
				EmailTemplateService emailTemplateService=new EmailTemplateService();
				emailTemplate = emailTemplateService.getByKey("campaign-compleate");
				AmazonSES amazonSES = new AmazonSES();
				amazonSES.FROMNAME = Define.BRAND_NAME_DEFAULT;
				amazonSES.mailTo = campaignAfterSendMail.getEmail();
				amazonSES.mailBCC=Define.MAIL_BCC_DEFAULT;
				amazonSES.mailSubject=emailTemplate.getSubject();
				
				String mailContent = emailTemplate.getContent();
		    	
		    	HashMap<String, Object> scopes = new HashMap<String, Object>();
		        scopes.put("firstname", campaignAfterSendMail.getFirstname());
		        scopes.put("campaign_name", campaignAfterSendMail.getName());
		        scopes.put("est_email_number", campaignAfterSendMail.getEstimate_send_email_number());
		        scopes.put("est_cost", campaignAfterSendMail.getEstimate_cost());
		        scopes.put("actual_email_number", campaignAfterSendMail.getActual_send_email_number());
		        scopes.put("actual_cost", campaignAfterSendMail.getActual_cost());
		        scopes.put("remain_amount", remainAmount);
		        scopes.put("id", campaignAfterSendMail.getId());
		        
		        StringWriter writer = new StringWriter();
		        MustacheFactory mf = new DefaultMustacheFactory();
		        Mustache mustache = mf.compile(new StringReader(mailContent), "emailContent");
		        mustache.execute(writer, scopes);
		        mailContent= writer.toString();
		        
		    	amazonSES.mailBody =mailContent;
		    	amazonSES.sendMail();
		    	*/
				//mail notification active campaign
				MailNotify mailNotify = new MailNotify();
				EmailTemplateService emailTemplateService=new EmailTemplateService(db);
				emailTemplate = emailTemplateService.getByKey("campaign-completed");
				
				mailNotify.setMailTo(campaignAfterSendMail.getEmail());
				mailNotify.setMailCC(campaignAfterSendMail.getMail_cc());
				mailNotify.setMailBCC(Define.MAIL_BCC_DEFAULT);
				mailNotify.setBrandName(Define.BRAND_NAME_DEFAULT);
				mailNotify.setMailSubject(emailTemplate.getSubject());

				HashMap<String, Object> scopes = new HashMap<String, Object>();
		        scopes.put("firstname", campaignAfterSendMail.getFirstname());
		        scopes.put("campaign_name", campaignAfterSendMail.getName());
		        scopes.put("est_email_number", Common.formatMoney(campaignAfterSendMail.getEstimate_send_email_number()));
		        scopes.put("est_cost", Common.formatMoney(campaignAfterSendMail.getEstimate_cost()));
		        scopes.put("actual_email_number", Common.formatMoney(campaignAfterSendMail.getActual_send_email_number()));
		        scopes.put("actual_cost", Common.formatMoney(campaignAfterSendMail.getActual_cost()));
		        scopes.put("remain_amount", Common.formatMoney(remainAmount));
		        scopes.put("code", campaignAfterSendMail.getId());
		        scopes.put("start_date", campaignAfterSendMail.getRunning_date());
		        scopes.put("completed_date", campaignAfterSendMail.getCompleted_date());
		        
		        mailNotify.setScopes(scopes);
		        
		        String mailContent = emailTemplate.getContent();
		        mailNotify.setMailContent(mailContent);
		        
				SendMailNotify sendMail = new SendMailNotify(mailNotify);
				sendMail.start();
//		    	logger.info("Notify end campaign");
			}
			
		} catch (SQLException e) {
			logger.error("Error get email by group ID "+e.getMessage());
		} catch (Exception e) {
			logger.error("Send mail error "+e.getMessage());
		}
        
//        logger.info("End send mail");
    }
 
    public void start() {
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }
    
    private void updateCampaignStatus(Integer campaignId) {
		NodeService nodeService = new NodeService(db);
		List<Node> nodes = nodeService.getNodeQueueInProgress(campaignId);
		if(nodes.size() == 0 || nodes==null) {
			CampaignService campaignService =new CampaignService(db);
			campaignService.setCampaignCompleted(campaignId);
//			this.campaign.setStatus(Define.CAMPAIGN_STATUS_COMPLETED);
		}
	}
}
