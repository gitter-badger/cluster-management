package org.noetl.parsers;

import org.junit.Test;
import org.noetl.pojos.clusterConfigs.BootStrapConf;
import org.noetl.pojos.clusterConfigs.ClusterConf;
import org.noetl.pojos.clusterConfigs.ClusterConfJson;
import org.noetl.pojos.clusterConfigs.ClusterNodeConf;
import org.noetl.pojos.clusterConfigs.EMRPremium;
import org.noetl.pojos.clusterConfigs.InstanceTypeConf;
import org.noetl.pojos.clusterConfigs.StepConfigConf;
import org.noetl.pojos.notificationConfigs.ConsoleNotificationConf;
import org.noetl.pojos.notificationConfigs.EmailConf;
import org.noetl.pojos.notificationConfigs.HipChatConf;
import org.noetl.pojos.notificationConfigs.NotificationConf;
import org.noetl.pojos.monitorConfigs.BackupConf;
import org.noetl.pojos.monitorConfigs.ExpectedFilesConf;
import org.noetl.pojos.monitorConfigs.MonitorConf;
import org.noetl.pojos.monitorConfigs.RawFileConf;

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
  public void testMonitorConf() throws Exception {
    InputStream fileStream = this.getClass().getResourceAsStream("/confs/monitorConf.json");
    MonitorConf monitorConfJson = JsonParser.getMapper().readValue(fileStream, MonitorConf.class);

    assertEquals("/mnt/src/sftp_uploads", monitorConfJson.getPATH());
    assertEquals("copy", monitorConfJson.getOPERATION());

    ExpectedFilesConf expectedFilesConf = monitorConfJson.getEXPECTED_FILES();
    RawFileConf[] frequent_files = expectedFilesConf.getFREQUENT_FILES();
    String[] names1 = new String[frequent_files.length];
    for (int i = 0; i < frequent_files.length; i++) {
      names1[i] = frequent_files[i].getNAME();
    }
    assertArrayEquals(new String[]{"exp1$", "exp2$"}, names1);


    RawFileConf[] infrequent_files = expectedFilesConf.getINFREQUENT_FILES();
    String[] names2 = new String[infrequent_files.length];
    for (int i = 0; i < infrequent_files.length; i++) {
      names2[i] = infrequent_files[i].getNAME();
    }
    assertArrayEquals(new String[]{"(hi|hello)"}, names2);

    BackupConf backupConf = monitorConfJson.getBACKUP();
    assertArrayEquals(new String[]{"/mnt/dest/sftp_uploads"}, backupConf.getLOCAL());
    assertEquals("s3://bkt/raw/", backupConf.getS3_RAW());
    assertEquals("s3://bkt/stage/", backupConf.getS3_STAGE());
  }

  @Test
  public void testParsingMailConf() throws Exception {
    InputStream fileStream = this.getClass().getResourceAsStream("/confs/notificationConf.json");
    NotificationConf notificationConf = JsonParser.getMapper().readValue(fileStream, NotificationConf.class);

    EmailConf mailConf = notificationConf.getEMAIL();
    assertEquals("host.com", mailConf.getHOST());
    //assertEquals(587, mailConf.getPORT());
    assertEquals("sender@company.com", mailConf.getSENDER());
    assertEquals("password", mailConf.getSENDER_PASSWORD());
    String[] recipients = mailConf.getRECIPIENTS();
    assertEquals(1, recipients.length);
    String recipient0 = recipients[0];
    assertEquals("recipient@company.com", recipient0);
    assertEquals("true", mailConf.getAUTHENTICATION());
    assertEquals("true", mailConf.getSTARTTLS());

    HipChatConf hipChatConf = notificationConf.getHIPCHAT();
    assertEquals("https://noetl.hipchat.com/v2/room/2398612/notification?auth_token=jfckaBNVM14j8gkiItWZBdUJceat6ODGaNnvwFjp", hipChatConf.getRESTURI());
    assertEquals(true, hipChatConf.isNOTIFY());
    assertEquals("green", hipChatConf.getMESSAGE_COLOR());

    ConsoleNotificationConf console = notificationConf.getCONSOLE();
    assertNotNull(console);
  }
}
