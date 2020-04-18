package sender.com.vn.common;


public class Define {
	
	public static final String URL_EMAIL_READ = "https://sender.com.vn/read-email/";
	public static final String URL_EMAIL_LINK_CLICK = "https://sender.com.vn/email-link-click/";

	public static final String API_USER = "admin";
	public static final String API_PASS = "1@345678";
	
	public static String BRAND_NAME_DEFAULT = "SENDER";
	public static String MAIL_FROM_DEFAULT = "noreply@sender.com.vn";
	public static String MAIL_BCC_DEFAULT = "truonghm1980@gmail.com";
	
	public static String CAMPAIGN_NODE_TIMER = "timer";
	public static String CAMPAIGN_NODE_END = "end";
	public static String CAMPAIGN_NODE_START = "start";
	public static String CAMPAIGN_NODE_EMAIL = "email";
	public static String CAMPAIGN_NODE_EMAIL_READ = "emailread";
	public static String CAMPAIGN_NODE_EMAIL_UNREAD = "emailunread";
	public static String CAMPAIGN_NODE_GATEWAY = "gateway";
	
	//define email campaign status
	public static String CAMPAIGN_STATUS_INVALID ="Invalid";
	public static String CAMPAIGN_STATUS_VALID ="Valid";
	public static String CAMPAIGN_STATUS_IN_PROGRESS ="in-progress";
	public static String CAMPAIGN_STATUS_COMPLETED ="Completed";
	
	//define noste status
	public static String CAMPAIGN_NODE_VALID ="Valid";
	public static String CAMPAIGN_NODE_INVALID ="Invalid";
    public static String CAMPAIGN_NODE_STATUS_WAIT ="Wait";
    public static String CAMPAIGN_NODE_STATUS_QUEUE ="Queue";
    public static String CAMPAIGN_NODE_STATUS_COMPLETED ="Completed";
    public static String CAMPAIGN_NODE_STATUS_IN_PROGRESS ="in-progress";
}
