package sender.com.vn.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import lombok.extern.slf4j.Slf4j;
import sender.com.vn.controller.JobController;
import sender.com.vn.jobs.SenderJob;
import sender.com.vn.model.Node;
import sender.com.vn.service.NodeService;

@Slf4j
public class Common {
	private static final Logger logger = LogManager.getLogger(Common.class);
	
	public static boolean createJob(Node timerNode, Scheduler scheduler, Db db) throws SchedulerException {
		
		String runDate = Common.getNextTime(timerNode.getWait_op(), timerNode.getWait_num(), timerNode.getWait_at(), timerNode.getWait_date());	
		String cronSchedule = Common.getCronSchedule(runDate);
		
		NodeService nodeService = new NodeService(db);
		
		Node taskNode = nodeService.getParent(timerNode.getId());
		
		Integer nodeId,campaignId;
		nodeId= taskNode.getId();
		campaignId = taskNode.getCampaign_id();
		
		String jobName="jobName_" + nodeId;
		String jobGroup="jobGroup_" + campaignId;
		
		JobKey jobKey;
		jobKey = new JobKey(jobName, jobGroup);
		
		Trigger trigger = TriggerBuilder
				.newTrigger() 
				.withIdentity("trigger_"+nodeId,"group_"+campaignId) 
				.withSchedule( 
						CronScheduleBuilder.cronSchedule(cronSchedule)
						)
				.build();
		 
		 JobDetail job = JobBuilder.newJob(SenderJob.class) 
				 .withIdentity(jobKey)
				 .build();
		 
		 scheduler.getContext().put(jobName, taskNode);
		 
		 scheduler.start();
		 scheduler.scheduleJob(job, trigger); 
		 
		 //update status note time = compleated
		 nodeService.setNodeComplated(timerNode.getId());
		 //update status node task = queue
		 nodeService.setNodeQueue(taskNode.getId(), runDate);
		 
		 //logger.info("Created job Parent/Id: {}/{} - starting @ {}",taskNode.getParent_id(),taskNode.getId(),cronSchedule);
		return true;
	}
	
 	public static String getNextTime(String wait_op, Integer p_waitNum,String wait_at,String wait_date) {
		
		Date afterDate=null;
		
		int waitNum =0;
		if(p_waitNum!=null) 
			waitNum = p_waitNum;
		
		switch(wait_op) {
		  case "minutes":
			  Calendar cal=Calendar.getInstance();
			  long t= cal.getTimeInMillis();
			  afterDate=new Date(t + (waitNum * 60000));//60000 = millisecs
		    break;
		  case "day":
			  afterDate = addDay(waitNum);
			  if(wait_at!=null && !wait_at.isEmpty()) {
				  afterDate=addTime(afterDate,wait_at);
			  }
		    break;
		  case "hour":
			  afterDate = addHour(waitNum);
			  break;
		  case "week":
			  afterDate = addWeek(waitNum);
			  if(wait_at!=null && !wait_at.isEmpty()) {
				  afterDate=addTime(afterDate,wait_at);
			  }
			  break;
		  case "month":
			  afterDate= addMonth(waitNum);
			  if(wait_at!=null && !wait_at.isEmpty()) {
				  afterDate=addTime(afterDate,wait_at);
			  }
			  break;
			  
		  default:
			  try {
				afterDate = new SimpleDateFormat("yyyy-MM-dd").parse(wait_date);
				if(wait_at!=null && !wait_at.isEmpty()) {
					  afterDate=addTime(afterDate,wait_at);
				  }else {
					  LocalTime time = LocalTime.now();
					  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
					  afterDate=addTime(afterDate,time.format(formatter));
				  }
			} catch (ParseException e) {
				e.printStackTrace();
			}
			  
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");  
        String strDate = dateFormat.format(afterDate); 
        
		return strDate.toString();
	}
 	
 	public static boolean isEmail(String email) {
 	      String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
 	      return email.matches(regex);
 	   }
 	
	public static String getCronSchedule(String dateTime) {
		
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date;
			date = formatter.parse(dateTime);
			LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			
			String cronSchedule =date.getSeconds()+" "
					+date.getMinutes()+" "
					+date.getHours()+" "
					+localDate.getDayOfMonth()+" "
					+localDate.getMonthValue() + " ? "
					+localDate.getYear();
			return cronSchedule;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String formatMoney(Integer number) {
		try {
			Locale localeEN = new Locale("en", "EN");
		    NumberFormat en = NumberFormat.getInstance(localeEN);
		    return en.format(number);
		}catch(NullPointerException e){
			return "0";
		}
	}
	
	public static String validateEmailContent(String emailContent,String newLink) {
		Document doc = Jsoup.parse(emailContent, "UTF-8");
	    Elements links = doc.select("a[href]");
	    
	    for(int linkIndexTopToBottom = 0; linkIndexTopToBottom < links.size(); linkIndexTopToBottom++){
	        
	        try {
	        	Element link = links.get(linkIndexTopToBottom);
				newLink = newLink+URLEncoder.encode( link.attr("href").toString(), "UTF-8" );
				link.attr("href",newLink);
			} catch (UnsupportedEncodingException e) {
				
			}
	    }
	    return doc.toString();
	}
	private static Date addTime(Date date, String time) {
		DateFormat dateFormat = new SimpleDateFormat("H:mm:ss");
		Calendar cal = Calendar.getInstance();
	      try {
	    	  Date d = dateFormat.parse(time);
			  cal.setTime(date);
			  cal.set(Calendar.HOUR_OF_DAY,d.getHours());
			  cal.set(Calendar.MINUTE,d.getMinutes());
			  cal.set(Calendar.SECOND,d.getSeconds());
			  return cal.getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
	      return null;
	}
	private static Date addMonth(int i) {
		Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, i);
        return cal.getTime();
    }
	
	private static Date addHour(int i) {
		Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, i);
        return cal.getTime();
    }
	
	private static Date addWeek(int i) {
		Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, i*7);//day of week
        return cal.getTime();
    }
	
	private static Date addDay(int i) {
		Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, i);
        return cal.getTime();
    }
}
