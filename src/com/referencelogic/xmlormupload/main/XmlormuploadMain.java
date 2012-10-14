/*
 * Copyright (c) 2004-2010, P. Simon Tuffs (simon@simontuffs.com)
 * All rights reserved.
 *
 * See the full license at http://one-jar.sourceforge.net/one-jar-license.html
 * This license is also included in the distributions of this software
 * under doc/one-jar-license.txt
 */
package com.referencelogic.xmlormupload.main;

import java.util.Arrays;
import com.thoughtworks.xstream.XStream;
import java.util.ArrayList;

public class XmlormuploadMain {
    
    public static void main(String args[]) {
        if (args == null)
            args = new String[0];
        System.out.println("xmlormupload main entry point, args=" + Arrays.asList(args));
        new XmlormuploadMain().run();
    }
    
    // Bring up the application: only expected to exit when user interaction
    // indicates so.
    public void run() {
        System.out.println("xmlormupload main is running");
        // Implement the functionality of the application. 
        XStream xstream = new XStream();
        System.out.println("xmlormupload OK.");
    }
    

}
