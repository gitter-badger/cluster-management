package org.noetl.aws.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AWSS3UtilTest {

  @Test
  public void testGetBucketName() throws Exception {
    assertEquals("bkt", AWSS3Util.getBucketName("s3://bkt/fresh/"));
    assertEquals("bkt", AWSS3Util.getBucketName("s3://bkt/"));
  }

  @Test
  public void testGetBucketKeyName() throws Exception {
    assertEquals("fresh/", AWSS3Util.getKey("s3://bkt/fresh/"));
    assertEquals("", AWSS3Util.getKey("s3://bkt/ "));
  }
}
