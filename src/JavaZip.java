

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.util.List;
import java.util.ArrayList;


// Our code must be able to do this
public class JavaZip
{
	private static final String TEST1 = "\"this is a test\"";
	private static final String TEST2 = "this is a test, a much\nmore extensive test";
	private static final String TEST3 = "this is a test, a much\nmore extensive test! HURAAH!";
	private static final String TEST4 = "this akwerlthwailt!";
	private static final String workingDir = System.getProperty("user.dir");

	private static byte[] buffer = new byte[1024];
	
	public static void main(String[] args)
	{
		File workingDirFile = new File(workingDir, "testFiles");

		//File[] testDirFiles = workingDirFile.listFiles();
		List<File> testFiles = createTestFiles();
		File[] testArray = testFiles.toArray(new File[testFiles.size()]);

		File output1 = initOutputFile("java.zip");

		writeWithOneParam(testArray, output1);		
	}
	
	public static void writeTestFile(File file, String text) {
	  try {
  	  if (!file.exists()) {
        file.createNewFile();
      }
  
      FileWriter fw = new FileWriter(file.getAbsoluteFile());
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(text);
      bw.close();
	  } catch (IOException e) {
	    System.out.println("No test file");
	  }
	}
	
	public static List<File> createTestFiles () {
	  File testFile = new File(workingDir, "test.txt");
		File testFile2 = new File(workingDir, "test2.txt");
		File testFile3 = new File(workingDir, "test3.txt");
		File testFile4 = new File(workingDir, "test4.txt");

		List<File> testFiles = new ArrayList<File>();
		testFiles.add(testFile);
		testFiles.add(testFile2);
		testFiles.add(testFile3);
		testFiles.add(testFile4);

		for (File f : testFiles) {
			if (f.exists())
			  try {
			    f.delete();
			  } catch (Exception e) {}
		}
		
		writeTestFile(testFile, TEST1);
		writeTestFile(testFile2, TEST2);
		writeTestFile(testFile3, TEST3);
		writeTestFile(testFile4, TEST4);
		return testFiles;
	}

	public static File initOutputFile(String fileName) {
		File outputFile = new File(workingDir, fileName);
		if (outputFile.exists()) {
		  try {
		    outputFile.delete();
		  } catch (Exception e) {
		    System.out.println("File not deleted");
		  }
		}
		return outputFile;
	}

	public static void writeWithOneParam(File[] fileList, File outputFile) {
		try {
			FileOutputStream outFile = new FileOutputStream(outputFile);
			ZipOutputStream outZip = new ZipOutputStream(outFile);

			InputStream inFile;
			System.out.print("writing files, please wait");

			for (File f : fileList) {		
				inFile = new BufferedInputStream(new FileInputStream(f));
				ZipEntry entry = new ZipEntry(f.getName());
				outZip.putNextEntry(entry);
			
				int inchar = inFile.read();
				while(inchar != -1) {
          outZip.write(inchar);
          inchar = inFile.read();
        }
				inFile.close();
				outZip.closeEntry();
			}
			outZip.close();
		}
		catch (IOException e) {
		  e.printStackTrace();
		}
	}

	public static void write(File[] fileList, File outputFile) {
		try {
			FileOutputStream outFile = new FileOutputStream(outputFile);
			ZipOutputStream outZip = new ZipOutputStream(outFile);

			InputStream inFile;
			System.out.print("writing files, please wait");

			for (File f : fileList) {		
				inFile = new FileInputStream(f);					
				ZipEntry entry = new ZipEntry(f.getName());
				outZip.putNextEntry(entry);
			
				int len = inFile.read(buffer);
				while (len > 0) {
					outZip.write(buffer, 0, len);
					len = inFile.read(buffer);
				}
				inFile.close();
				outZip.closeEntry();
			}
			outZip.close();
		}
		catch (IOException e) {
		  e.printStackTrace();
		}
	}
}