package gov.nasa.pds.model.plugin.util;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gov.nasa.pds.model.plugin.DMDocument;
import gov.nasa.pds.model.plugin.DOMMsgDefn;

public class Utility {
  private static final Logger LOG = LoggerFactory.getLogger(Utility.class);

  public static boolean checkCreateDirectory(String lDirectoryPathName) {
    File file = new File(lDirectoryPathName);
    if (file.exists() && file.isDirectory()) {
      Utility.registerMessage("0>info Found directory: " + lDirectoryPathName);
      return true;
    }
    // Create the directory
    boolean bool = file.mkdirs();
    if (bool) {
      Utility.registerMessage("0>info Created directory: " + lDirectoryPathName);
      return true;
    } else {
      Utility.registerMessage("1>error Directory create failed: " + lDirectoryPathName);
    }
    return false;
  }

  public static boolean checkFileName(String inputFileName) {
    File file = new File(inputFileName);
    if (file.exists() && (file.isFile())) {
      Utility.registerMessage("0>info Found input file: " + inputFileName);
      return true;
    }
    Utility.registerMessage("1>error " + "Input file not found: " + inputFileName);
    return false;
  }

  public static void registerMessage(String lMessage) {
    DOMMsgDefn lMessageDefn = new DOMMsgDefn(lMessage);
    DMDocument.mainMsgArr.add(lMessageDefn);
    return;
  }

  public static void registerMessage(String lNameSpaceIdNCLC, String lMessage) {
    DOMMsgDefn lMessageDefn = new DOMMsgDefn(lMessage);
    lMessageDefn.setNameSpaceIdNCLC(lNameSpaceIdNCLC);
    DMDocument.mainMsgArr.add(lMessageDefn);
    return;
  }

  public static String[] getFilepaths(String basePath, String extension) throws URISyntaxException {
    if (!extension.startsWith(".")) {
      extension = "." + extension;
    }

    Path path = Paths.get(basePath);
    Iterator<File> fileIter = FileUtils.iterateFiles(path.toFile(),
        WildcardFileFilter.builder().setWildcards("*" + extension).get(), TrueFileFilter.INSTANCE);

    List<String> fileList = new ArrayList<>();
    while (fileIter.hasNext()) {
      fileList.add(fileIter.next().getAbsolutePath());
    }

    String[] filepaths = new String[fileList.size()];
    return fileList.toArray(filepaths);
  }

	
}
