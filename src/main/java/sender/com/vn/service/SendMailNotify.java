package sender.com.vn.service;


import java.io.StringReader;
import java.io.StringWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import lombok.extern.slf4j.Slf4j;
import sender.com.vn.model.MailNotify;

@Slf4j
public class SendMailNotify extends Thread{
	private static final Logger logger = LogManager.getLogger(SendMailNotify.class);
	private Thread t;
	private MailNotify mailNotify;
	
    private String threadName ="sendMail";
    
    public SendMailNotify (MailNotify mailNotify) {
    	this.mailNotify = mailNotify;
    }
    
    @Override
    public void run() {
    	try {
    		AmazonSES amazonSES = new AmazonSES();
    		
    		amazonSES.mailTo = this.mailNotify.getMailTo();
        	amazonSES.mailSubject=this.mailNotify.getMailSubject();
        	amazonSES.FROMNAME = this.mailNotify.getBrandName();
        	amazonSES.mailBCC = this.mailNotify.getMailBCC();
        	
        	StringWriter writer = new StringWriter();
	        MustacheFactory mf = new DefaultMustacheFactory();
	        Mustache mustache = mf.compile(new StringReader(this.mailNotify.getMailContent()), "emailContent");
	        mustache.execute(writer, this.mailNotify.getScopes());
	        
        	amazonSES.mailBody =writer.toString();
			amazonSES.sendMail();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
 
    public void start() {
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }
}
