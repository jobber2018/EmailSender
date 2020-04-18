package sender.com.vn.jobs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.extern.slf4j.Slf4j;
import sender.com.vn.common.Db;
import sender.com.vn.model.CampaignContact;
import sender.com.vn.model.CampaignContactHistory;
import sender.com.vn.model.Contact;
import sender.com.vn.service.CampaignContactService;
import sender.com.vn.service.ContactService;

@Slf4j
public class EmailLinkClick extends Thread{
	private static final Logger logger = LogManager.getLogger(EmailLinkClick.class);
	
	private Thread t;
	private Integer campaignContactId;
	private String uri;
	
    private String threadName ="emailLinkClick";
    private Db db = new Db();
    
    public EmailLinkClick (Integer campaignContactId,String uri) {
    	this.campaignContactId = campaignContactId;
    	this.uri = uri;
    }
    
    @Override
    public void run() {
        try {
        	CampaignContactService campaignContactService = new CampaignContactService(db);
			CampaignContact campaignContact = campaignContactService.getById(campaignContactId);
			campaignContact.setClick(campaignContact.getClick() + 1);
			campaignContactService.update(campaignContact);
			
			CampaignContactHistory campaignContactHistory = new CampaignContactHistory();
			campaignContactHistory.setAction("Click");
			campaignContactHistory.setRemark(uri);
			campaignContactHistory.setCampaign_contact_id(campaignContact.getId());
			campaignContactService.addHistory(campaignContactHistory);
			
			ContactService contactService = new ContactService(db);
			Contact contact = contactService.getById(campaignContact.getContact_id());
			contact.setVerify(contact.getVerify()+1);
			contactService.update(contact);
			
		} catch (Exception e) {
			logger.error("Analyze open email error "+e.getMessage());
		}
    }
 
    public void start() {
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }
}
