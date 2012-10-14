package com.referencelogic.xmlormupload;

import java.util.ArrayList;

public interface UploadableForm {
  boolean saveToDatabase();
  
  boolean loadFromString();
}