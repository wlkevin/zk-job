package com.job.core.model;

public class Task {
	
	private String taskName;
	private String cronExpression;
	private int id;
	private int state;//1新增2修改3删除
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
