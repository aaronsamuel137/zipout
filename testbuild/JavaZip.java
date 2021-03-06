

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;


// Our code must be able to do this
public class JavaZip
{
	public static void main(String[] args)
	{
		byte[] buffer = new byte[1024];
		String workingDir = System.getProperty("user.dir");
		File testFile = new File(workingDir, "test.txt");
		if (testFile.exists())
		  try {
		    testFile.delete();
		  } catch (Exception e) {}
		  
		writeTestFile(testFile);
		
		File outputFile = new File(workingDir, "java.zip");
		if (outputFile.exists()) {
		  try {
		    outputFile.delete();
		  } catch (Exception e) {
		    System.out.println("File not deleted");
		  }
		}

		try
		{
			FileOutputStream outFile = new FileOutputStream(outputFile);
			ZipOutputStream outZip = new ZipOutputStream(outFile);
			InputStream inFile = new FileInputStream(testFile);

			ZipEntry entry = new ZipEntry("test.txt");
			outZip.putNextEntry(entry);
			
			int len = 0;
			while ((len = inFile.read(buffer)) > 0)
			{
				outZip.write(buffer, 0, len);
				//for (byte b : buffer)
				  //if (b != 0)
				    //System.out.println(Integer.toHexString(b));
			}

			inFile.close();
			outZip.closeEntry();
			outZip.close();
		}
		catch (IOException e)
		{
		  System.out.println("Exception caught");
		}
	}
	
	public static void writeTestFile(File file) {
	  try {
  	  if (!file.exists()) {
        file.createNewFile();
      }
  
      FileWriter fw = new FileWriter(file.getAbsoluteFile());
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write("this is a test");
      bw.close();
	  } catch (IOException e) {
	    System.out.println("No test file");
	  }
	}
	
	public static void readBytesFromZip () {
	  
	}
}