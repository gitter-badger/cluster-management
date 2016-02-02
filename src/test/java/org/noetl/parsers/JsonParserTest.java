package org.noetl.parsers;

import org.noetl.pojos.notificationConfigs.ConsoleNotificationConf;
import org.noetl.pojos.notificationConfigs.EmailConf;
import org.noetl.pojos.clusterConfigs.BootStrapConf;
import org.noetl.pojos.clusterConfigs.ClusterConf;
import org.noetl.pojos.clusterConfigs.ClusterConfJson;
import org.noetl.pojos.clusterConfigs.ClusterNodeConf;
import org.noetl.pojos.clusterConfigs.EMRPremium;
import org.noetl.pojos.clusterConfigs.InstanceTypeConf;
import org.noetl.pojos.clusterConfigs.StepConfigConf;
import org.noetl.pojos.notificationConfigs.HipChatConf;
import org.noetl.pojos.notificationConfigs.NotificationConf;
import org.noetl.pojos.serviceConfigs.MonitorConfJson;
import org.junit.Test;

import java.io.Console;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsonParserTest {
  @Test
  public void testParsingClusterConf() throws Exception {
    InputStream fileStream = this.getClass().getResourceAsStream("/confs/clusterConf_comprehensive.json");
    ClusterConfJson clusterConfJson = JsonParser.getMapper().readValue(fileStream, ClusterConfJson.class);
    assertEquals("us-west-2", clusterConfJson.getRegion());
    assertEquals("http://spot-price.s3.amazonaws.com/spot.js", clusterConfJson.getSpotPriceURL());
    assertEquals("USD", clusterConfJson.getCurrency());
    assertEquals("aws-key", clusterConfJson.getKey());
    ClusterConf clusterConf = clusterConfJson.getCluster();
    assertEquals("MARS", clusterConf.getName());
    assertEquals("subnet-e0000", clusterConf.getSubnet());
    assertEquals("3.10", clusterConf.getVersion());
    assertEquals("EMR_DefaultRole", clusterConf.getServiceRole());
    assertEquals("EMR_EC2_DefaultRole", clusterConf.getJobFlowRole());
    assertEquals("s3://aws-emr-lg/", clusterConf.getLogURI());
    ClusterNodeConf masterNode = clusterConf.getMasterNode();
    assertEquals(1, masterNode.getCount());
    InstanceTypeConf masterNodeInstanceTypeConf = masterNode.getInstanceType();
    assertEquals("size", masterNodeInstanceTypeConf.getType());
    assertEquals("m2.2xlarge", masterNodeInstanceTypeConf.getSize());
    assertEquals(null, masterNodeInstanceTypeConf.getTier());
    assertEquals("on_demand", masterNode.getMarketType());
    assertEquals("linux", masterNode.getOs());

    ClusterNodeConf coreNode = clusterConf.getCoreNode();
    assertEquals(1, coreNode.getCount());
    InstanceTypeConf coreNodeInstanceTypeConf = coreNode.getInstanceType();
    assertEquals("tier", coreNodeInstanceTypeConf.getType());
    assertEquals(null, coreNodeInstanceTypeConf.getSize());
    assertEquals("high", coreNodeInstanceTypeConf.getTier());
    assertEquals("spot", coreNode.getMarketType());
    assertEquals("linux", coreNode.getOs());

    assertArrayEquals(new String[]{"Spark", "Hive", "HBASE", "Hue", "Ganglia"},
      clusterConf.getInstalls().toArray(new String[clusterConf.getInstalls().size()]));

    List<StepConfigConf> stepConfigConfs = clusterConf.getStepConfigs();
    assertEquals(1, stepConfigConfs.size());
    StepConfigConf stepConfigConf = stepConfigConfs.get(0);
    assertEquals("Install Hive", stepConfigConf.getName());
    assertEquals(true, stepConfigConf.isUseDefault());
    assertEquals(new HashMap<String, Object>(), stepConfigConf.getHadoopJarStepConfigs());

    List<BootStrapConf> bootStrapConfs = clusterConf.getBootStraps();
    assertEquals(1, bootStrapConfs.size());
    BootStrapConf bootStrapConf = bootStrapConfs.get(0);
    assertEquals("Install HBase", bootStrapConf.getName());
    assertEquals("s3://elasticmapreduce/bootstrap-actions/setup-hbase", bootStrapConf.getScript());

    Map<String, List<EMRPremium>> tiers = clusterConfJson.getTiers();
    assertEquals(2, tiers.size());
    List<EMRPremium> highTier = tiers.get("high");
    assertEquals(3, highTier.size());
    EMRPremium highTier1 = highTier.get(0);
    assertEquals("m3.2xlarge", highTier1.getSize());
    assertEquals(0.14, highTier1.getPremium(), 1e-6);

    List<EMRPremium> mediumTier = tiers.get("medium");
    assertEquals(3, mediumTier.size());
    EMRPremium mediumTier1 = mediumTier.get(0);
    assertEquals("m2.2xlarge", mediumTier1.getSize());
    assertEquals(0.123, mediumTier1.getPremium(), 1e-6);
  }

  @Test
  public void testParsingServiceConf() throws Exception {
    InputStream fileStream = this.getClass().getResourceAsStream("/confs/monitorConf.json");
    MonitorConfJson monitorConfJson = JsonParser.getMapper().readValue(fileStream, MonitorConfJson.class);

    assertArrayEquals(new String[]{"exp1", "exp2"},
      monitorConfJson.getExpectedFiles().toArray(new String[monitorConfJson.getExpectedFiles().size()]));

    assertEquals("/mnt/src/sftp_uploads", monitorConfJson.getSftpConf().getSource());
    assertEquals("/mnt/dest/sftp_uploads", monitorConfJson.getSftpConf().getDestination());
    assertEquals("s3://bkt/fresh/", monitorConfJson.getS3Conf().getBackUp());
    assertEquals("s3://bkt/stage/", monitorConfJson.getS3Conf().getStage());
  }

  @Test
  public void testParsingMailConf() throws Exception {
    InputStream fileStream = this.getClass().getResourceAsStream("/confs/notificationConf.json");
    NotificationConf notificationConf = JsonParser.getMapper().readValue(fileStream, NotificationConf.class);

    EmailConf mailConf = notificationConf.getEmail();
    assertEquals("host.com", mailConf.getHost());
    //assertEquals(587, mailConf.getPort());
    assertEquals("sender@company.com", mailConf.getSender());
    assertEquals("password", mailConf.getSenderPassword());
    List<String> recipients = mailConf.getRecipients();
    assertEquals(1, recipients.size());
    String recipient0 = recipients.get(0);
    assertEquals("recipient@company.com", recipient0);
    assertEquals("true", mailConf.getAuthentication());
    assertEquals("true", mailConf.getStarttls());

    HipChatConf hipChatConf = notificationConf.getHipChat();
    assertEquals("https://noetl.hipchat.com/v2/room/2398612/notification?auth_token=jfckaBNVM14j8gkiItWZBdUJceat6ODGaNnvwFjp", hipChatConf.getRestURI());
    assertEquals(true, hipChatConf.isNotify());
    assertEquals("green", hipChatConf.getMessageColor());

    ConsoleNotificationConf console = notificationConf.getConsole();
    assertNotNull(console);
  }
}
