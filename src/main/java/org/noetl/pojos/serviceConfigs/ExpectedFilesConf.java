package org.noetl.pojos.serviceConfigs;

import java.util.List;

public class ExpectedFilesConf {
  private List<String> FREQUENT_FILES;
  private List<String> INFREQUENT_FILES;

  public List<String> getFREQUENT_FILES() {
    return FREQUENT_FILES;
  }

  public void setFREQUENT_FILES(List<String> FREQUENT_FILES) {
    this.FREQUENT_FILES = FREQUENT_FILES;
  }

  public List<String> getINFREQUENT_FILES() {
    return INFREQUENT_FILES;
  }

  public void setINFREQUENT_FILES(List<String> INFREQUENT_FILES) {
    this.INFREQUENT_FILES = INFREQUENT_FILES;
  }
}
