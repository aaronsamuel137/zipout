package src;

import src.Calendar.MyCalendar;
import src.Date;

public class ZipEntry {
  private String name;
  //Minimum version needed to extract the file(s) from a compressed state
  private static short REQ_VERSION;
  
  //Method used to compress file
  private static short COMPRESSION_METHOD;
	
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
  
  public ZipEntry(String name) {
    this.name = name;
  }

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
  
  //Method to get the time frome the system
  public void setTimeDate(){
	  //Create Date and Calendar objects to populate information
	  Date modTime = new Date();
	  MyCalendar modCalendar = new MyCalendar(modTime.getTime());
	  
	  //Hour is military time
	  int timeBits = (modCalendar.HOUR_OF_DAY);
	  
	  //Shift bits
	  timeBits = timeBits << 6;
	  
	 //Get the minutes from MyCalendar
	  int minBits = modCalendar.DAY_OF_MONTH;
	  
	  //Mask all but the last 6 bits of minutes
	  int minMask = 0xFFC0;
	  minBits = minBits ^ minMask;
	  
	  //Add minutes to dateBits
	  timeBits = timeBits ^ minBits;
	  
	  //Shift bits
	  timeBits = timeBits << 5;
	  
	  //Get the seconds from MyCalendar
	  int secBits = modCalendar.SECOND;
	  
	  //Divide seconds by 2
	  secBits = secBits >> 1;
	  
	  //Mask all but the last 5 bits of seconds
	  int secMask = 0xFFE0;
	  secBits = secBits ^ secMask;
	  
	  //Add minutes to dateBits
	  timeBits = timeBits ^ secBits;
	  
	  //Truncate to short then store
	  this.MOD_TIME = (short)timeBits;
	  
	  //Year is based on offset from 1980
	  int dateBits = (modCalendar.YEAR -1980);
	  
	  //Shift bits over
	  dateBits = dateBits << 4;
	  
	  //Get month from MyCalendar, Jan starts as "0"
	  dateBits = dateBits & (byte)(modCalendar.MONTH -1);
	  
	  //Shift bits over again
	  dateBits = dateBits << 5;
	  
	  //Get the day from MyCalendar
	  int dayBits = modCalendar.DAY_OF_MONTH;
	  
	  //Mask all but the last 5 bits of day
	  int dayMask = 0xFFE0;
	  dayBits = dayBits ^ dayMask;
	  
	  //Add day to dateBits
	  dateBits = dateBits ^ dayBits;
	  
	  //Truncate to short then store
	  this.MOD_DATE = (short)dateBits;
	  
	  
	  
	  
	  
	
	  
	  
	  
    
  }
  
  //Method to set the minimum version required to open the zip file
  //Valid values for the compression method are the numbers 1.0 to 10.0
  public boolean setRequiredVersion(float versionFloat){
	  //Check for valid version numbers
	  if (versionFloat < 1 || versionFloat > 100){
		  return false;
	  }
	  
	  //Convert to short value for storage
	  versionFloat = versionFloat * 10;
	  short versionShort = (short)versionFloat;
	  
	  //Set value of version
	  REQ_VERSION = versionShort;
	  return true;
  }
  
  //Method to set the compression method for the file
  //Valid values for the compression method are the numbers 0 to 19 and 99
  public boolean setCompressionMethod(short compMethod){
	  if (compMethod == 99){
		  COMPRESSION_METHOD = compMethod;
		  return true;
	  }
	  else if (compMethod < 0 || compMethod > 19){
		  return false;
	  }
	  else{
		  COMPRESSION_METHOD = compMethod;
		  return true;
	  }
  }
  
  //

  
}
