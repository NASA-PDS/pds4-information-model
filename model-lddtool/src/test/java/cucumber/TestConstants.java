package cucumber;

import java.io.File;

public final class TestConstants {

  public static final String TEST_DATA_DIR =
      System.getProperty("user.dir") + File.separator + "src/test/resources/data";
  public static final String TEST_OUT_DIR =
      System.getProperty("user.dir") + File.separator + "target/test";
  public static final String EXAMPLE_DIR =
      System.getProperty("user.dir") + File.separator + "src/main/resources/examples";
  public static final String RESOURCES_DIR =
		  TEST_DATA_DIR + File.separator + "conf";
}