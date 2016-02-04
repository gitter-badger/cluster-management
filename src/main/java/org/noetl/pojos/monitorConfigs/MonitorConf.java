package org.noetl.pojos.monitorConfigs;

public class MonitorConf {
  private String PATH;
  private String OPERATION;
  private ExpectedFilesConf EXPECTED_FILES;
  private BackupConf BACKUP;

  public String getPATH() {

    return PATH;
  }

  public void setPATH(String PATH) {
    this.PATH = PATH;
  }

  public String getOPERATION() {
    return OPERATION;
  }

  public void setOPERATION(String OPERATION) {
    this.OPERATION = OPERATION;
  }

  public ExpectedFilesConf getEXPECTED_FILES() {
    return EXPECTED_FILES;
  }

  public void setEXPECTED_FILES(ExpectedFilesConf EXPECTED_FILES) {
    this.EXPECTED_FILES = EXPECTED_FILES;
  }

  public BackupConf getBACKUP() {
    return BACKUP;
  }

  public void setBACKUP(BackupConf BACKUP) {
    this.BACKUP = BACKUP;
  }
}
