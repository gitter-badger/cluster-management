package org.noetl.pojos.notificationConfigs;

public class NotificationConf {
  private EmailConf email;
  private HipChatConf hipChat;
  private ConsoleNotificationConf console;

  public EmailConf getEmail() {
    return email;
  }

  public void setEmail(EmailConf email) {
    this.email = email;
  }

  public HipChatConf getHipChat() {
    return hipChat;
  }

  public void setHipChat(HipChatConf hipChat) {
    this.hipChat = hipChat;
  }

  public ConsoleNotificationConf getConsole() {
    return console;
  }

  public void setConsole(ConsoleNotificationConf console) {
    this.console = console;
  }
}
