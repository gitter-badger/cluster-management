package org.noetl.pojos;

import org.noetl.pojos.clusterConfigs.ClusterConfJson;
import org.noetl.pojos.notificationConfigs.NotificationConf;
import org.noetl.pojos.monitorConfigs.MonitorConf;

public class AutomationConf {
  private String accessKey;
  private String secretAccessKey;
  private String rootPath;
  private String logFile;
  private NotificationConf notification;
  private MonitorConf monitorConf;
  private ClusterConfJson clusterConf;

  private Object OS_ENV;
  private Object PROJECT;
  private Object LOGGING;
  private Object WORKFLOW;

  public NotificationConf getNotification() {
    return notification;
  }

  public void setNotification(NotificationConf notification) {
    this.notification = notification;
  }

  public String getRootPath() {
    return rootPath;
  }

  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  public String getLogFile() {
    return logFile;
  }

  public void setLogFile(String logFile) {
    this.logFile = logFile;
  }

  public Object getOS_ENV() {
    return OS_ENV;
  }

  public void setOS_ENV(Object OS_ENV) {
    this.OS_ENV = OS_ENV;
  }

  public Object getPROJECT() {
    return PROJECT;
  }

  public void setPROJECT(Object PROJECT) {
    this.PROJECT = PROJECT;
  }

  public Object getLOGGING() {
    return LOGGING;
  }

  public void setLOGGING(Object LOGGING) {
    this.LOGGING = LOGGING;
  }

  public Object getWORKFLOW() {
    return WORKFLOW;
  }

  public void setWORKFLOW(Object WORKFLOW) {
    this.WORKFLOW = WORKFLOW;
  }

  public String getAccessKey() {
    return accessKey;
  }

  public void setAccessKey(String accessKey) {
    this.accessKey = accessKey;
  }

  public String getSecretAccessKey() {
    return secretAccessKey;
  }

  public void setSecretAccessKey(String secretAccessKey) {
    this.secretAccessKey = secretAccessKey;
  }


  public MonitorConf getMonitorConf() {
    return monitorConf;
  }

  public void setMonitorConf(MonitorConf monitorConf) {
    this.monitorConf = monitorConf;
  }

  public ClusterConfJson getClusterConf() {
    return clusterConf;
  }

  public void setClusterConf(ClusterConfJson clusterConf) {
    this.clusterConf = clusterConf;
  }
}
