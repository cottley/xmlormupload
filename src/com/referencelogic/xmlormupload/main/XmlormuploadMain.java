/*
 * Copyright (c) 2004-2010, P. Simon Tuffs (simon@simontuffs.com)
 * All rights reserved.
 *
 * See the full license at http://one-jar.sourceforge.net/one-jar-license.html
 * This license is also included in the distributions of this software
 * under doc/one-jar-license.txt
 */
package com.referencelogic.xmlormupload.main;

import com.thoughtworks.xstream.XStream;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.ConfigurationException;

import java.util.Iterator;

import java.util.List;

import groovy.lang.GroovyClassLoader;
import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

import org.hibernate.service.classloading.spi.ClassLoaderService;

import org.hibernate.service.BootstrapServiceRegistryBuilder;
import org.hibernate.service.BootstrapServiceRegistry;


public class XmlormuploadMain {

    private static final Logger log = Logger.getLogger( XmlormuploadMain.class );
    private static boolean isDebugging;
    private static final String configFileName = "xmlormupload.config.xml";
    private ExecutorService exec;
    private static boolean matchRegex = false;
    private static String matchRegexStr = "";
    
    public static void main(String args[]) {
        PropertyConfigurator.configure("log4j.properties");
        isDebugging = log.isDebugEnabled();
        for (String s : args)
        {
          log.debug("Processing parameter: " + s);
          
          if (matchRegex && matchRegexStr.equals("")) {
            matchRegexStr = s;
            log.debug("Set regex string to: " + matchRegexStr);
          }
        
          if (s.equalsIgnoreCase("--restrict")) {
            matchRegex = true;
            log.debug("Got restrict flag, so matching regex");
          }
        }        
        new XmlormuploadMain().run();
    }
    

    public void run() {
      try {
        XMLConfiguration config = new XMLConfiguration(configFileName);
        String sourceDir = config.getString("source.path");
        List allowedExtensions = config.getList("source.extensions");
        int poolSize = config.getInt("threadpool.size");

        String groovySourceDir = config.getString("domainclasses.path");
        List groovyAllowedExtensions = config.getList("domainclasses.extensions");

        
        if (isDebugging) { log.debug("Loaded configuration successfully. Reading groovy class list from: " + groovySourceDir + " with allowed extensions " + groovyAllowedExtensions); }
        
        if (isDebugging) { log.debug("Loaded configuration successfully. Reading file list from: " + sourceDir + " with allowed extensions " + allowedExtensions); }
        Iterator iter =  FileUtils.iterateFiles(new File(sourceDir), (String[])allowedExtensions.toArray(new String[allowedExtensions.size()]), true);
        if (poolSize < 1) { poolSize = 5; }
        
        exec = Executors.newFixedThreadPool(poolSize);
                
        GroovyClassLoader gcl = new GroovyClassLoader();

        ClassLoader ojcl = Thread.currentThread().getContextClassLoader();
        
        boolean allFilesResolved = false;
        while (!allFilesResolved) {
          Iterator groovyIter =  FileUtils.iterateFiles(new File(groovySourceDir), (String[])groovyAllowedExtensions.toArray(new String[groovyAllowedExtensions.size()]), true);
        
          allFilesResolved = true;
        
          while (groovyIter.hasNext()) {
            File groovyFile = (File) groovyIter.next();
            log.info("Trying to parse file " + groovyFile);
            try {
              Class clazz = gcl.parseClass(groovyFile);
            } catch (IOException ioe) {
              log.error("Unable to read file " + groovyFile + " to parse class ", ioe);
            } catch (Exception e) {
              log.error("Unable to parse file " + groovyFile + " ex:" + e);
              allFilesResolved = false;
            }
          }
          
        }
                                
        Thread.currentThread().setContextClassLoader(gcl);
        
        Configuration hibernateConfig = new Configuration();
        
        SessionFactory sf;
        if (!matchRegex) {
          sf = hibernateConfig.configure(new File("hibernate.config.xml")).buildSessionFactory();
        } else {
          sf = hibernateConfig.configure(new File("hibernate.update.config.xml")).buildSessionFactory();
        }

        log.info("Opened session");
        
        while(iter.hasNext()) {
          File file = (File) iter.next();
          String filePath = "";
          try {
            filePath = file.getCanonicalPath();
            log.debug("Canonical path being processed is: " + filePath);
          } catch (IOException ioe) {
            log.warn("Unable to get canonical path from file", ioe);
          }
          log.debug("Is matchRegex true? " + matchRegex);
          log.debug("Does filePath match regexStr?" + filePath.matches(matchRegexStr));
          if ((!matchRegex) || (matchRegex && filePath.matches(matchRegexStr))) {
            exec.execute(new Xmlormuploader(file, config, gcl, sf));
          }
        }
        
        exec.shutdown();
        try {
          while(!exec.isTerminated()) {
            exec.awaitTermination(30, TimeUnit.SECONDS); 
          }
        } catch (InterruptedException ie) {
          // Do nothing, going to close database connection anyway        
        }
       
        sf.close();

      } catch(ConfigurationException cex) {
        log.fatal("Unable to load config file " + configFileName + " to determine configuration.", cex);
      } 
    }
    
}
