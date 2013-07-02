import java.util.Calendar;
import java.util.Date;


public class ZipEntry {
  String name;
  //Minimum version needed to extract the file(s) from a compressed state
  short reqVersion;
  
  //Method used to compress file
  short compressionMethod;
  
  //Format of date and time are both 2 byte fields
  short modTime;
  short modDate;
  
  //CRC-32
  int crc;
  
  //Sizes of file
  int compSize;
  int uncompSize;
  
  public ZipEntry(String name) {
    this.name = name;
    setTimeDate();
    compSize = 0;
    uncompSize = 0;
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
    return compSize;
  }

  //Method to return the uncompressed size of the file
  public int getSize() {
    return uncompSize;
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
    //adjust to out time zone
    timeBits = timeBits - 6;
	  
	  //Shift bits
	  timeBits = timeBits << 6;
	  
	  //Get the minutes from MyCalendar
	  int minBits = 0x3f & (modCalendar.get(MINUTE));;
	  
	  //Add minutes to dateBits
	  timeBits = timeBits ^ minBits;
	  
	  //Shift bits
	  timeBits = timeBits << 5;
	  
	  //Get the seconds from MyCalendar
	  int secBits = 0x1f & (modCalendar.get(SECOND));
	  
	  //Divide seconds by 2
	  secBits = secBits >> 1;
	  
	  //Add minutes to dateBits
	  timeBits = timeBits ^ secBits;
	  
	  //Truncate to short then store
	  modTime = (short)timeBits;
	  
	  //Year is based on offset from 1980
	  int dateBits = (modCalendar.get(YEAR) -1980);

	  //Shift bits over
	  dateBits = dateBits << 4;
    //System.out.println(dateBits);
	  
	  //Get month from MyCalendar, Jan starts as "0"
    int month = 0xf & (modCalendar.get(MONTH));
	  dateBits = dateBits ^ month;
	  
	  //Shift bits over again
	  dateBits = dateBits << 5;
	  
	  //Get the day from MyCalendar
	  int dayBits = 0x1f & modCalendar.get(DAY_OF_MONTH);
	  
	  //Mask all but the last 5 bits of day
	  //int dayMask = 0xFFE0;
	  //dayBits = dayBits ^ dayMask;
	  
	  //Add day to dateBits
	  dateBits = dateBits ^ dayBits;
	  
	  //Truncate to short then store
	  modDate = (short)dateBits;  
    
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
	  reqVersion = versionShort;
	  return true;
  }
  
  //Method to set the compression method for the file
  //Valid values for the compression method are the numbers 0 to 19 and 99
  public boolean setCompressionMethod(short compMethod){
	  if (compMethod == 99){
		  compressionMethod = compMethod;
		  return true;
	  }
	  else if (compMethod < 0 || compMethod > 19){
		  return false;
	  }
	  else{
		  compressionMethod = compMethod;
		  return true;
	  }
  }
  
}