import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;


public class ZipOutputStream extends DeflaterOutputStream{
  //private static final int SIGNATURE = 0x04034b50;
  byte[] SIGNATURE = { 0x50, 0x4B, 0x03, 0x04 };
  byte[] RANDOM_BYTES = { 0x2, 0x3 };
  private OutputStream out;
  byte[] data;
  
  public ZipOutputStream(OutputStream out) {
      //super(out, new Deflater(Deflater.DEFAULT_COMPRESSION, true));
      super(out);
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
  
  static private byte[] littleEndian(byte[] bytes) {
    int length = bytes.length;
    byte[] newBytes = new byte[length];
    for (int i = 0 ; i < length ; i++)
      newBytes[length - i - 1] = bytes[i];
    return newBytes;
  }
  
  public static void main(String[] args) {
    byte[] myBytes = {(byte)0x23, (byte)0x4f, (byte)0x21, (byte)0x7e};
    for (byte b : myBytes)
    {
        System.out.println((byte)b);
    }
    myBytes = littleEndian(myBytes);
    for (byte b : myBytes)
    {
        System.out.println((byte)b);
    }
  }
}
