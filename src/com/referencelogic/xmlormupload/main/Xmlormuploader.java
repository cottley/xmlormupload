package com.referencelogic.xmlormupload.main;

import java.io.File;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;

import groovy.lang.GroovyClassLoader;
import java.io.IOException;

public class Xmlormuploader implements Runnable {

  protected File file;
  protected XMLConfiguration config;
  private static final Logger log = Logger.getLogger( Xmlormuploader.class );
  protected boolean isDebugging;
  protected GroovyClassLoader gcl;

  Xmlormuploader(File file, XMLConfiguration config, GroovyClassLoader gcl) {
    this.file = file;
    this.config = config;
    isDebugging = log.isDebugEnabled();
    this.gcl = gcl;
  }

  public void run() {

      String sourceDir = config.getString("source.path");
      String destDir = config.getString("destination.path");
      String sourceFilePath = file.toString();
      if (isDebugging) { log.debug("Processing " + sourceFilePath); }

      XStream xstream = new XStream();
      xstream.setClassLoader(gcl); 
      
      // Read the file and get objects
      xstream.fromXML(file);
      
      // TODO serialize to relational database
      
  }

}
