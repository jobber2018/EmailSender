package sender.com.vn.service;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import lombok.extern.slf4j.Slf4j;
import sender.com.vn.model.EmailContect;

@Slf4j
public class AmazonSES {
	private static final Logger logger = LogManager.getLogger(AmazonSES.class);
	// Replace sender@example.com with your "From" address.
    // This address must be verified.
    public String FROM = "noreply@sender.com.vn";
    public String FROMNAME = "SENDER";
	
    // Replace recipient@example.com with a "To" address. If your account 
    // is still in the sandbox, this address must be verified.
//    static final String TO = "truonghm1980@gmail.com";
    
    // Replace smtp_username with your Amazon SES SMTP user name.
    static final String SMTP_USERNAME = "AKIAXSLECV4ENFQVSYC3";
    
    // Replace smtp_password with your Amazon SES SMTP password.
    static final String SMTP_PASSWORD = "BO1pr5etU4IDU7bsl5Rx3SP4w8MkniW9Zksvq8W/IATw";
    
    // The name of the Configuration Set to use for this message.
    // If you comment out or remove this variable, you will also need to
    // comment out or remove the header below.
    static final String CONFIGSET = "VNHOMESTAY_CONFIG_SET";
    
    // Amazon SES SMTP host name. This example uses the US West (Oregon) region.
    // See https://docs.aws.amazon.com/ses/latest/DeveloperGuide/regions.html#region-endpoints
    // for more information.
    static final String HOST = "email-smtp.us-east-1.amazonaws.com";
    
    // The port you will connect to on the Amazon SES SMTP endpoint. 
    static final int PORT = 587;
    
    public String mailSubject = "";
    public String mailTo ="";
    public String mailCC ="";//mail cach nhau boi dau ,
    public String mailBCC ="";
    public String mailBody ="";
    public String mailReply ="";
    

    public String buildMailBody(String content, EmailContect emailContect) {
    	
    	 HashMap<String, Object> scopes = new HashMap<String, Object>();
         scopes.put("firstname", emailContect.getFirstname());
         scopes.put("lastname", emailContect.getLastname());
         scopes.put("email", emailContect.getEmail());

//         Writer writer = new OutputStreamWriter(System.out);
         StringWriter writer = new StringWriter();
         MustacheFactory mf = new DefaultMustacheFactory();
         Mustache mustache = mf.compile(new StringReader(content), "emailContent");
         mustache.execute(writer, scopes);
    	return writer.toString();
    }
    
    public boolean sendMail() throws Exception {
    	
    	// Create a Properties object to contain connection configuration information.
    	Properties props = System.getProperties();
    	props.put("mail.transport.protocol", "smtp");
    	props.put("mail.smtp.port", PORT); 
    	props.put("mail.smtp.starttls.enable", "true");
    	props.put("mail.smtp.auth", "true");

        // Create a Session object to represent a mail session with the specified properties. 
    	Session session = Session.getDefaultInstance(props);
    	
    	// Create a message with the specified information. 
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(FROM,FROMNAME));
        
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(this.mailTo));
        msg.setSubject(this.mailSubject,"UTF-8");
        msg.setContent(this.mailBody,"text/html; charset=UTF-8");
        
        if(!this.mailReply.isEmpty()) {
	        InternetAddress[] replyTo = InternetAddress.parse(this.mailReply);
	        msg.setReplyTo(replyTo);
        }
        
        if(!this.mailCC.isEmpty()) {
        	InternetAddress[] myCcList = InternetAddress.parse(this.mailCC);
        	msg.addRecipients(Message.RecipientType.CC,myCcList);
        }
        
        if(!this.mailBCC.isEmpty()) {
        	InternetAddress[] myBccList = InternetAddress.parse(this.mailBCC);
        	msg.addRecipients(Message.RecipientType.BCC,myBccList);
        }
        	
        
        // Add a configuration set header. Comment or delete the 
        // next line if you are not using a configuration set
        msg.setHeader("X-SES-CONFIGURATION-SET", CONFIGSET);
            
        // Create a transport.
        Transport transport = session.getTransport();
    	
        
     // Send the message.
        try
        {
//            logger.info("Sending "+this.mailTo+"...");
            
            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
        	
            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());
            logger.info("Sent "+this.mailTo);
            return true;
        }
        catch (Exception ex) {
        	logger.error("The email was not sent."+this.mailTo);
        	logger.error("Error message: " + ex.getMessage());
        }
        finally
        {
            transport.close();
        }
        
    	return false;
    	
    }
    
    public static void main(String[] args) throws Exception {
    	
    	AmazonSES amazonSES = new AmazonSES();
    	amazonSES.mailTo = "truonghm1980@gmail.com";
    	amazonSES.mailSubject="Test mail form Job VNHOMESTAY";
    	amazonSES.mailCC="anhvu1203283@gmail.com,truonghm@thdgroup.vn";
    	amazonSES.mailBCC="jobber.vn@gmail.com";
    	
    	EmailContect email= new EmailContect();
    	email.setEmail("info@thdgroup.vn");
    	email.setLastname("Hoang Manh");
    	email.setFirstname("Truong");
    	
    	String mailBody = "<h1>Hello {{firstname}} {{lastname}}</h1> "
    			+ "<p>Đây là email {{emai}} được gửi bời dịch vụ <img src=\"https://vnhomestay.com.vn/email-campaign-step-mail-check/1/1.html\">"
    			;
    	
    	mailBody = amazonSES.buildMailBody(mailBody,email);
    	
    	amazonSES.mailBody =mailBody;
    	amazonSES.sendMail();
//    	
//    	logger.info("begin send mail");
//    	Integer emailGroup = 1;
    	
//    	logger.info("begin send mail");
//    	String mailBody = "<h1>Hello {{firstname}} {{lastname}}</h1> "
//    			+ "<p>This email {{email}} was sent with Amazon SES using the";
//    			
//    	ModelStep stepInfo= new ModelStep();
//    	stepInfo.setEmail_group_id(1);
//    	stepInfo.setEmail_content(mailBody);
//    	stepInfo.setEmail_subject("Test email job");
//    	
//    	SendMail sendMail = new SendMail(stepInfo);
//    	sendMail.start();
//    	logger.info("end send mail");
    	
    }
}
