package com.banmatrip.alert.domain;

public class TaskSchedulejob {
    private Integer id;

    private String jobName;

    private String jobStatus;

    private String cronExpression;

    private String concurrent;

    private String description;

    private String jobGroup;

    private String targetObject;

    private String targetMethod;

    private String isSpringBean;

    private String clazz;

    private String childJobs;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName == null ? null : jobName.trim();
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus == null ? null : jobStatus.trim();
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression == null ? null : cronExpression.trim();
    }

    public String getConcurrent() {
        return concurrent;
    }

    public void setConcurrent(String concurrent) {
        this.concurrent = concurrent == null ? null : concurrent.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup == null ? null : jobGroup.trim();
    }

    public String getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(String targetObject) {
        this.targetObject = targetObject == null ? null : targetObject.trim();
    }

    public String getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod == null ? null : targetMethod.trim();
    }

    public String getIsSpringBean() {
        return isSpringBean;
    }

    public void setIsSpringBean(String isSpringBean) {
        this.isSpringBean = isSpringBean == null ? null : isSpringBean.trim();
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz == null ? null : clazz.trim();
    }

    public String getChildJobs() {
        return childJobs;
    }

    public void setChildJobs(String childJobs) {
        this.childJobs = childJobs == null ? null : childJobs.trim();
    }
}