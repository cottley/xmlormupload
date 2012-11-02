package com.referencelogic.xmlormupload.main;

import java.io.File;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;

import groovy.lang.GroovyClassLoader;
import java.io.IOException;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class Xmlormuploader implements Runnable {

  protected File file;
  protected XMLConfiguration config;
  private static final Logger log = Logger.getLogger( Xmlormuploader.class );
  protected boolean isDebugging;
  protected GroovyClassLoader gcl;
  protected SessionFactory sf;
  
  Xmlormuploader(File file, XMLConfiguration config, GroovyClassLoader gcl, SessionFactory sf) {
    this.file = file;
    this.config = config;
    isDebugging = log.isDebugEnabled();
    this.gcl = gcl;
    this.sf = sf;
  }

  public void run() {

      String sourceDir = config.getString("source.path");
      String destDir = config.getString("destination.path");
      String sourceFilePath = file.toString();
      if (isDebugging) { log.debug("Processing " + sourceFilePath); }

      XStream xstream = new XStream();
      xstream.setClassLoader(gcl); 
      
      // Read the file and get objects
      Object deserialized = xstream.fromXML(file);
            
      Session sess = sf.openSession();
      Transaction t = sess.beginTransaction();
       
      if (deserialized instanceof ArrayList) {
        log.debug("Deserialized object is an ArrayList");
        ArrayList ar = (ArrayList)deserialized;
        for (Object o : ar) {
          // Serialize
          sess.save(o);
        }
      } else {
        // Serialize
        sess.save(deserialized);
      }
      
      sess.flush();
      t.commit();
      sess.close();
      
  }

}
