public class ZipOutputStreamTest
{
	private static final String TEST1 = "test1.txt";
	private static final String TEST2 = "test2.txt";
	private static final String TEST3 = "test3.txt";
	private static final String TEST4 = "test4.txt";

	private static final String TEST1_CONTENTS = "\"this is a test\"";
	private static final String TEST2_CONTENTS = "this is a\nmulti-line test";
	private static final String TEST3_CONTENTS = "74 68 69 73 20 69 73 20 61 20 74 65 73 74";
	private static final String TEST4_CONTENTS = "01110100 01101000 01101001 01110011 00100000 01101001 01110011 00100000 01100001 00100000 01110100 01100101 01110011 01110100";
	
	private static final String ONE_PARAM_ZIP = "zosTest1.zip";
	private static final String THREE_PARAM_ZIP = "zosTest3.zip";

	private static final Map<String, String> FILES_CONTENTS;
	static
	{
		Map<String, String> m = new HashMap<String, String>();
		m.put(TEST1, TEST1_CONTENTS);
		m.put(TEST2, TEST2_CONTENTS);
		m.put(TEST3, TEST3_CONTENTS);
		m.put(TEST4, TEST4_CONTENTS);
		FILES_CONTENTS = Collections.unmodifiableMap(m);
	}

	private static byte[] buffer = new byte[1024];
	private List<File> testFiles;
	private File outputFile;

	public static void main(String[] args)
	{
		// Create the test files
		createTestFiles();
		// Test 1-param write function
		useOneParamWrite();
		verifyContents(ONE_PARAM_ZIP);
		// Test 3-param write function
		useThreeParamWrite();
		verifyContents(THREE_PARAM_ZIP);
		// Remove test files
		cleanUp();
	}

	private void createTestFiles()
	{
		// Create test files with known contents
		File f1 = new File(TEST1);
		File f2 = new File(TEST2);
		File f3 = new File(TEST3);
		File f4 = new File(TEST4);

		writeFileContents(f1, TEST1_CONTENTS);
		writeFileContents(f2, TEST2_CONTENTS);
		writeFileContents(f3, TEST3_CONTENTS);
		writeFileContents(f4, TEST4_CONTENTS);

		testFiles = new ArrayList<File>();
		testFiles.add(f1);
		testFiles.add(f2);
		testFiles.add(f3);
		testFiles.add(f4);
	}

	private void writeFileContents(File file, String contents)
	{
		try
		{
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(contents);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			try
			{
				bw.close();
				fw.close();
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
				

		}
	}

	private void useOneParamWrite()
	{
		try
		{
			// Create a zip file for this test
			outputFile = new File(ONE_PARAM_ZIP);

			// Prepare the streams
			FileOutputStream outputStream = new FileOutputStream(outputFile);
			ZipOutputStream zipContents = new ZipOutputStream(outputStream);
			
			InputStream inputFile;

			// Zip the files
			long startTime = System.currentTimeMillis();
			for (File f : testFiles)
			{
				String name = f.getName();
				System.out.println("Zipping " + name + "...");
				inputFile = new BufferedInputStream(new FileInputStream(f));
				ZipEntry entry = new ZipEntry(name);
				zipContents.putNextEntry(entry);

				// Use the 1-parameter write method; takes a single byte
				int inChar = inputFile.read();
				while (inChar != -1)
				{
					zipContents.write(inChar);
					inChar = inputFile.read();
				}

				// Done with this file
				inputFile.close();
				zipContents.closeEntry();
				System.out.println("Done");
			}

			// All files have been written
			long endTime = System.currentTimeMillis();
			System.out.println("Finished " + ONE_PARAM_ZIP + " in " + ((endTime - startTime) / 1000.0) + " seconds");
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			try
			{
				inputFile.close();
				zipContents.close();
				outputStream.close();
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	private void useThreeParamWrite()
	{
		try
		{
			// Create a zip file for this test
			outputFile = new File(THREE_PARAM_ZIP);

			// Prepare the streams
			FileOutputStream outputStream = new FileOutputStream(outputFile);
			ZipOutputStream zipContents = new ZipOutputStream(outputStream);
			
			InputStream inputFile;

			// Zip the files
			long startTime = System.currentTimeMillis();
			for (File f : testFiles)
			{
				String name = f.getName();
				System.out.println("Zipping " + name + "...");
				inputFile = new FileInputStream(f);
				ZipEntry entry = new ZipEntry(f.getName());
				zipContents.putNextEntry(entry);

				// Use the 3-parameter write method; takes a buffer, offset, and length
				int len = inputFile.read(buffer);
				while (len > 0)
				{
					zipContents.write(buffer, 0, len);
					len = inputFile.read(buffer);
				}

				// Done with this file
				inputFile.close();
				zipContents.closeEntry();
				System.out.println("Done");
			}

			// All files have been written
			long endTime = System.currentTimeMillis();
			System.out.println("Finished " + ONE_PARAM_ZIP + " in " + ((endTime - startTime) / 1000.0) + " seconds");
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			try
			{
				inputFile.close();
				zipContents.close();
				outputStream.close();
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	private void verifyContents(String zipName)
	{
		try
		{
			int numTestFiles = testFiles.size();
			int numZipFiles = 0;
			String line;
			String contents;

			// Get the contents of each file in the zip
			ZipFile zf = new ZipFile(zipName);
			for (Enumeration<? extends ZipEntry> e = zf.entries(); e.hasMoreElements();)
			{
				ZipEntry entry = e.nextElement();
				BufferedReader reader = new BufferedReader(new InputStreamReader(zf.getInputStream(entry)));
				contents = "";
				numZipFiles += 1;

				while ((line = reader.readLine()) != null)
				{
					if (contents.length() > 0)
					{
						contents += "\n";
					}
					contents += line;
				}
				reader.close();

				// Assert that this file's contents are correct
				assert(contents.equals(FILES_CONTENTS.get(entry.getName())));
			}
			zf.close();

			// Asser that the zip contained the correct number of files
			assert(numZipFiles == numTestFiles);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			try
			{
				zf.close();
				reader.close();
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	private void cleanUp()
	{
		try
		{
			// Delete the test files
			for (File f : testFiles)
			{
				if (f.exists())
				{
					f.delete();
				}
			}

			// Delete the zip files
			File zip1 = new File(ONE_PARAM_ZIP);
			File zip2 = new File(THREE_PARAM_ZIP);
			if (zip1.exists())
			{
				zip1.delete();
			}
			if (zip2.exists())
			{
				zip2.delete();
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
