package org.noetl.pojos.notificationConfigs;

public class HipChatConf {
  private String restURI;
  private boolean notify;
  private String messageColor;

  public String getRestURI() {
    return restURI;
  }

  public void setRestURI(String restURI) {
    this.restURI = restURI;
  }

  public boolean isNotify() {
    return notify;
  }

  public void setNotify(boolean notify) {
    this.notify = notify;
  }

  public String getMessageColor() {
    return messageColor;
  }

  public void setMessageColor(String messageColor) {
    this.messageColor = messageColor;
  }

  @Override
  public String toString() {
    return "HipChatConf{" +
      "restURI='" + restURI + '\'' +
      ", notify=" + notify +
      ", messageColor='" + messageColor + '\'' +
      '}';
  }
}
