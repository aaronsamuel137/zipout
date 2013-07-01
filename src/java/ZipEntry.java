/* Copyright (c) 2008-2013, Avian Contributors

   Permission to use, copy, modify, and/or distribute this software
   for any purpose with or without fee is hereby granted, provided
   that the above copyright notice and this permission notice appear
   in all copies.

   There is NO WARRANTY for this software.  See license.txt for
   details. */

import Calendar.java;

public static class ZipEntry {
  //Minimum version needed to extract the file(s) from a compressed state
  private static byte REQ_VERSION;
  
  //Method used to compress file
  private static byte COMPRESSION_METHOD;
	
  //Utilize Calendar class to determine time/date of modification
  private static Calendar LAST_MOD;
  
  //Format of date and time are both 2 byte fields
  private static short MOD_TIME;
  private static short MOD_DATE;
  
  //CRC-32
  private static int CRC;
  
  //Sizes of file
  private static int COMP_SIZE;
  private static int UNCOMP_SIZE;
  
  //File name length in ?
  private static short NAME_LEN;
  
  //Extra field length in ?
  private static short EXTRA_LEN;

  //Method to return name of the file
  public String getName() {
    return null;
  }
  
  //Method to check if file is a directory
  public boolean isDirectory() {
    return getName().endsWith("/");
  }

  //Method to return the compressed size of the file
  public int getCompressedSize() {
    return COMP_SIZE;
  }

  //Method to return the uncompressed size of the file
  public int getSize() {
    return UNCOMP_SIZE;
  }

  
}
