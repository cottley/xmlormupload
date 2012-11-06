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

import com.thoughtworks.xstream.mapper.MapperWrapper;

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
      
      try {
        if (file.length() > 0) {
          // Read the file and get objects
          Object deserialized = xstream.fromXML(file);
            
          Session sess = sf.openSession();
          Transaction t = sess.beginTransaction();
       
          if (deserialized instanceof ArrayList) {
            log.debug("Deserialized object is an ArrayList");
            ArrayList ar = (ArrayList)deserialized;
            for (Object o : ar) {
              // Serialize
              try {
                sess.save(o);
              } catch (Exception e) {
                log.error("Unable to save deserialized object.", e);
              }
            }
          } else {
            // Serialize
            try {
              sess.save(deserialized);
            } catch (Exception e) {
              log.error("Unable to save deserialized object.", e);
            }
          }
      
          sess.flush();
          t.commit();
          sess.close();
        } else {
          log.warn("File at " + sourceFilePath + " contains no data, ignored.");
        }
      } catch (Exception ex) {
        log.error("Could not deserialize " + sourceFilePath, ex);
      }
      
  }

}
