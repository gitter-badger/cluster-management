package org.noetl.automation.services;

import org.noetl.parsers.JsonParser;
import org.noetl.pojos.AutomationConf;
import org.noetl.pojos.serviceConfigs.BackupConf;
import org.noetl.pojos.serviceConfigs.MonitorConf;
import org.noetl.utils.DateTimeUtil;
import org.noetl.utils.FileOps;
import org.noetl.utils.GeneralUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SFTPToS3Service extends BaseService {
  private static final Logger logger = Logger.getLogger(SFTPToS3Service.class);
  private final FileOps fileOps;
  private final MonitorConf monitorConfJson;
  private final String monitorPath;
  private final String operation;
  private final BackupConf backupPaths;

  public SFTPToS3Service(File configurationFile) throws IOException {
    this(JsonParser.getMapper().readValue(configurationFile, AutomationConf.class));
  }

  public SFTPToS3Service(AutomationConf automationConf) throws IOException {
    super(automationConf.getNotification(), automationConf.getAccessKey(), automationConf.getSecretAccessKey());
    fileOps = new FileOps(notificationService);
    monitorConfJson = automationConf.getMonitorConf();
    monitorPath = monitorConfJson.getPATH();
    operation = monitorConfJson.getOPERATION();
    backupPaths = monitorConfJson.getBACKUP();
  }

  @Override
  public void startService() {
    try {
      Path faxFolder = Paths.get(monitorPath);
      WatchService watchService = FileSystems.getDefault().newWatchService();
      faxFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
      String[] s3Backups = new String[]{backupPaths.getS3_RAW(), backupPaths.getS3_STAGE()};
      List<String> localBackups = backupPaths.getLOCAL();
      boolean valid;
      logger.info("SFTP to S3 service started...");
      do {
        WatchKey watchKey = watchService.take();
        for (WatchEvent event : watchKey.pollEvents()) {
          // !!!! Note !!!!
          // watchKey.pollEvents() is a blockage call.
          // it only returns when there is an event. Use screen rather than crontab.
          if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {
            File newFile = new File(monitorPath, event.context().toString());
            checkForCompletion(newFile);
            String timeString = DateTimeUtil.getCurrentTimeDefault();
            String destinationSuffix = "/" + DateTimeUtil.getCurrentTimeYMD();
            List<String> localDestinations = new ArrayList<>(localBackups.size());
            for (String localBackup : localBackups) {
              localDestinations.add(localBackup + destinationSuffix);
            }
            if (newFile.isFile()) {
              logger.info(String.format("New file '%s' detected at %s", newFile.toString(), timeString));
              fileOps.uploadFileToS3(newFile, Arrays.asList(s3Backups), credential);
              fileOps.moveFileLocal(newFile, localDestinations);
            } else {
              String folderName = newFile.getName();
              if (DateTimeUtil.canParseYMD(folderName))
                //Do not move date folder. It is created by code.
                continue;
              logger.info(String.format("New directory '%s' detected at %s", newFile.toString(), timeString));
              fileOps.uploadFolderS3(newFile, Arrays.asList(s3Backups), credential);
              fileOps.moveDirectoryLocal(newFile, localDestinations);
            }
          }
        }
        valid = watchKey.reset();
        if (!valid) {
          logger.info(String.format("Key reset invalid. Exiting monitoring for %s.", monitorPath));
          break;
        }
      } while (valid);
    } catch (IOException e) {
      String errorMsg = "Path under monitoring may not exist.\n" + GeneralUtils.getStackTrace(e);
      String subject = "SFTPToS3Service Failed";
      notificationService.notify(subject, errorMsg);
      throw new RuntimeException(subject, e);
    } catch (Exception e) {
      String subject = "SFTPToS3Service Failed";
      notificationService.notify(subject, GeneralUtils.getStackTrace(e));
      throw new RuntimeException(subject, e);
    }
  }

  public void checkForCompletion(File newFile) throws InterruptedException {
    long fileSize;
    long lastModified;
    do {
      fileSize = newFile.length();
      lastModified = newFile.lastModified();
      logger.info(String.format("File '%s' => current size:%d, last modified time:%s",
        newFile.toString(), fileSize, lastModified));
      Thread.sleep(1000);
    } while (fileSize != newFile.length() || lastModified != newFile.lastModified());
  }
}
