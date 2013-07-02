

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DeflaterOutputStream;

public class ZipOutputStream extends DeflaterOutputStream{
  private static final int SIGNATURE = 0x04034b50;
  private static final short VERSION = 20;
  private static final short BITFLAG = 0x0808;
  private static final short METHOD = 8;
  private static final int CENTRAL_FILE_HEADER = 0x02014b50;
  
  
  private final OutputStream out;
  private DeflaterOutputStream deflaterStream;
  private List<EntryOffset> entries;
  
  private long bytesWritten;
  private boolean closed = false;

  public ZipOutputStream(OutputStream outStream) {
    super(outStream);
    out = outStream;
    bytesWritten = 0;
    entries = new ArrayList<EntryOffset>();
    deflaterStream = new DeflaterOutputStream(outStream);
  }

  private void ensureOpen() throws IOException {
    if (closed) {
      throw new IOException("Closed");
    }
  }
  
  public void putNextEntry(ZipEntry z) {
    try {
      ensureOpen();
      EntryOffset entry = new EntryOffset(bytesWritten, z);
      entries.add(entry);
      writeLocalHeader(z);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public void closeEntry() {
    try {
      ensureOpen();
    } catch (IOException e) {}
  }
  
  private void writeLocalHeader(ZipEntry entry) {
    writeFourBytes(SIGNATURE);
    writeTwoBytes(VERSION);
    writeTwoBytes(BITFLAG);
    writeTwoBytes(METHOD);
    
    for (int i = 0 ; i < 5 ; i++) {
      writeFourBytes(0);
    }
    bytesWritten += 30;
  }
  
  public void write(byte[] b, int offset, int length) {
    try {
      deflaterStream.write(b, offset, length);
      bytesWritten += length;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void writeDataDescriptor() {
    // not needed yet
  }
  
  private void writeCentralDirectoryHeader() {
    writeFourBytes(CENTRAL_FILE_HEADER);
    writeTwoBytes(VERSION);
    writeTwoBytes(VERSION);
    writeTwoBytes(BITFLAG);
    writeTwoBytes(METHOD);
  }
  

  
  static byte[] littleEndian(byte[] bytes) {
    int length = bytes.length;
    byte[] newBytes = new byte[length];
    for (int i = 0 ; i < length ; i++)
      newBytes[length - i - 1] = bytes[i];
    return newBytes;
  }
  
  public void close() {
    // call super.close
    try {
      super.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  private void writeTwoBytes(int bytes) {
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
  
  private class EntryOffset {
    public long offset;
    public ZipEntry entry;
    public EntryOffset(long off, ZipEntry e) {
      this.offset = off;
      this.entry = e;
    }
  }
  
}
