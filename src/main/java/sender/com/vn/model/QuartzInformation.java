package sender.com.vn.model;

import java.util.Date;
import java.util.List;

public class QuartzInformation {

	private String version;
    private String schedulerName;
    private String instanceId;

    private Class threadPoolClass;
    private int numberOfThreads;

    private Class schedulerClass;
    private boolean isClustered;

    private Class jobStoreClass;
    private long numberOfJobsExecuted;

    private Date startTime;
    private boolean inStandbyMode;

    private List<String> simpleJobDetail;

    public String getSchedulerProductName() {
        return "Quartz Scheduler (spring-boot-starter-quartz)";
    }

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSchedulerName() {
		return schedulerName;
	}

	public void setSchedulerName(String schedulerName) {
		this.schedulerName = schedulerName;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public Class getThreadPoolClass() {
		return threadPoolClass;
	}

	public void setThreadPoolClass(Class threadPoolClass) {
		this.threadPoolClass = threadPoolClass;
	}

	public int getNumberOfThreads() {
		return numberOfThreads;
	}

	public void setNumberOfThreads(int numberOfThreads) {
		this.numberOfThreads = numberOfThreads;
	}

	public Class getSchedulerClass() {
		return schedulerClass;
	}

	public void setSchedulerClass(Class schedulerClass) {
		this.schedulerClass = schedulerClass;
	}

	public boolean isClustered() {
		return isClustered;
	}

	public void setClustered(boolean isClustered) {
		this.isClustered = isClustered;
	}

	public Class getJobStoreClass() {
		return jobStoreClass;
	}

	public void setJobStoreClass(Class jobStoreClass) {
		this.jobStoreClass = jobStoreClass;
	}

	public long getNumberOfJobsExecuted() {
		return numberOfJobsExecuted;
	}

	public void setNumberOfJobsExecuted(long numberOfJobsExecuted) {
		this.numberOfJobsExecuted = numberOfJobsExecuted;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public boolean isInStandbyMode() {
		return inStandbyMode;
	}

	public void setInStandbyMode(boolean inStandbyMode) {
		this.inStandbyMode = inStandbyMode;
	}

	public List<String> getSimpleJobDetail() {
		return simpleJobDetail;
	}

	public void setSimpleJobDetail(List<String> simpleJobDetail) {
		this.simpleJobDetail = simpleJobDetail;
	}
    
    
}
