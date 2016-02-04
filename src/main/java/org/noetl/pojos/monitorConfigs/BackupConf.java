package org.noetl.pojos.monitorConfigs;

public class BackupConf {
  private String[] LOCAL;
  private String S3_RAW;
  private String S3_STAGE;

  public String[] getLOCAL() {
    return LOCAL;
  }

  public void setLOCAL(String[] LOCAL) {
    this.LOCAL = LOCAL;
  }

  public String getS3_RAW() {
    return S3_RAW;
  }

  public void setS3_RAW(String s3_RAW) {
    S3_RAW = s3_RAW;
  }

  public String getS3_STAGE() {
    return S3_STAGE;
  }

  public void setS3_STAGE(String s3_STAGE) {
    S3_STAGE = s3_STAGE;
  }
}
