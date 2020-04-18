package sender.com.vn.service;

import static org.quartz.JobKey.jobKey;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import sender.com.vn.common.Common;
import sender.com.vn.common.Db;
import sender.com.vn.common.Define;
import sender.com.vn.jobs.EmailLinkClick;
import sender.com.vn.jobs.EmailRead;
import sender.com.vn.model.Campaign;
import sender.com.vn.model.CampaignContact;
import sender.com.vn.model.EmailTemplate;
import sender.com.vn.model.JobResult;
import sender.com.vn.model.MailNotify;
import sender.com.vn.model.Node;
import sender.com.vn.model.QuartzJobDetail;

//https://stackjava.com/uncategorized/cron-expression-la-gi-huong-dan-cu-phap-cron-expression.html

@Slf4j
@Service
public class JobService {

	private Scheduler scheduler;
	private Db db=new Db();
	
	private static final Logger logger = LogManager.getLogger(JobService.class);
    
	public JobService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }
	
	public JobResult runCampaign(Integer campaignId) throws SchedulerException {
		JobResult jobResult = new JobResult();
		jobResult.setStatus(1);
		try {
			
			CampaignService campaignService=new CampaignService(db);
			
			Campaign campaign = campaignService.getById(campaignId);
			
			if(campaign.getStatus().equals(Define.CAMPAIGN_STATUS_IN_PROGRESS) 
					|| campaign.getStatus().equals(Define.CAMPAIGN_STATUS_COMPLETED)) 
				throw new Exception("Could not run campaign, because campaign is "+campaign.getStatus());
			
			NodeService nodeService = new NodeService(db);
			//Update all node status = wait off campaign
			nodeService.setStatusWait(campaignId);
			
			//get node start
			Node nodeStart = nodeService.getNodeStart(campaignId);
			
			//get first node timer
			Node node= nodeService.getParent(nodeStart.getId());
			
//			Node node= nodeService.getById(campaignId);
			
			if(node.getCategory().equals(Define.CAMPAIGN_NODE_TIMER)) {
				
				logger.info("Run campaign ID: {}", campaignId);
				
				//only clone contact when count=0
				if(campaignService.isCloneContact(campaign.getId()) ==0)
					campaignService.cloneContact(campaign.getContact_group_id(),campaign.getId());
				
				//Create job for first task
				Common.createJob(node, scheduler,db);
				nodeService.setNodeComplated(nodeStart.getId());
				campaignService.setCampaignRunning(campaign.getId());
				
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Date date = new Date();
				campaign.setRunning_date(dateFormat.format(date));
				
				//mail notification active campaign
				MailNotify mailNotify = new MailNotify();
				EmailTemplateService emailTemplateService=new EmailTemplateService(db);
				EmailTemplate emailTemplate = emailTemplateService.getByKey("campaign-start");
				
				mailNotify.setMailTo(campaign.getEmail());
				mailNotify.setMailCC(campaign.getMail_cc());
				mailNotify.setMailBCC(Define.MAIL_BCC_DEFAULT);
				mailNotify.setBrandName(Define.BRAND_NAME_DEFAULT);
				mailNotify.setMailSubject(emailTemplate.getSubject());

		    	HashMap<String, Object> scopes = new HashMap<String, Object>();
		        scopes.put("firstname", campaign.getFirstname());
		        scopes.put("campaign_name", campaign.getName());
		        scopes.put("est_email_number", Common.formatMoney(campaign.getEstimate_send_email_number()));
		        scopes.put("est_cost", Common.formatMoney(campaign.getEstimate_cost()));
		        scopes.put("code", campaign.getId());
		        scopes.put("start_date", campaign.getRunning_date());
		        
		        mailNotify.setScopes(scopes);
		        
		        String mailContent = emailTemplate.getContent();
		        mailNotify.setMailContent(mailContent);
		        
				SendMailNotify sendMail = new SendMailNotify(mailNotify);
				sendMail.start();
			}else 
				throw new Exception("Cannot find node start");
			
		}catch(Exception e){
			jobResult.setMessage(e.getMessage());
			jobResult.setStatus(0);
			logger.error(e.getMessage());
		}

	    return jobResult;
	}
	
	
	public JobResult emailRead(Integer campaignContactId) throws SchedulerException {
		JobResult jobResult = new JobResult();
		jobResult.setStatus(1);
		try {
			
			EmailRead emailRead = new EmailRead(campaignContactId);
			emailRead.start();
			
		}catch(Exception e){
			jobResult.setMessage(e.getMessage());
			jobResult.setStatus(0);
		}

	    return jobResult;
	}
	
	public JobResult emailLinkClick(Integer campaignContactId, String uri) throws SchedulerException {
		JobResult jobResult = new JobResult();
		jobResult.setStatus(1);
		try {
			
			EmailLinkClick emailLinkClick = new EmailLinkClick(campaignContactId,uri);
			emailLinkClick.start();
			
		}catch(Exception e){
			jobResult.setMessage(e.getMessage());
			jobResult.setStatus(0);
		}

	    return jobResult;
	}

	public JobResult reRunNode() {
		
		JobResult jobResult = new JobResult();
		jobResult.setStatus(1);
		try {
			
			NodeService nodeService = new NodeService(db);
			List<Node> nodes = nodeService.getNodePending();
			Integer createdTask=0;
			Integer existTask=0;
			Integer totalTask=0;
			for(Node node : nodes) {
				try {
					Node timerNode = nodeService.getById(node.getParent_id());
					if(timerNode.getCategory().equals(Define.CAMPAIGN_NODE_TIMER)) {
						try {
							Common.createJob(timerNode, scheduler,db);
							createdTask++;
						}catch (SchedulerException e) {
							existTask++;
						}
					}
					
				}catch (NullPointerException e ) {}
				
				totalTask++;
			}
			jobResult.setMessage("Created new "+createdTask + " task, exits "+ existTask + " task/ Total: "+totalTask);
		}catch(Exception e){
			e.getStackTrace();
			jobResult.setMessage(e.getMessage());
			jobResult.setStatus(0);
		}

	    return jobResult;
	}
}
