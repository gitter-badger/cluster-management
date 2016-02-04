package org.noetl.pojos.notificationConfigs;

public class HipChatConf {
  private String RESTURI;
  private boolean NOTIFY;
  private String MESSAGE_COLOR;

  public String getRESTURI() {
    return RESTURI;
  }

  public void setRESTURI(String RESTURI) {
    this.RESTURI = RESTURI;
  }

  public boolean isNOTIFY() {
    return NOTIFY;
  }

  public void setNOTIFY(boolean NOTIFY) {
    this.NOTIFY = NOTIFY;
  }

  public String getMESSAGE_COLOR() {
    return MESSAGE_COLOR;
  }

  public void setMESSAGE_COLOR(String MESSAGE_COLOR) {
    this.MESSAGE_COLOR = MESSAGE_COLOR;
  }
}
