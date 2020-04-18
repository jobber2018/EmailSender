package sender.com.vn.model;

import java.util.Date;
import java.util.TimeZone;

public class QuartzTrigger {
	private String name;
    private String group;
    private String description;
    private String calendarName;
    private Date nextFireTime;
    private Date previousFireTime;
    private Date startTime;
    private Date endTime;
    private Date finalFireTime;
    private int priority;
    private int misfireInstruction;

    private String triggerType;

    private long repeatInterval;
    private int repeatCount;
    private long timesTriggered;

    private TimeZone timeZone;
    private String cronExpression;
    private String expressionSummary;
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
	public String getCalendarName() {
		return calendarName;
	}
	public void setCalendarName(String calendarName) {
		this.calendarName = calendarName;
	}
	public Date getNextFireTime() {
		return nextFireTime;
	}
	public void setNextFireTime(Date nextFireTime) {
		this.nextFireTime = nextFireTime;
	}
	public Date getPreviousFireTime() {
		return previousFireTime;
	}
	public void setPreviousFireTime(Date previousFireTime) {
		this.previousFireTime = previousFireTime;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getFinalFireTime() {
		return finalFireTime;
	}
	public void setFinalFireTime(Date finalFireTime) {
		this.finalFireTime = finalFireTime;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getMisfireInstruction() {
		return misfireInstruction;
	}
	public void setMisfireInstruction(int misfireInstruction) {
		this.misfireInstruction = misfireInstruction;
	}
	public String getTriggerType() {
		return triggerType;
	}
	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}
	public long getRepeatInterval() {
		return repeatInterval;
	}
	public void setRepeatInterval(long repeatInterval) {
		this.repeatInterval = repeatInterval;
	}
	public int getRepeatCount() {
		return repeatCount;
	}
	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}
	public long getTimesTriggered() {
		return timesTriggered;
	}
	public void setTimesTriggered(long timesTriggered) {
		this.timesTriggered = timesTriggered;
	}
	public TimeZone getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public String getExpressionSummary() {
		return expressionSummary;
	}
	public void setExpressionSummary(String expressionSummary) {
		this.expressionSummary = expressionSummary;
	}
}
