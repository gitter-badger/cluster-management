package org.noetl.automation.services;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import org.noetl.automation.services.notification.INotificationService;
import org.noetl.automation.services.notification.MultipleNotificationService;
import org.noetl.pojos.notificationConfigs.NotificationConf;

public abstract class BaseService {
  protected final INotificationService notificationService;
  protected final AWSCredentials credential;

  public BaseService(INotificationService notificationService, AWSCredentials credential) {
    this.notificationService = notificationService;
    this.credential = credential;
  }

  public BaseService(NotificationConf notificationConf, String accessKey, String secretAccessKey) {
    notificationService = new MultipleNotificationService(notificationConf);
    credential = new BasicAWSCredentials(accessKey, secretAccessKey);
  }

  public abstract void startService();
}
