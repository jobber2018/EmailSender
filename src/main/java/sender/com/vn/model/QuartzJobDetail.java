package sender.com.vn.model;

import java.util.List;

public class QuartzJobDetail {
	private String name;
    private String group;
    private String description;
    private Class jobClass;
    private boolean concurrentExectionDisallowed;  // misspelling is actually in Quartz object :)
    private boolean persistJobDataAfterExecution;
    private boolean durable;
    private boolean requestsRecovery;

    private List<QuartzTrigger> triggers;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Class getJobClass() {
		return jobClass;
	}

	public void setJobClass(Class jobClass) {
		this.jobClass = jobClass;
	}

	public boolean isConcurrentExectionDisallowed() {
		return concurrentExectionDisallowed;
	}

	public void setConcurrentExectionDisallowed(boolean concurrentExectionDisallowed) {
		this.concurrentExectionDisallowed = concurrentExectionDisallowed;
	}

	public boolean isPersistJobDataAfterExecution() {
		return persistJobDataAfterExecution;
	}

	public void setPersistJobDataAfterExecution(boolean persistJobDataAfterExecution) {
		this.persistJobDataAfterExecution = persistJobDataAfterExecution;
	}

	public boolean isDurable() {
		return durable;
	}

	public void setDurable(boolean durable) {
		this.durable = durable;
	}

	public boolean isRequestsRecovery() {
		return requestsRecovery;
	}

	public void setRequestsRecovery(boolean requestsRecovery) {
		this.requestsRecovery = requestsRecovery;
	}

	public List<QuartzTrigger> getTriggers() {
		return triggers;
	}

	public void setTriggers(List<QuartzTrigger> triggers) {
		this.triggers = triggers;
	}
}
