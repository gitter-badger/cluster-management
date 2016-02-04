package org.noetl.pojos.monitorConfigs;

public class ExpectedFilesConf {
  private RawFileConf[] FREQUENT_FILES;
  private RawFileConf[] INFREQUENT_FILES;

  public RawFileConf[] getFREQUENT_FILES() {
    return FREQUENT_FILES;
  }

  public void setFREQUENT_FILES(RawFileConf[] FREQUENT_FILES) {
    this.FREQUENT_FILES = FREQUENT_FILES;
  }

  public RawFileConf[] getINFREQUENT_FILES() {
    return INFREQUENT_FILES;
  }

  public void setINFREQUENT_FILES(RawFileConf[] INFREQUENT_FILES) {
    this.INFREQUENT_FILES = INFREQUENT_FILES;
  }
}
