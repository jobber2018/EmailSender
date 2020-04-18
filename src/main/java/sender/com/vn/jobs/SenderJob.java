package sender.com.vn.jobs;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import sender.com.vn.common.Common;
import sender.com.vn.common.Db;
import sender.com.vn.common.Define;
import sender.com.vn.model.Campaign;
import sender.com.vn.model.JobResult;
import sender.com.vn.model.MailNotify;
import sender.com.vn.model.Node;
import sender.com.vn.service.CampaignService;
import sender.com.vn.service.JobService;
import sender.com.vn.service.NodeService;
import sender.com.vn.service.SendMailNode;
import sender.com.vn.service.SendMailNotify;

@Slf4j
@Service
public class SenderJob implements Job{
	private static final Logger logger = LogManager.getLogger(SenderJob.class);
	private Db db=new Db();
	@Override
    public void execute(JobExecutionContext context) {
		String jobName = context.getJobDetail().getKey().getName();
		logger.info("Run job name ** {} ** starting @ {}", jobName, context.getFireTime());
		
		SchedulerContext schedulerContext;
		try {
			
			//get data current job (timer node)
			schedulerContext = context.getScheduler().getContext();
			Node taskNode = (Node) schedulerContext.get(jobName);
			
//			logger.info("Business taks ID/Parent: {}/{}",taskNode.getId(),taskNode.getParent_id());
			//Send mail in node task
			CampaignService campaignService =new CampaignService(db);
			Campaign campaign=campaignService.getById(taskNode.getCampaign_id());
			SendMailNode sendMailNode = new SendMailNode(campaign,taskNode,db);
			sendMailNode.start();
			
			NodeService nodeService = new NodeService(db);
			//nodeService.setNodeComplated(taskNode.getId());
			nodeService.setNodeInProgress(taskNode.getId());
			
			//get child node off current task node
			Node node = nodeService.getParent(taskNode.getId());
			try {
				if(node.getCategory().equals(Define.CAMPAIGN_NODE_TIMER)) {
					//create new job for node
					Common.createJob(node, context.getScheduler(),db);
				}else if(node.getCategory().equals(Define.CAMPAIGN_NODE_GATEWAY)) {
					List<Node> nodes = nodeService.getParents(node.getId());
					for (Node tmpNode : nodes) {
						if(tmpNode.getCategory().equals("timer")) 
							Common.createJob(tmpNode, context.getScheduler(),db);
					}
					nodeService.setNodeComplated(node.getId());
				}
			}catch(NullPointerException e) {
				//object null =>node is not valid or not has end node.
				//logger.info("==================Null Job name {}", jobName);
				//setCampaignStatus(campaign.getId());
				//nodeService.setNodeComplated(node.getId());
			}
			
		} catch (SchedulerException e) {
			// TODO log error
			logger.error("==================Err====", e);
//			e.printStackTrace();
		}
		
    }
}
