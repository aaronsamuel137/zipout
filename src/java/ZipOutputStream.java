import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;


public class ZipOutputStream extends DeflaterOutputStream{
  //private static final int SIGNATURE = 0x04034b50;

  private byte[] SIGNATURE = { 0x50, 0x4B, 0x03, 0x04 };
  private byte[] RANDOM_BYTES = { 0x2, 0x3 };
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
    ensureOpen();
  }
  
  public void closeEntry() {
    
  }
  
  private void writeLocalHeader(int offset) {
    try {
      out.write(SIGNATURE, offset, offset + 4);
      for (int i = 0 ; i < 13 ; i++ )
        out.write(RANDOM_BYTES, offset + 4 + (i * 2), offset + 6 + (i * 2));
    } catch (IOException e) {
      e.printStackTrace();
    }
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
  
}
