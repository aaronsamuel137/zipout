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
	
	private static final String ONE_PARAM_ZIP_NAME = "zosTest1.zip";
	private static final String THREE_PARAM_ZIP_NAME = "zosTest3.zip";

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

	private static final boolean USE_ONE_PARAM_WRITE = true;
	private static final boolean USE_THREE_PARAM_WRITE = false;
	private static byte[] buffer = new byte[1024];
	private File outputZip;
	private int numTestFiles = 0;
	private int numZipFiles = 0;

	public static void main(String[] args)
	{
		// Test 1-param write function
		createZip(USE_ONE_PARAM_WRITE);
		verifyContents(ONE_PARAM_ZIP_NAME);
		// Test 3-param write function
		createZip(USE_THREE_PARAM_WRITE);
		verifyContents(THREE_PARAM_ZIP_NAME);
		// Remove the created zip files
		cleanUp();
	}

	private void createZip(boolean useOneParam)
	{
		try
		{
			// Create a zip file for this test
			outputZip = useOneParam ? new File(ONE_PARAM_ZIP_NAME) : new File(THREE_PARAM_ZIP_NAME);

			// Prepare the streams
			FileOutputStream outputStream = new FileOutputStream(outputZip);
			ZipOutputStream zipContents = new ZipOutputStream(outputStream);

			// Zip the file contents (convert directly from string to bytes)
			long startTime = System.currentTimeMillis();
			for (Map.Entry<String, String> f : FILES_CONTENTS.entrySet())
			{
				numTestFiles += 1;
				String name = f.getKey();
				String contents = f.getValue();

				System.out.println("Zipping " + name + "...");
				ZipEntry entry = new ZipEntry(name);
				zipContents.putNextEntry(entry);

				byte[] bytesToWrite = contents.getBytes();

				if (useOneParam)
				{
					// Use the 1-parameter write method; takes a single byte
					for (int i = 0; i < bytesToWrite.length; i++)
					{
						zipContents.write(bytesToWrite[i]);
					}
				}
				else
				{
					// Use 3-parameter write method; takes a buffer, offset, and length
					zipContents.write(bytesToWrite, 0 , bytesToWrite.length);
				}

				// Done with this file
				zipContents.closeEntry();
				System.out.println("Done");
			}

			// All files have been written
			long endTime = System.currentTimeMillis();
			System.out.println("Finished " + outputZip.getName() + " in " + ((endTime - startTime) / 1000.0) + " seconds");
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			try
			{
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

			// Assert that the zip contained the correct number of files
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
			// Delete the zip files
			File zip1 = new File(ONE_PARAM_ZIP_NAME);
			File zip2 = new File(THREE_PARAM_ZIP_NAME);
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
