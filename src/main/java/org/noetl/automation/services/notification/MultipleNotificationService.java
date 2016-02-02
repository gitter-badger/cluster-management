package org.noetl.automation.services.notification;

import org.noetl.pojos.notificationConfigs.ConsoleNotificationConf;
import org.noetl.pojos.notificationConfigs.EmailConf;
import org.noetl.pojos.notificationConfigs.HipChatConf;
import org.noetl.pojos.notificationConfigs.NotificationConf;

import java.util.ArrayList;

public class MultipleNotificationService implements INotificationService {

  private final ArrayList<INotificationService> notificationServices;

  public MultipleNotificationService(NotificationConf notificationConf) {
    notificationServices = new ArrayList<>();
    EmailConf email = notificationConf.getEmail();
    if (email != null)
      notificationServices.add(new EmailNotificationService(email));
    HipChatConf hipChat = notificationConf.getHipChat();
    if (hipChat != null)
      notificationServices.add(new HipChatRoomNotificationService(hipChat));
    ConsoleNotificationConf console = notificationConf.getConsole();
    if (console != null)
      notificationServices.add(new ConsoleNotificationService());
  }

  @Override
  public void notify(String subject, String text) {
    for (INotificationService notifier : notificationServices) {
      notifier.notify(subject, text);
    }
  }
}
