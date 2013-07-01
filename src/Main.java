import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class Main {
  static String workingDir = System.getProperty("user.dir");
  
  public static void main(String[] args) {
    
    File srcDir = new File(workingDir, "src");
    File testFile = new File(srcDir, "test.txt");
    test(testFile);
  }
  
  static void test(File file) {
    FileOutputStream outputZip;
    File outputFile = new File(workingDir, "test.zip");
    try {
      outputZip = new FileOutputStream(outputFile);
      ZipOutputStream zOut = new ZipOutputStream(outputZip);
      zOut.zipFile(file);
      
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }
  
  static void testLittleEndian() {
    byte[] myBytes = {(byte)0x23, (byte)0x4f, (byte)0x21, (byte)0x7e};
    for (byte b : myBytes)
    {
      System.out.println((byte)b);
    }
    myBytes = ZipOutputStream.littleEndian(myBytes);
    for (byte b : myBytes)
    {
      System.out.println((byte)b);
    }
  }
  
  public void zipFile(File file) {
    try {
      InputStream input = new BufferedInputStream(new FileInputStream(file));
      
      // write the header
      //writeLocalHeader();
      
      // write the file data
      int inChar = input.read();
      while(inChar != -1) {
        //super.write(inChar);
        inChar = input.read();
      }
      
      input.close();
      
    } catch (IOException e) {
      System.out.print("IO Exception");
    }
    
  }
}
