

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Deflater;
import java.io.BufferedOutputStream;

public class ZipOutputStream extends DeflaterOutputStream {
  private static final int SIGNATURE =                    0x04034b50;
  private static final short VERSION =                    20;
  private static final short BITFLAG =                    8;//0x0808;
  private static final short METHOD =                     8;
  private static final int CENTRAL_FILE_HEADER =          0x02014b50;
  private static final int DATA_DESCRIPTER_HEADER =       0x08074b50;
  private static final int END_OF_CENTRAL_DIRECTORY_SIG = 0x06054b50;
  private static final int DEFAULT_LEVEL =                6;
  
  
  private final OutputStream out;
  private DeflaterOutputStream deflaterStream;
  private Deflater deflater = null;
  private List<ZipEntry> entries;
  private CRC32 crc;
  private ZipEntry currentEntry;
  
  private int bytesWritten;
  private int sizeOfCentralDirectory;
  private boolean closed = false;
  private byte[] buffer;

  public ZipOutputStream(OutputStream outStream, int bufferSize) {
    super(outStream);
    out = outStream;
    bytesWritten = 0;
    entries = new ArrayList<ZipEntry>();
    sizeOfCentralDirectory = 0;
    buffer = new byte[bufferSize];
  }

  public ZipOutputStream(OutputStream outStream) {
    this(outStream, 4 * 1024);
  }

  private void ensureOpen() throws IOException {
    if (closed) {
      throw new IOException("Closed");
    }
  }
  
  public void putNextEntry(ZipEntry z) throws IOException {
    ensureOpen();
    z.offset = bytesWritten;
    currentEntry = z;
    entries.add(z);
    writeLocalHeader(z);
  }
  
  public void closeEntry() throws IOException {
    ensureOpen();
    crc.reset();
    writeDataDescripter(currentEntry);
  }
  
  private void writeLocalHeader(ZipEntry e) throws IOException {
    writeFourBytes(SIGNATURE);       // local header signature
    writeTwoBytes(VERSION);          // version used
    writeTwoBytes(BITFLAG);          // flags
    writeTwoBytes(METHOD);           // compression method
    writeTwoBytes(e.modTime);        // last modified time
    writeTwoBytes(e.modDate);        // last modified date   
    writeFourBytes(0);               // CRC is 0 for local header

    // with default flag settings, the compressed and uncompressed size
    // is written here as 0 and written correctly in the data descripter
    writeFourBytes(0);               // compressed size
    writeFourBytes(0);               // uncompressed size
    writeTwoBytes(e.name.length());  // length of file name

    // extra field length, in this implementation extra field in not used
    writeTwoBytes(0);

    // write file name, return the number of bytes written
    int len = writeVariableByteLength(e.getName());
    System.out.println("local " + len);

    bytesWritten += 30 + len;
  }
  
  private void writeDataDescripter(ZipEntry currentEntry) throws IOException {
    writeFourBytes(DATA_DESCRIPTER_HEADER);        // data descripter header
    writeFourBytes(currentEntry.crc);        // crc value
    writeFourBytes(currentEntry.compSize);   // compressed size
    writeFourBytes(currentEntry.uncompSize); // uncompressed size
    bytesWritten += 16;
  }
  
  public void write(byte[] b, int offset, int length) throws IOException {
    currentEntry.uncompSize = length;
    crc = new CRC32();
    crc.update(b, offset, length);
    currentEntry.crc = (int) crc.getValue();
    
    deflater = new Deflater(DEFAULT_LEVEL, true);
    deflater.setInput(b, offset, length);


    while (deflater.getRemaining() > 0)
      deflate();

    deflater.finish();
    while (!deflater.finished()) {
      deflate();
    }
    deflater.dispose();

    bytesWritten += length;
  }

  private void deflate() throws IOException {
    int len = deflater.deflate(buffer, 0, buffer.length);
    currentEntry.compSize = len;
    if (len > 0)
      out.write(buffer, 0 , len);
  }
  
  private void writeCentralDirectoryHeader(ZipEntry e) throws IOException {
    writeFourBytes(CENTRAL_FILE_HEADER);
    writeTwoBytes(VERSION);
    writeTwoBytes(VERSION);
    writeTwoBytes(BITFLAG);
    writeTwoBytes(METHOD);
    
    writeTwoBytes(e.modTime);            // last mod time    
    writeTwoBytes(e.modDate);            // last mod date
    writeFourBytes(e.crc);               // crc
    writeFourBytes(e.compSize);          // compressed size
    writeFourBytes(e.uncompSize);        // uncompressed size
   
    writeTwoBytes(e.getName().length()); // file name length
    
    writeTwoBytes(0); // extra field length is 0 since we didn't use
    writeTwoBytes(0); // comment length is 0 too
    writeTwoBytes(0); // disk number start?
    writeTwoBytes(0); // internal file attribute
    writeFourBytes(0); // external file attribute
    writeFourBytes((int) e.offset); // relative offset of local header

    int len = writeVariableByteLength(e.entry.getName());
    System.out.println("CENTRAL LENGTH " + len);

    bytesWritten += 46 + len;
    sizeOfCentralDirectory += 46 + len;
  }
  
  private void writeEndofCentralDirectory(int offset) throws IOException {
    short numEntries = (short) entries.size();
    writeFourBytes(END_OF_CENTRAL_DIRECTORY_SIG);
    writeTwoBytes(0);
    writeTwoBytes(0);
    writeTwoBytes(numEntries);
    writeTwoBytes(numEntries);
    writeFourBytes(sizeOfCentralDirectory);     // length of central directory
    writeFourBytes(offset);                     // offset of central directory
    writeTwoBytes(0);                           // length of added comments, not used
    bytesWritten += 22;
  }
  
  public void close() throws IOException {
    int offset = bytesWritten;
    for (ZipEntry e : entries)
      writeCentralDirectoryHeader(e);
    writeEndofCentralDirectory(offset);
    System.out.println("offset " + offset);
    System.out.println("Close " + bytesWritten);
  }
  
  private void writeTwoBytes(int bytes) throws IOException {
    out.write(bytes & 0xff);
    out.write((bytes >> 8) & 0xff);
  }
  
  private void writeFourBytes(int bytes) throws IOException {
    out.write(bytes & 0xff);
    out.write((bytes >> 8) & 0xff);
    out.write((bytes >> 16) & 0xff);
    out.write((bytes >> 24) & 0xff);
  }

  private int writeVariableByteLength(String text) throws IOException {
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
