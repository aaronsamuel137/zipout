
public class ZipOutputStream {
  private static final int SIGNATURE = 0x04034b50;
  private OutputStream out;
  
  public ZipOutputStream(OutputStream out) {
      //super(out, new Deflater(Deflater.DEFAULT_COMPRESSION, true));
      super(out);
  }
  
  private void writeLocalHeader() {
    out.write(SIGNATURE, 0, 4);
    write(0x112233445566778899)
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
