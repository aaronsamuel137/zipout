

import java.util.Calendar;

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
  public void setTime(){
	  //Need to expand on this....
    
  }
  
  //Method to set the mi
  
  //Method to set the minium version required to open the zip file
  //Valid values for the compression method are the numbers 10 to 63
  public boolean setRequiredVersion(short compMethod){
	  if (compMethod < 10 || compMethod > 63){
		  return false;
	  }
	  else{
		  COMPRESSION_METHOD = compMethod;
		  return true;
	  }
  }
  
  //Method to set the compression method for the file
  //Valid values for the compression method are the numbers 0 to 19
  public boolean setCompressionMethod(short compMethod){
	  if (compMethod < 0 || compMethod > 19){
		  return false;
	  }
	  else{
		  COMPRESSION_METHOD = compMethod;
		  return true;
	  }
  }
  
  //

  
}
