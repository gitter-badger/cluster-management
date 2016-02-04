package org.noetl.automation.services.notification;

import org.noetl.pojos.notificationConfigs.EmailConf;
import org.apache.log4j.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class EmailNotificationService implements INotificationService {

  private final static Logger logger = Logger.getLogger(EmailNotificationService.class);
  private final EmailConf mailConf;

  public EmailNotificationService(EmailConf mailConf) {

    this.mailConf = mailConf;
  }

  public void notify(String subject, String text) {
    try {
      Session session = initialization();
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(mailConf.getSENDER()));
      String[] recipientList = mailConf.getRECIPIENTS();

      for (String receiver : recipientList) {
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
        message.setSubject(subject);
        message.setText(text);
        Transport.send(message);
      }
      logger.info("Sent email to: " + Arrays.toString(recipientList));
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }

  private Session initialization() {
    Properties props = new Properties();
    //It must be a STRING!!!!!
    props.put("mail.smtp.auth", mailConf.getAUTHENTICATION());
    //It must be a STRING!!!!!
    props.put("mail.smtp.starttls.enable", mailConf.getSTARTTLS());
    props.put("mail.smtp.host", mailConf.getHOST());
    props.put("mail.smtp.port", mailConf.getPORT());

    Session session = Session.getDefaultInstance(props,
      new javax.mail.Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication
            (mailConf.getSENDER(), mailConf.getSENDER_PASSWORD());
        }
      });
    logger.info("Finish mail service setup.");
    return session;
  }
}
