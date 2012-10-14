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

import org.apache.commons.io.FileUtils;

import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.ConfigurationException;

import java.util.Iterator;

import java.util.List;

public class XmlormuploadMain {

    private static final Logger log = Logger.getLogger( XmlormuploadMain.class );
    private static boolean isDebugging;
    private static final String configFileName = "xmlormupload.config.xml";
    private ExecutorService exec;
    
    public static void main(String args[]) {
        PropertyConfigurator.configure("log4j.properties");
         isDebugging = log.isDebugEnabled();
        new XmlormuploadMain().run();
    }
    

    public void run() {
      try {
        XMLConfiguration config = new XMLConfiguration(configFileName);
        String sourceDir = config.getString("source.path");
        List allowedExtensions = config.getList("source.extensions");
        int poolSize = config.getInt("threadpool.size");
        if (isDebugging) { log.debug("Loaded configuration successfully. Reading file list from: " + sourceDir + " with allowed extensions " + allowedExtensions); }
        Iterator iter =  FileUtils.iterateFiles(new File(sourceDir), (String[])allowedExtensions.toArray(new String[allowedExtensions.size()]), true);
        if (poolSize < 1) { poolSize = 5; }

        exec = Executors.newFixedThreadPool(poolSize);
        
        while(iter.hasNext()) {
          File file = (File) iter.next();
          exec.execute(new Xmlormuploader(file, config));
        }
        
        exec.shutdown();

      } catch(ConfigurationException cex) {
        log.fatal("Unable to load config file " + configFileName + " to determine configuration.", cex);
      } 
    }
    

}
