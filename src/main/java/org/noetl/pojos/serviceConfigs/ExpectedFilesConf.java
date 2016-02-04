package org.noetl.pojos.serviceConfigs;

public class ExpectedFilesConf {
  private String[] FREQUENT_FILES;
  private String[] INFREQUENT_FILES;

  public String[] getFREQUENT_FILES() {
    return FREQUENT_FILES;
  }

  public void setFREQUENT_FILES(String[] FREQUENT_FILES) {
    this.FREQUENT_FILES = FREQUENT_FILES;
  }

  public String[] getINFREQUENT_FILES() {
    return INFREQUENT_FILES;
  }

  public void setINFREQUENT_FILES(String[] INFREQUENT_FILES) {
    this.INFREQUENT_FILES = INFREQUENT_FILES;
  }
}
