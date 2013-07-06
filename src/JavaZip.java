

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
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;


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
		String testDir;
		File[] testFiles;
		File workingDirFile = null;
		boolean useDefaultTest = false;
		File output = initOutputFile("java.zip");

		Set<String> cmdArgs = new HashSet<String>(Arrays.asList(args));

		if (cmdArgs.contains("mdump"))
			testFiles = (new File(workingDir, "testMdump")).listFiles();
		else if (cmdArgs.contains("logs"))
			testFiles = (new File(workingDir, "testFiles")).listFiles();
		else {
      List<File> testers = createTestFiles();
			testFiles = testers.toArray(new File[testers.size()]);
		}

    if (cmdArgs.contains("one")) {
    	System.out.println("\nzipping with function write(int b)...");
    	writeWithOneParam(testFiles, output);
    }
    else {
    	System.out.println("\nzipping with function write(byte[] b, int offset, int length)...");
    	write(testFiles, output);
    }
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
	
	public static List<File> createTestFiles() {
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

			long startTime = System.currentTimeMillis();
			for (File f : fileList) {
				String name = f.getName();
				System.out.print("zipping " + name + "...\t\t");
				inFile = new BufferedInputStream(new FileInputStream(f));
				ZipEntry entry = new ZipEntry(name);
				outZip.putNextEntry(entry);
			
				int inchar = inFile.read();
				while(inchar != -1) {
          outZip.write(inchar);
          inchar = inFile.read();
        }
				inFile.close();
				outZip.closeEntry();
				System.out.print("done\n");
			}
			outZip.close();
			long endTime = System.currentTimeMillis();
			System.out.print