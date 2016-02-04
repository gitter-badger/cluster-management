package org.noetl.automation.services.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.log4j.Logger;
import org.noetl.pojos.notificationConfigs.HipChatConf;
import org.noetl.utils.GeneralUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class HipChatRoomNotificationService implements INotificationService {
  private static final Logger logger = Logger.getLogger(HipChatRoomNotificationService.class);
  private String messageColor;
  private boolean notifyTheRoom;
  private String restURI;


  public HipChatRoomNotificationService(HipChatConf hipChatConf) {
    this(hipChatConf.getRESTURI(), hipChatConf.isNOTIFY(), hipChatConf.getMESSAGE_COLOR());
  }

  public HipChatRoomNotificationService(String restURI, boolean notifyTheRoom, String messageColor) {
    this.restURI = restURI;
    this.notifyTheRoom = notifyTheRoom;
    this.messageColor = messageColor;
  }

  @Override
  public void notify(String subject, String text) {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode root = mapper.createObjectNode();

    root.put("color", messageColor);
    root.put("message", text);
    root.put("notify", notifyTheRoom);
    root.put("message_format", "text");
    try {
      ProcessBuilder pb = new ProcessBuilder("curl", "-d", root.toString(), "-H", "Content-Type: application/json", restURI);
      pb.redirectErrorStream(true);
      Process p = pb.start();
      p.waitFor();
      StringBuilder output = new StringBuilder();
      BufferedReader reader =
        new BufferedReader(new InputStreamReader(p.getInputStream()));
      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line).append(System.lineSeparator());
      }
      //logger.info(output.toString());
    } catch (Exception e) {
      logger.error(GeneralUtils.getStackTrace(e));
    }
  }
}
