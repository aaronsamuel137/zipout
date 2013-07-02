import java.util.Calendar;
import java.util.Date;


public class ZipEntry {
  private String name;
  //Minimum version needed to extract the file(s) from a compressed state
  private static short REQ_VERSION;
  
  //Method used to compress file
  private static short COMPRESSION_METHOD;
  
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
    return name;
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
	  final int DAY_OF_MONTH = 5;
	  final int HOUR_OF_DAY = 11;
	  final int MINUTE = 12;
	  final int MONTH = 2;
	  final int SECOND = 13;
	  final int YEAR = 1;
	  
	  //Create Calendar object
	  Calendar modCalendar = Calendar.getInstance();
	  
	  //Hour is military time
	  int timeBits = modCalendar.get(HOUR_OF_DAY);
	  
	  //Shift bits
	  timeBits = timeBits << 6;
	  
	 //Get the minutes from MyCalendar
	  int minBits = modCalendar.get(MONTH);
	  
	  //Mask all but the last 6 bits of minutes
	  int minMask = 0xFFC0;
	  minBits = minBits ^ minMask;
	  
	  //Add minutes to dateBits
	  timeBits = timeBits ^ minBits;
	  
	  //Shift bits
	  timeBits = timeBits << 5;
	  
	  //Get the seconds from MyCalendar
	  int secBits = modCalendar.get(SECOND);
	  
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
	  int dateBits = (modCalendar.get(YEAR) -1980);
	  
	  //Shift bits over
	  dateBits = dateBits << 4;
	  
	  //Get month from MyCalendar, Jan starts as "0"
	  dateBits = dateBits & (byte)(modCalendar.get(MONTH) -1);
	  
	  //Shift bits over again
	  dateBits = dateBits << 5;
	  
	  //Get the day from MyCalendar
	  int dayBits = modCalendar.get(DAY_OF_MONTH);
	  
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
  
  private static class MyCalendar extends Calendar {
    private static final long MILLIS_PER_DAY = 86400000;
    private static final int MILLIS_PER_HOUR = 3600000;
    private static final int MILLIS_PER_MINUTE = 60000;
    private static final int MILLIS_PER_SECOND = 1000;

    private static final int EPOCH_YEAR = 1970;
    private static final int EPOCH_LEAP_YEAR = 1968;
    private static final int DAYS_TO_EPOCH = 731;

    private static final int[][] DAYS_IN_MONTH = new int[][] {
      { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 },
      { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 }
    };

    public MyCalendar(long time) {
      this.time = time;
      this.isTimeSet = true;
      parseIntoFields(time);
    }

    public void setTime(Date date) {
      super.setTime(date);
      parseIntoFields(this.time);
    }

    private static boolean isLeapYear(int year) {
      return (year%4 == 0) && (year%100 != 0) || (year%400 == 0);
    }
    
    private void parseIntoFields(long timeInMillis) {
      long days = timeInMillis / MILLIS_PER_DAY;
      /* convert days since Jan 1, 1970 to days since Jan 1, 1968 */
      days += DAYS_TO_EPOCH;
      long years = 4 * days / 1461; /* days/365.25 = 4*days/(4*365.25) */
      int year = (int)(EPOCH_LEAP_YEAR + years);
      days -= 365 * years + years / 4;
      if (!isLeapYear(year)) days--;
      
      int month=0;
      int leapIndex = isLeapYear(year) ? 1 : 0;
      while (days >= DAYS_IN_MONTH[leapIndex][month]) {
        days -= DAYS_IN_MONTH[leapIndex][month++];
      }
      days++;

      int remainder = (int)(timeInMillis % MILLIS_PER_DAY);
      int hour = remainder / MILLIS_PER_HOUR;
      remainder = remainder % MILLIS_PER_HOUR;
      int minute = remainder / MILLIS_PER_MINUTE;
      remainder = remainder % MILLIS_PER_MINUTE;
      int second = remainder / MILLIS_PER_SECOND;
      fields[YEAR] = year;
      fields[MONTH] = month;
      fields[DAY_OF_MONTH] = (int)days;
      fields[HOUR_OF_DAY] = hour;
      fields[MINUTE] = minute;
      fields[SECOND] = second;
    }

    @Override
    public void add(int arg0, int arg1) {
      // TODO Auto-generated method stub
      
    }

    @Override
    public int getActualMaximum(int arg0) {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public int getActualMinimum(int arg0) {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public int getMaximum(int arg0) {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public int getMinimum(int arg0) {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public void roll(int arg0, boolean arg1) {
      // TODO Auto-generated method stub
      
    }
  }
}
