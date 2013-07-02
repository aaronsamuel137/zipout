

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;

public class ZipOutputStream extends DeflaterOutputStream{
  private static final int SIGNATURE = 0x04034b50;
  private static final short VERSION = 20;
  private static final short BITFLAG = 8;
  private static final short METHOD = 8;
  
  private final OutputStream out;
  //private final Deflater deflater = null;
  //private final byte[] buffer = null;
  

  //private byte[] SIGNATURE = { 0x50, 0x4B, 0x03, 0x04 };
  //private byte[] RANDOM_BYTES = { 0x2, 0x3 };
  private int offset;
  private boolean closed = false;

  public ZipOutputStream(OutputStream outStream) {
    //super(out, new Deflater(Deflater.DEFAULT_COMPRESSION, true));
    super(outStream);
    out = outStream;
    //out = new BufferedOutputStream(outStream);
    System.out.println(out); 
    offset = 0;
  }

  private void ensureOpen() throws IOException {
    if (closed) {
      throw new IOException("Closed");
    }
  }
  
  public void putNextEntry(ZipEntry z) {
    try {
      ensureOpen();
      writeLocalHeader(z);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public void closeEntry() {
    
  }
  
  private void writeLocalHeader(ZipEntry entry) {
    writeFourBytes(SIGNATURE);
    //writeTwoBytes(VERSION);
    //writeTwoBytes(BITFLAG);
    //writeTwoBytes(METHOD);
    
    // for testing we need 30 byte headers
    /*writeFourBytes(0x1234);
    writeFourBytes(0x1234);
    writeFourBytes(0x1234);
    writeFourBytes(0x1234);
    writeFourBytes(0x1234);*/
  }

  private void writeFileData() {
    
  }
  


  private void writeDataDescriptor() {
      
  }
  

  
  static byte[] littleEndian(byte[] bytes) {
    int length = bytes.length;
    byte[] newBytes = new byte[length];
    for (int i = 0 ; i < length ; i++)
      newBytes[length - i - 1] = bytes[i];
    return newBytes;
  }
  
  public void close() {
    try {
      super.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  private void writeTwoBytes(int bytes) {
    OutputStream out = this.out;
    try {
      out.write(bytes & 0xff);
      out.write((bytes >> 8) & 0xff);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  private void writeFourBytes(int bytes) {
    //OutputStream out = new ByteArrayOutputStream();
    //OutputStream out = this.out;
    try {
      out.write(bytes & 0xff);
      out.write((bytes >> 8) & 0xff);
      out.write((bytes >> 16) & 0xff);
      out.write((bytes >> 24) & 0xff);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
}
