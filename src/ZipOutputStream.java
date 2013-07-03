

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Deflater;

public class ZipOutputStream {
  private static final int SIGNATURE = 0x04034b50;
  private static final short VERSION = 20;
  private static final short BITFLAG = 0x0808;
  private static final short METHOD = 8;
  private static final int CENTRAL_FILE_HEADER = 0x02014b50;
  private static final int DATA_DESCRIPTER_HEADER = 0x08074b50;
  
  
  private final OutputStream out;
  private DeflaterOutputStream deflaterStream;
  private Deflater deflater = null;
  private List<EntryOffset> entries;
  private CRC32 crc = new CRC32();
  private EntryOffset currentEntry;
  
  private long bytesWritten;
  private boolean closed = false;
  private byte[] buffer;

  public ZipOutputStream(OutputStream outStream) {
    //super(outStream);
    out = outStream;
    bytesWritten = 0;
    entries = new ArrayList<EntryOffset>();
    //deflaterStream = new DeflaterOutputStream(outStream);
    deflater = new Deflater();
    buffer = new byte[1024 * 4];
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
      currentEntry = entry;
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
      System.out.println("here");
      writeDataDescripter(currentEntry);
      writeCentralDirectoryHeader(currentEntry);
      System.out.println("here");
    } catch (IOException e) {
      System.out.println("Exception in closeEntry");
    }
  }
  
  private void writeLocalHeader(ZipEntry e) {
    writeFourBytes(SIGNATURE);
    writeTwoBytes(VERSION);
    writeTwoBytes(BITFLAG);
    writeTwoBytes(METHOD);

    writeTwoBytes(e.modTime);
    writeTwoBytes(e.modDate);

    // CRC is 0 for local header
    writeFourBytes(0); // maybe should be 8 bytes?

    // if flag is not set, size is written as 0 here
    // and written after the file
    writeFourBytes(0);    // compressed size
    writeFourBytes(0);    // uncompressed size

    writeTwoBytes(e.name.length());
    writeTwoBytes(0);     //extra field length, 0 for default

    int len = writeVariableByteLength(e.getName());

    bytesWritten += 30 + len;
  }
  
  private void writeDataDescripter(EntryOffset currentEntry) {
    writeFourBytes(DATA_DESCRIPTER_HEADER);
    writeFourBytes(currentEntry.entry.crc);
    writeFourBytes(currentEntry.entry.compSize); 
    writeFourBytes(currentEntry.entry.uncompSize);
  }
  
  public void write(byte[] b, int offset, int length) {
    try {
      deflater = new Deflater();
      deflater.setInput(b, offset, length);
      while (deflater.getRemaining() > 0)
        deflate();      
      deflater.finish();
      while (!deflater.finished())
        deflate();
      deflater.dispose();
      //super.write(b, offset, length);
      bytesWritten += length;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void deflate() throws IOException {
    int len = deflater.deflate(buffer, 0, buffer.length);
    if (len > 0)
      out.write(buffer, 0 , len);
  }
  
  private void writeCentralDirectoryHeader(EntryOffset e) {
    writeFourBytes(CENTRAL_FILE_HEADER);
    writeTwoBytes(VERSION);
    writeTwoBytes(VERSION);
    writeTwoBytes(BITFLAG);
    writeTwoBytes(METHOD);
    
    writeFourBytes(0); // last mod time
    
    writeFourBytes(e.entry.crc);
    
    // place holder for size and compressed size
    for (int i = 0 ; i < 2 ; i++) {
      writeFourBytes(0);
    }
    
    // file name length
    writeTwoBytes(e.entry.getName().length() * 2);
    
    writeTwoBytes(0); // extra field length is 0 since we didn't use
    writeTwoBytes(0); // comment length is 0 too
    writeTwoBytes(0); // disk number start?
    writeTwoBytes(0); // internal file attribute
    writeFourBytes(0); // external file attribute
    writeFourBytes((int) e.offset); // relative offset of local header  
  }
  
  private void writeEndofCentralDirectory() {
    // TODO
  }
  
  public void close() {
    // call super.close
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

  private int writeVariableByteLength(String text) {
    try {
      byte[] bytes = text.getBytes("UTF-8");
      out.write(bytes, 0, bytes.length);
      return bytes.length;
    } catch (Exception ex) {
      System.out.println("Unsupported byte encoding");
    }
    return 0;
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
