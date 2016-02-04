package org.noetl.pojos.notificationConfigs;

public class NotificationConf {
  private EmailConf EMAIL;
  private HipChatConf HIPCHAT;
  private ConsoleNotificationConf CONSOLE;

  public EmailConf getEMAIL() {
    return EMAIL;
  }

  public void setEMAIL(EmailConf EMAIL) {
    this.EMAIL = EMAIL;
  }

  public HipChatConf getHIPCHAT() {
    return HIPCHAT;
  }

  public void setHIPCHAT(HipChatConf HIPCHAT) {
    this.HIPCHAT = HIPCHAT;
  }

  public ConsoleNotificationConf getCONSOLE() {
    return CONSOLE;
  }

  public void setCONSOLE(ConsoleNotificationConf CONSOLE) {
    this.CONSOLE = CONSOLE;
  }
}
