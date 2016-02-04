package org.noetl.pojos.notificationConfigs;

public class EmailConf {
  private String HOST;
  private int PORT;
  private String SENDER;
  private String SENDER_PASSWORD;
  private String[] RECIPIENTS;
  private String AUTHENTICATION;
  private String STARTTLS;

  public String getHOST() {
    return HOST;
  }

  public void setHOST(String HOST) {
    this.HOST = HOST;
  }

  public int getPORT() {
    return PORT;
  }

  public void setPORT(int PORT) {
    this.PORT = PORT;
  }

  public String getSENDER() {
    return SENDER;
  }

  public void setSENDER(String SENDER) {
    this.SENDER = SENDER;
  }

  public String getSENDER_PASSWORD() {
    return SENDER_PASSWORD;
  }

  public void setSENDER_PASSWORD(String SENDER_PASSWORD) {
    this.SENDER_PASSWORD = SENDER_PASSWORD;
  }

  public String[] getRECIPIENTS() {
    return RECIPIENTS;
  }

  public void setRECIPIENTS(String[] RECIPIENTS) {
    this.RECIPIENTS = RECIPIENTS;
  }

  public String getAUTHENTICATION() {
    return AUTHENTICATION;
  }

  public void setAUTHENTICATION(String AUTHENTICATION) {
    this.AUTHENTICATION = AUTHENTICATION;
  }

  public String getSTARTTLS() {
    return STARTTLS;
  }

  public void setSTARTTLS(String STARTTLS) {
    this.STARTTLS = STARTTLS;
  }
}
