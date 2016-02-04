package org.noetl.utils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.noetl.automation.services.notification.INotificationService;
import org.noetl.aws.utils.AWSS3Util;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FileOps {

  private static final Logger logger = Logger.getLogger(FileOps.class);
  private final INotificationService notifier;

  public FileOps(INotificationService notifier) {
    if (notifier == null)
      throw new RuntimeException("Notification service is required for this class " + FileOps.class.getName());
    this.notifier = notifier;
  }

  public void uploadFileToS3(File srcFile, Iterable<String> s3Destinations, AWSCredentials credential) {
    TransferManager tx = null;
    List<String> destinations = new LinkedList<>();
    try {
      for (String s3Destination : s3Destinations) {
        String bucket = AWSS3Util.getBucketName(s3Destination);
        String key = AWSS3Util.getKey(s3Destination);

        logger.info(String.format("Uploading file '%s' to s3 '%s'.", srcFile.toString(), s3Destination));
        tx = new TransferManager(credential);
        final Upload upload = tx.upload(bucket, key + srcFile.getName(), srcFile);
        upload.waitForCompletion();
        destinations.add(s3Destination + srcFile.getName());
      }
      String msg = String.format("A new file got uploaded to s3 successfully.\n\nFile:\n\t%s\nS3 Destinations:\n\t%s\n", srcFile.toString(), StringUtils.join(destinations, "\n\t"));
      logger.info(msg);
      notifier.notify("New file got uploaded to S3", msg);
    } catch (Exception e) {
      String subject = "File uploading to s3 failed.";
      String errorMsg = String.format("%s\n\nFile:\n\t%s\nS3 Destinations:\n\t%s\nError Message:\n\t%s\n",
        subject,
        srcFile.toString(),
        StringUtils.join(destinations, "\n\t"),
        GeneralUtils.getStackTrace(e));
      logger.error(errorMsg);
      notifier.notify(subject, errorMsg);
    } finally {
      if (tx != null)
        tx.shutdownNow();
    }
  }

  public void uploadFolderS3(File folder, Iterable<String> s3Destinations, AWSCredentials credential) {
    TransferManager tx = null;
    String folderName = folder.getName();
    String folderPath = folder.toString();
    int pathLength = folderPath.length();
    ArrayList<String> transferedFiles = new ArrayList<>();
    List<String> destinations = new LinkedList<>();
    try {
      for (String s3Destination : s3Destinations) {
        String bucket = AWSS3Util.getBucketName(s3Destination);
        String s3DestinationKey = AWSS3Util.getKey(s3Destination);
        logger.info(String.format("Uploading folder '%s' to s3 '%s'.", folderPath, s3Destination));
        tx = new TransferManager(credential);
        ArrayList<File> listOfFiles = getAllFiles(folder);

        for (File file : listOfFiles) {
          String s3Path = file.toString().substring(pathLength);
          String s3Key = s3DestinationKey + folderName + s3Path;
          Upload upload = tx.upload(bucket, s3Key, file);
          upload.waitForCompletion();
          String uploaded = AWSS3Util.makeS3Path(bucket, s3Key);
          transferedFiles.add(uploaded);
          logger.info("Uploaded file: " + uploaded);
        }
        destinations.add(s3Destination + folderName);
      }
      String msg = String.format(
        "A new folder got uploaded to s3 successfully.\n\nFolder:\n\t%s\nS3 Destinations:\n\t%s\nFiles:\n\t\n%s",
        folderPath,
        StringUtils.join(destinations, "\n\t"),
        StringUtils.join(transferedFiles, "\n\t"));
      logger.info(msg);
      notifier.notify("New folder got uploaded to S3", msg);
    } catch (Exception e) {
      String[] stockArr = new String[transferedFiles.size()];
      String subject = "Folder uploading to s3 failed.";
      String errorMsg = String.format(
        "%s\n\nFolder:\t%s\nS3 Destinations:\n\t%s\nFiles:\n\t\n%s\nError Message:\n\t%s\n",
        subject,
        folderPath,
        StringUtils.join(destinations, "\n\t"),
        StringUtils.join(transferedFiles, "\n\t"), GeneralUtils.getStackTrace(e));
      logger.error(errorMsg);
      notifier.notify(subject, errorMsg);
    } finally {
      if (tx != null)
        tx.shutdownNow();
    }
  }

  private ArrayList<File> getAllFiles(File folder) {
    ArrayList<File> allFiles = new ArrayList<>();
    if (!folder.isDirectory())
      throw new RuntimeException("Expecting a folder for this method call, but getting: " + folder.toString());

    File[] firstLevelFiles = folder.listFiles();
    if (firstLevelFiles == null || firstLevelFiles.length == 0)
      return allFiles;

    for (File f : firstLevelFiles) {
      if (f.isFile())
        allFiles.add(f);
      else
        allFiles.addAll(getAllFiles(f));
    }
    return allFiles;
  }

  public void moveFileLocal(File file, Iterable<String> localDestinations) {
    for (String dest : localDestinations) {
      moveAndRenameFileLocal(file, new File(dest, file.getName()).getAbsolutePath());
    }
  }

  private void moveAndRenameFileLocal(File file, String newFullPath) {
    if (!file.exists())
      throw new RuntimeException("The source file doesn't exist. " + file.getAbsolutePath());
    if (!file.isFile())
      throw new RuntimeException("Expecting a file for this method call, but getting: " + file.getAbsolutePath());
    try {
      File destFile = new File(newFullPath);
      if (destFile.exists())
        throw new RuntimeException("The destination path already exists: " + destFile.getAbsolutePath());
      FileUtils.copyFile(file, destFile);
    } catch (Exception e) {
      String subject = "Local file movement failed. Keep the file at its original place.";
      String errorMsg = String.format("%s\n%s", subject, GeneralUtils.getStackTrace(e));
      logger.error(errorMsg);
      notifier.notify(subject, errorMsg);
    }
  }

  public void moveDirectoryLocal(File folder, Iterable<String> localDestinations) {
    for (String dest : localDestinations) {
      moveAndRenameDirectoryLocal(folder, new File(dest, folder.getName()).getAbsolutePath());
    }
  }

  private void moveAndRenameDirectoryLocal(File folder, String newFullPath) {
    if (!folder.exists())
      throw new RuntimeException("The source folder doesn't exist. " + folder.getAbsolutePath());
    if (!folder.isDirectory())
      throw new RuntimeException("Expecting a folder for this method call, but getting: " + folder.getAbsolutePath());
    try {
      File destFolder = new File(newFullPath);
      if (destFolder.exists())
        throw new RuntimeException("The destination path already exists: " + destFolder.getAbsolutePath());
      FileUtils.moveDirectory(folder, destFolder);
    } catch (Exception e) {
      String subject = "Local folder movement failed. Keep the directory at its original place.";
      String errorMsg = String.format("%s\n%s", subject, GeneralUtils.getStackTrace(e));
      logger.error(errorMsg);
      notifier.notify(subject, errorMsg);
    }
  }
  /*
  public static void moveFileSFTP(FileProp fle) throws JSchException, SftpException, IOException {
    String hostname = "ec2-52-25-234-80.us-west-2.compute.amazonaws.com";
    String login = "ec2-user";
    String directory = fle.finalPath;

    java.util.Properties config = new java.util.Properties();
    config.put("StrictHostKeyChecking", "no");

    JSch ssh = new JSch();
    ssh.addIdentity("/Users/girishmohandass/.ssh/data-key.pem");
    Session session = ssh.getSession(login, hostname, 22);
    session.setConfig(config);
    session.connect();
    Channel channel = session.openChannel("sftp");
    channel.connect();

    File localFile = new File(fle.origPath + fle.Name);
    ChannelSftp sftp = (ChannelSftp) channel;
    sftp.cd(directory);
    sftp.put(new FileInputStream(localFile), localFile.getName());

    System.out.println("File is copied from " + fle.origPath + fle.Name + " to " + login + "@" + hostname + directory);
    channel.disconnect();
    session.disconnect();

  }
  */
}
