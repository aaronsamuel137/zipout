package java.util.zip;
import java.io.IOException;
import java.io.OutputStream;


public class ZipOutputStream extends DeflaterOutputStream{
  private static final int SIGNATURE = 0x04034b50;
  private static final short VERSION = 20;
  private static final short BITFLAG = 8;
  private static final short METHOD = 8;
  

  //private byte[] SIGNATURE = { 0x50, 0x4B, 0x03, 0x04 };
  //private byte[] RANDOM_BYTES = { 0x2, 0x3 };
  private OutputStream out;
  private byte[] data;
  private int offset;
  private boolean closed = false;

  public ZipOutputStream(OutputStream out) {
    //super(out, new Deflater(Deflater.DEFAULT_COMPRESSION, true));
    super(out);
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
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public void closeEntry() {
    
  }
  
  private void writeLocalHeader(int offset) {
    writeFourBytes(SIGNATURE);
    writeTwoBytes(VERSION);
    writeTwoBytes(BITFLAG);
    writeTwoBytes(METHOD);
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
    try {
      out.write(bytes & 0xff);
      out.write((bytes << 8) & 0xff);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  private void writeFourBytes(int bytes) {
    try {
      out.write(bytes & 0xff);
      out.write((bytes << 8) & 0xff);
      out.write((bytes << 16) & 0xff);
      out.write((bytes << 24) & 0xff);
    } catch (IOException e) {}
  }
  
}
