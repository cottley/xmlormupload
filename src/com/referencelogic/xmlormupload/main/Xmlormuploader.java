package com.referencelogic.xmlormupload.main;

import java.io.File;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

public class Xmlormuploader implements Runnable {

  protected File file;
  protected XMLConfiguration config;
  private static final Logger log = Logger.getLogger( Xmlormuploader.class );
  protected boolean isDebugging;

  Xmlormuploader(File file, XMLConfiguration config) {
    this.file = file;
    this.config = config;
    isDebugging = log.isDebugEnabled();
  }

  public void run() {

      String sourceDir = config.getString("source.path");
      String destDir = config.getString("destination.path");
      String sourceFilePath = file.toString();
      if (isDebugging) { log.debug("Processing " + sourceFilePath); }

  }

}
