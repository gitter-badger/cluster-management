package org.noetl.pojos.serviceConfigs;

import java.util.List;

public class BackupConf {
  private List<String> LOCAL;
  private String S3_RAW;
  private String S3_STAGE;

  public List<String> getLOCAL() {
    return LOCAL;
  }

  public void setLOCAL(List<String> LOCAL) {
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
